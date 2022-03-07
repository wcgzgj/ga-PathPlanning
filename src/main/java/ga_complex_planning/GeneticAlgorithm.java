package ga_complex_planning;

import ga_complex_planning.planning_info.Info;
import ga_complex_planning.pojo.Point;
import util.PropertyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
    private int CAR_CAPACITY = Integer.valueOf(planningInfoPro.getProperty("CAR_CAPACITY"));
    // 车辆速度
    private int CAR_SPEED = Integer.valueOf(planningInfoPro.getProperty("CAR_SPEED"));
    // 对时间窗需求的权重
    private double TIME_WINDOW_WEIGHT = Double.valueOf(gaComplexPro.getProperty("TIME_WINDOW_WEIGHT"));
    // 对货物需求的权重
    private double GOOD_NEED_WEIGHT = Double.valueOf(gaComplexPro.getProperty("GOOD_NEED_WEIGHT"));
    // 受灾点个数
    private int POINT_NUM = info.size();

    // 种群
    private List<Chromosome> pop = new ArrayList<>();
    // 种群大小
    private int POP_SIZE = Integer.valueOf(gaComplexPro.getProperty("POP_SIZE"));
    // 迭代次数
    int ITER_NUM = Integer.valueOf(gaComplexPro.getProperty("ITER_NUM"));


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
    // 总迭代中的最佳基因
    private int[] bestGene = null;
    // 总迭代中的最坏基因
    private int[] worstGene = null;



    /**
     * 执行遗传算法函数
     */
    public void conductGA() {
        for (int i = 0; i < ITER_NUM; i++) {

        }
    }

    /**
     * 计算种群适应度
     */
    private void calculatePopScore() {
        if (pop==null || pop.size()==0) return;
        for (Chromosome chromosome : pop) {
            calculateScore(chromosome);
            totalScore+=chromosome.getScore();
            if (chromosome.getScore()>bestScore) {
                bestScore=chromosome.getScore();
                bestGene= chromosome.getGene();
            } else if (chromosome.getScore()<worstScore){
                worstScore= chromosome.getScore();
                worstGene= chromosome.getGene();
            }
            averageScore=totalScore/POP_SIZE;
            // TODO: 种群适应度计算测试！！！
            averageScore=averageScore>bestScore?bestScore:averageScore;
        }
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
            int carCurrCapacity = CAR_CAPACITY;
            // 记录当前车辆已经使用的时间（方便与时间窗进行对比）
            double currCarTotalTime=0;
            for (int i = 0; i < slice.size() - 1; i++) {
                // 获取起始点和终点信息
                Point startPoint = info.get(slice.get(i));
                Point endPoint = info.get(slice.get(i+1));

                // 计算两点之间路径长度
                double routeLength = routeLength(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
                // 行驶时间
                double routeTime = routeLength / CAR_SPEED;

                // TODO : 适应度计算 个体适应度计算测试！!!
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
