package ga_complex_planning;

import com.sun.tools.javac.util.Pair;

import java.util.*;

/**
 * @ClassName Chromosome
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:35 下午
 * @Version 1.0
 **/
public class Chromosome {
    private int[] gene;
    private int geneSize;
    private double score;
    private int carNum;
    private int pointNum;

    private static Random r = new Random();

    public Chromosome() {}

    public Chromosome(int carNum, int pointNum) {
        this.carNum=carNum;
        this.pointNum=pointNum;
        geneSize = carNum+1+pointNum;
        gene = new int[geneSize];
        for (int i = 0; i < geneSize; i++) {
            gene[i]=-1;
        }
        init();
    }

    /**
     * 初始化个体基因，需要保证
     * 1、基因中起始点0个数 为 carNum +1
     * 2、每个点只能出现一次，且所有点都必须出现
     * 3、两个起始点不可以相邻
     * 4、基因收尾必须是起始点位置 0
     *
     * 假设 carNum = 2  pointNum =4
     * 正确的基因:
     * 0 2 3 0 1 4 0
     *
     * 错误的基因：
     * 0 3 3 0 1 4 0  (3受灾点重复)
     * 0 0 3 2 1 4 0  (有两个起始点重合->有车子没出发)
     */
    // TODO: 记录生成过程中出现过的不合格的位置，
    //  以减少 carNum 趋近于 points 时出现的初始化时间越来越长的问题
    public void init() {
        int remainStartPoints = carNum + 1;
        gene[0]=0;
        gene[geneSize-1]=0;
        remainStartPoints-=2;
        // 存放未被放置的受灾点
        List<Integer> pointList = new ArrayList<>();
        for (int i = 1; i <= pointNum ; i++) {
            pointList.add(i);
        }
        // 随机生成起始点
        if (carNum>1 && geneSize>4) {
            //System.out.println("还需要被放置的起始点个数为:"+remainStartPoints);
            while (remainStartPoints>0) {
                // 随机基因起始点
                int tmpStartIndex = r.nextInt(geneSize - 4) + 2;
                if (gene[tmpStartIndex]!=0 && gene[tmpStartIndex-1]!=0 && gene[tmpStartIndex+1]!=0) {
                    gene[tmpStartIndex]=0;
                    remainStartPoints--;
                }
            }
        }

        //System.out.println("放置完起始点后的基因为:"+Arrays.toString(gene));
        // 将受灾点坐标，放进基因组中
        for (int i = 0; i < geneSize; i++) {
            if (gene[i]==-1) {
                int randomIndex = 0;
                if (pointList.size()>0) {
                    randomIndex = r.nextInt(pointList.size());
                }
                gene[i]=pointList.get(randomIndex);
                pointList.remove(randomIndex);
            }
        }
    }


    /**
     * 判断当前染色体是否符合要求
     * 1、基因中起始点0个数 为 carNum +1
     * 2、每个点只能出现一次，且所有点都必须出现
     * 3、两个起始点不可以相邻
     * 4、基因收尾必须是起始点位置 0
     * 这个函数主要是用在交叉后对子代进行判断的
     * 这里还要注意一点，如果生成的子代不满足条件
     * 我们不应该重新轮盘赌选择父代，而是重新选择交叉片段的位移
     * @param chromosome
     * @return
     */
    public static boolean isGoodChromosome(Chromosome chromosome) {
        // 1、基因不存在
        if (chromosome==null || chromosome.getGene()==null || chromosome.getGene().length==0) return false;
        int[] gene = chromosome.getGene();
        List<Integer> genePointList = new ArrayList<>();
        // 基因中目标起始点的个数
        int targetStartPointNum = chromosome.getCarNum() + 1;
        int startPointCount = 2;
        if (gene[gene.length-2]==0) return false;
        for (int i = 1; i < gene.length-1; i++) {
            if (gene[i]==0 && gene[i-1]==0) {
                // 2、两个起始点邻接
                return false;
            }
            if (gene[i]==0) {
                startPointCount++;
            }
            else {
                genePointList.add(gene[i]);
            }
        }
        // 3、起始点个数不满足要求
        if (startPointCount!=targetStartPointNum) return false;
        // 4、不满足囊括所有受灾点
        // 受灾点排布必须满足 n=N 时，为  1,2,...N
        genePointList.sort((o1, o2) -> o1-o2);
        if (genePointList.get(0)!=1) return false;
        for (int i = 1; i <genePointList.size() ; i++) {
            if (genePointList.get(i)-1!=genePointList.get(i-1)) return false;
        }
        return true;
    }

    /**
     * 交叉生成两个新的子代
     * 这里确保两个子代都是符合染色体规范的
     * @param p1
     * @param p2
     * @return
     */
    public static List<Chromosome> genetic(Chromosome p1,Chromosome p2) {
        if (p1==null || p2==null) return null;
        if (p1.getGeneSize()!=p2.getGeneSize()) return null;
        List<Chromosome> children = new ArrayList<>();

        // 起始点可能的集合（后期起始点可能不会设置为0）
        Set<Integer> startPointSet = new HashSet<>();
        startPointSet.add(1);
        List<Pair<Integer, Integer>> possibleGeneticPair = getPossibleGeneticPair(p1, p2, startPointSet);
        // 获取交叉位置
        Pair<Integer, Integer> pair = possibleGeneticPair.get(r.nextInt(possibleGeneticPair.size()));
        Chromosome c1 = p1.clone();
        Chromosome c2 = p2.clone();
        int[] c1Gene = c1.getGene();
        int[] c2Gene = c2.getGene();
        int left = pair.fst;
        int right = pair.snd;
        for (int i = left; i <=right ; i++) {
            int tmp = c1Gene[i];
            c1Gene[i]=c2Gene[i];
            c2Gene[i]=tmp;
        }
        children.add(c1);
        children.add(c2);
        return children;
    }

    /**
     * 获取所有可能的交叉位置
     * 使用双指针法获取
     * 如果排除收尾后，还是，没有获取对应的点的信息
     * 我们直接获取收尾（交换后的结果，就是两个染色体位置互换）
     * @param p1
     * @param p2
     * @return
     */
    private static List<Pair<Integer,Integer>> getPossibleGeneticPair(Chromosome p1,Chromosome p2,Set<Integer>startPointSet) {
        if (p1==null || p2==null) return null;
        if (p1.getGeneSize()!=p2.getGeneSize()) return null;
        List<Pair<Integer, Integer>> res = new ArrayList<>();
        int geneSize = p1.getGeneSize();
        // 暂时不包含首尾
        for (int l = 1; l <geneSize-1 ; l++) {
            for (int r = l; r <geneSize-1 ; r++) {
                if (isSwapScopeLegal(l,r,p1,p2,startPointSet)) {
                    res.add(new Pair<>(l,r));
                }
            }
        }
        return res;
    }

    /**
     * 计算范围中起始点的个数是否一致
     * 这样可以加速符合条件的子代生成
     * @param left
     * @param right
     * @return
     */
    private static boolean isSwapScopeLegal(int left,int right,Chromosome p1,Chromosome p2,Set<Integer>startPointSet) {
        if (left<0 || right<0 || left>=p1.getGeneSize() || right>=p1.getGeneSize()) return false;
        int[] p1Gene = p1.getGene();
        int[] p2Gene = p2.getGene();
        //int p1StartCount=0;
        //int p2StartCount=0;
        //for (int i = left; i <=right ; i++) {
        //    if (startPointSet.contains(p1Gene[i])) p1StartCount++;
        //    if (startPointSet.contains(p2Gene[i])) p2StartCount++;
        //}
        //return p1StartCount==p2StartCount;
    }


    /**
     * 基因变异
     * 为保证符合路径要求，基因变异只能进行基因组片段交换
     * 且交换条件如下：
     * 1、两两都不是起始点
     * 2、两个中只能有一个为起始点，且这个起始点不可以是头或尾
     * @param mutationNum
     */
    public void mutation(int mutationNum) {

    }

    /**
     * 对染色体进行深拷贝
     * @return
     */
    public Chromosome clone() {
        Chromosome child = new Chromosome();
        child.setCarNum(carNum);
        child.setPointNum(pointNum);
        child.setGeneSize(geneSize);
        int[] pGene = gene;
        int[] cGene = new int[pGene.length];
        for (int i = 0; i < pGene.length; i++) {
            cGene[i]=pGene[i];
        }
        child.setGene(cGene);
        return child;
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public int getGeneSize() {
        return geneSize;
    }

    public void setGeneSize(int geneSize) {
        this.geneSize = geneSize;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("基因为:").append(Arrays.toString(gene)).append("\n")
                .append("适应度为:").append(score)
                .toString();
    }
}
