package ga_complex_planning;

import ga_complex_planning.planning_info.Info;
import ga_complex_planning.pojo.Point;
import util.GAGraphUtil;
import util.PropertyUtil;

import java.util.*;

/**
 * @ClassName GeneticAlgorithm
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:35 下午
 * @Version 1.0
 **/
public class GeneticAlgorithm extends Codec implements RouteCalculator {

    private Properties gaComplexPro = PropertyUtil.getProperty("ga_complex");
    private Properties planningInfoPro = PropertyUtil.getProperty("planning_info");

    private Map<Double,Double> bestScoreDataMap = new HashMap<>();
    private Map<Double,Double> worstScoreDataMap = new HashMap<>();
    private Map<Double,Double> totalScoreDataMap = new HashMap<>();

    public static int underZeroCount = 0;

    private static final Random r = new Random();

    // 当且仅当 GeneticAlgorithm 对象被实例化后，下面这段静态代码块才会执行
    // 此时，codeMap 的值才会存在
    // 也就是说，GA 对象没有创建的时候，Codec 就是无效的
    static {
        // 默认的解码是 A->1  B->2
        // 后期可以修改为读取配置文件中的解码信息
        for (int i = 1; i <=26 ; i++) {
            codeMap.put(String.valueOf(i),String.valueOf((char) (64+i)));
        }
        codeMap.put("0","start");
    }

    // 受灾地信息
    private Map<String, Point> info = Info.getInfo();

    // 车辆数目
    private int CAR_NUM = Integer.valueOf(planningInfoPro.getProperty("CAR_NUM"));
    // 车辆载重
    private double CAR_CAPACITY = Double.valueOf(planningInfoPro.getProperty("CAR_CAPACITY"));
    // 车辆速度
    private double CAR_SPEED = Double.valueOf(planningInfoPro.getProperty("CAR_SPEED"));
    // 对时间窗需求的权重
    private double TIME_WINDOW_WEIGHT = Double.valueOf(gaComplexPro.getProperty("TIME_WINDOW_WEIGHT"));
    // 对货物需求的权重
    private double GOOD_NEED_WEIGHT = Double.valueOf(gaComplexPro.getProperty("GOOD_NEED_WEIGHT"));
    // 受灾点个数  start 点不能计算在其中
    private int POINT_NUM = info.size()-1;
    // 个体初始得分，设置初始得分是为了防止个体得分出现负数
    private double ORIGIN_SCORE = Double.valueOf(gaComplexPro.getProperty("ORIGIN_SCORE"));
    // 应急点紧急程度对应的权值
    private double EMERGENCY_WEIGHT = Double.valueOf(gaComplexPro.getProperty("EMERGENCY_WEIGHT"));

    // 种群
    private List<Chromosome> pop = new ArrayList<>();
    // 种群大小
    private int POP_SIZE = Integer.valueOf(gaComplexPro.getProperty("POP_SIZE"));
    // 迭代次数
    private int ITER_NUM = Integer.valueOf(gaComplexPro.getProperty("ITER_NUM"));
    // 迭代次数计数
    private int iterCount = 0;



    // 基因长短
    private int geneSize = calculateGeneSize();
    // 当前种群最佳适应度
    private double bestScore = Double.MIN_VALUE;
    // 当前种群最坏适应度
    private double worstScore = Double.MAX_VALUE;
    // 当前种群总适应度
    private double totalScore = 0;
    // 当前种群平均适应度
    private double averageScore = 0;
    // 当前种群最佳基因
    private int[] bestGene = null;
    // 当前种群最坏基因
    private int[] worstGene = null;

    // 变异概率
    private double MUTATION_RATE = Double.valueOf(gaComplexPro.getProperty("MUTATION_RATE"));
    // 最大变异长度
    private int MAX_MUTATION_NUM = geneSize/2;


    /**
     * 执行遗传算法函数
     * @return 执行结束后最佳的种群基因
     */
    public int[] conductGA() {
        long startTime = System.currentTimeMillis();
        init();
        for (int i = 0; i < ITER_NUM; i++) {
            // 1、计算种群适应度
            calculatePopScore();
            print(i+1);
            // 2、交叉生成新的种群
            evolve();
            // 3、种群变异
            mutation();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("==============================");
        System.out.println("出现负值结果个数为："+GeneticAlgorithm.underZeroCount);
        System.out.println("平均每次迭代出现负值的个数为:"+GeneticAlgorithm.underZeroCount/500);
        System.out.println("运算耗时为："+(endTime-startTime)+"ms");
        Map[] bestWorstDataSet = new Map[2];
        Map[] totalDataSet = new Map[1];
        bestWorstDataSet[0]=bestScoreDataMap;
        bestWorstDataSet[1]=worstScoreDataMap;
        totalDataSet[0]=totalScoreDataMap;
        GAGraphUtil.drawBestWorstScoreGraph(bestWorstDataSet);
        GAGraphUtil.drawTotalScoreGraph(totalDataSet);
        GAGraphUtil.blockUtil();
        return bestGene;
    }

    /**
     * 临时打印相关信息
     */
    private void print(int generation) {
        System.out.println("----------------------------------------");
        System.out.println("当前代数："+generation);
        System.out.println("当前种群最佳适应度："+bestScore);
        System.out.println("当前种群最坏适应度："+worstScore);
        System.out.println("当前种群总适应度："+totalScore);
        System.out.println("当前种群平均适应度："+averageScore);
        System.out.println("当前种群最佳基因："+Arrays.toString(bestGene));
        System.out.println("当前种群最坏基因："+Arrays.toString(worstGene));
    }

    /**
     * 初始化种群
     */
    private void init() {
        for (int i = 0; i < POP_SIZE ;i++) {
            Chromosome chromosome = new Chromosome(CAR_NUM, POINT_NUM);
            pop.add(chromosome);
        }
    }

    /**
     * 计算种群适应度
     */
    private void calculatePopScore() {
        iterCount++;
        if (pop==null || pop.size()==0) return;
        totalScore=0;
        averageScore=0;
        bestScore=0;
        worstScore=Double.MAX_VALUE;
        bestGene=null;
        worstGene=null;
        for (Chromosome chromosome : pop) {
            calculateScore(chromosome);
            //System.out.println(chromosome);
            totalScore+=chromosome.getScore();
            if (chromosome.getScore()>bestScore) {
                bestScore=chromosome.getScore();
                bestGene= chromosome.getGene();
            }
            if (chromosome.getScore()<worstScore){
                worstScore= chromosome.getScore();
                worstGene= chromosome.getGene();
            }
            averageScore=totalScore/POP_SIZE;
            averageScore=averageScore>bestScore?bestScore:averageScore;
        }
        // 将待绘制的折线图数据信息存入 map 中
        bestScoreDataMap.put((double) iterCount,bestScore);
        worstScoreDataMap.put((double) iterCount,worstScore);
        totalScoreDataMap.put((double) iterCount,totalScore);
    }

    // TODO: 使用强硬的适应度计算策略 <- 1、在初始化种群的时候 2、在父代交叉的时候
    /**
     * 计算个体适应度 <---------  这里可以作为论文的研究点？
     *
     * 这里的适应度计算采取的是温和策略，即：
     * 1、不在时间窗内到达的话，不会舍去该路径
     * 2、车辆货物余额不满足受灾点要求的话，不会舍去该路径
     * 而是减少适应度得分
     *
     *
     * 需要考虑：
     * 1、单个车体载重能否满足所有受灾点
     * 2、单个车体到达时间是否在时间窗之内
     * 3、受灾点的紧急程度
     * @param chromosome
     */
    private void calculateScore(Chromosome chromosome) {
        if (!Chromosome.isGoodChromosome(chromosome)) {
            throw new RuntimeException("错误：当前染色体出现错误，无法计算");
        }
        // 可能染色体序列 0 5 0 3 2 1 4 0   ： start E start C B A D start

        // 已转录基因组
        // 不同车辆行驶的路径已经切分开了 : 0 5 0 3 2 1 4 0 -> [[start,E,start],
        //                                                [start,C,B,A,D,start]]
        List<List<String>> decodedGeneList = decodeGene(chromosome);
        double scoreCount = 0d;
        // 每个片段，代表一辆车的行驶路径
        for (List<String> slice : decodedGeneList) {
            // 记录车辆剩余货物重量
            double carCurrCapacity = CAR_CAPACITY;
            // 记录当前车辆已经使用的时间（方便与时间窗进行对比）
            double currCarTotalTime=0;
            // 紧急程度权值计算起点
            int emergencyStart = POINT_NUM;
            for (int i = 0; i < slice.size() - 1; i++) {
                // 获取起始点和终点信息
                Point startPoint = info.get(slice.get(i));
                Point endPoint = info.get(slice.get(i+1));

                // 计算两点之间路径长度
                double routeLength = routeLength(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                // 行驶时间
                double routeTime = routeLength / CAR_SPEED;

                currCarTotalTime+=routeTime;
                // 1、是否符合时间窗
                if (currCarTotalTime>endPoint.getEnd()) {
                    scoreCount=scoreCount-(currCarTotalTime-endPoint.getEnd())*TIME_WINDOW_WEIGHT;
                }
                // 当前车辆运行时间还要加上受灾点需要的服务时间
                currCarTotalTime+=endPoint.getServiceTime();
                // 2、车辆运载的货物重量是否满足受灾点需求
                if (carCurrCapacity<endPoint.getNeed()) {
                    scoreCount=scoreCount-(endPoint.getNeed()-carCurrCapacity)*GOOD_NEED_WEIGHT;
                    carCurrCapacity=0;
                }
                // 减去当前受灾点消耗掉的货物
                carCurrCapacity-=endPoint.getNeed();
                // 3、计算车辆紧急程度权值
                int timeCore = emergencyStart * emergencyStart; // 扩大紧急程度对对总分数的影响
                scoreCount+=endPoint.getEmergency()*timeCore*EMERGENCY_WEIGHT;
                emergencyStart--;
            }
        }
        // 统计出现负值分数的个数
        if (scoreCount<0) {
            underZeroCount++;
        }
        // 如果适应度分数为0，要做补偿
        scoreCount+=ORIGIN_SCORE;
        //System.out.println("score为："+scoreCount);
        // 最终策略，防止适应度分数小于0，使轮盘赌失效
        // 如果出现大量分数为负数的情况，绘制出的图形会出现异常
        scoreCount = Math.max(0,scoreCount);
        chromosome.setScore(scoreCount);
    }

    /**
     * 轮盘赌算法  获取较优的父个体
     * @return
     */
    private Chromosome getParentChromosome() {
        // 轮盘赌选中的部分
        double slice = Math.random() * totalScore;
        //System.out.println("轮盘赌 slice:"+slice);
        //System.out.println("轮盘赌 total:"+totalScore);
        double sum = 0d;
        //int targetCount =0;
        for (Chromosome chromosome : pop) {
            //targetCount++;
            sum+=chromosome.getScore();
            // 轮盘赌选中
            if (sum>slice) {
                //System.out.println("当前命中的目标为："+targetCount);
                return chromosome;
            }
            //System.out.println("轮盘赌选中个体为:\n"+chromosome);
        }
        return pop.get(pop.size()-1);
    }

    /**
     * 交叉产生新的子代
     * 这里要注意，一定要避免迭代次数过多导致的程序阻塞
     *
     */
    private void evolve() {
        List<Chromosome> newPop = new ArrayList<>();
        while (newPop.size()<POP_SIZE) {
            Chromosome p1 = getParentChromosome();
            Chromosome p2 = getParentChromosome();
            // genetic 保证生成的子代一定合法
            List<Chromosome> children = Chromosome.genetic(p1, p2);
            for (Chromosome child : children) {
                if (Chromosome.isGoodChromosome(child)) {
                    newPop.add(child);
                }
            }
        }
        // 保证新生成的子代长度与原父代长度相等
        while (newPop.size()>POP_SIZE) {
            newPop.remove(r.nextInt(newPop.size()));
        }
        pop.clear();
        pop=newPop;
    }

    /**
     * 种群变异
     * 种群变异为了保证变异后子代还是符合条件的
     * 必须对基因组序列进行两两交换
     */
    private void mutation() {
        for (Chromosome chromosome : pop) {
            if (Math.random()<MUTATION_RATE) {
                chromosome.mutation(MAX_MUTATION_NUM);
            }
        }
    }

    /**
     * 计算两点之间的路径长度
     * @param fromX
     * @param fromY
     * @param destX
     * @param destY
     * @return
     */
    @Override
    public double routeLength(double fromX, double fromY, double destX, double destY) {
        double deltaX = fromX - destX;
        double deltaY = fromY - destY;
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
    }

    /**
     * 计算基因长短
     * 因为路径规划问题中，基因长短和车辆个数和受灾点有关系
     * 所以需要额外开函数计算
     * @return
     */
    private int calculateGeneSize() {
        return CAR_NUM+POINT_NUM+1;
    }

}
