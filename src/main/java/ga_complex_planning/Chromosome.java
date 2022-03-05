package ga_complex_planning;

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

    public Chromosome(int carNum,int pointNum) {
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
        //System.out.println("待选择的受灾点为:"+pointList);
        // 受灾点之间定义的步长

        int step = (geneSize - 2) / (remainStartPoints + 1);
        //System.out.println("步长为:"+step);
        int cursor=1;
        // 按照既定步长，放置起始点
        while (remainStartPoints-->0) {
            cursor+=step;
            gene[cursor]=0;
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
     * @param chromosome
     * @return
     */
    public boolean isGoodChromosome(Chromosome chromosome) {
        int[] gene = chromosome.getGene();
        // 基因中目标起始点的个数
        int targetStartPointNum = carNum + 1;
        return false;
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
