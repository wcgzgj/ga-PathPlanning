package ga_simple_planning;

import com.sun.jdi.PrimitiveValue;
import util.PropertyUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName GeneticAlgorithm
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:35 下午
 * @Version 1.0
 **/
public class GeneticAlgorithm extends MaxFuncAdapter {
    // 种群
    private List<Chromosome> pop;
    Properties pro = PropertyUtil.getProperty("ga_simple");
    // 种群大小
    private int POP_SIZE = Integer.valueOf(pro.getProperty("POP_SIZE"));
    // 基因长短
    private int CHROMOSOME_SIZE = Integer.valueOf(pro.getProperty("CHROMOSOME_SIZE"));
    // 迭代次数
    private int ITER_NUM = Integer.valueOf(pro.getProperty("ITER_NUM"));
    // 个体变异概率
    private double MUTATION_RATE=Double.valueOf(pro.getProperty("MUTATION_RATE"));
    // 最大变异长度
    private int MAX_MUTATION_NUM=Integer.valueOf(pro.getProperty("MAX_MUTATION_NUM"));

    // 当前这代种群中的最佳适应度
    private double bestScore = Double.MIN_VALUE;
    // 当前这代种群中的最坏适应度
    private double worstScore = Double.MAX_VALUE;
    // 群体适应度得分（这里只限于当前群体）
    private double totalScore = 0;
    // 平均群体适应度（需要注意，因为精度问题，我们要确保平均得分不得超过最好得分）
    private double averageScore = 0;

    // 种群代数目统计
    private int popCount=0;

    private int bestX = 0;
    private int bestY = 0;


    public GeneticAlgorithm() {
        pop = new ArrayList<Chromosome>();
    }


    public void conductGA() {
        //init();
        mockBadPop(); // 初始化烂种群
        for (int i = 0; i < ITER_NUM ; i++) {
            // 计算种群适应度
            calculateScore();
            print();
            // 种群遗传
            // - 筛选较优父代（轮盘赌算法）
            // - 交叉运算
            evolve();
            // 种群变异
            mutation();
        }
    }

    private void print() {
        System.out.println("当前代数为："+popCount);
        //System.out.println("当前种群为：");
        //for (Chromosome chromosome : pop) {
        //    System.out.println(chromosome);
        //}
        System.out.println("最佳适应度为："+bestScore);
        System.out.println("最坏适应度为："+worstScore);
        System.out.println("总适应度为："+totalScore);
        System.out.println("平均适应度为："+averageScore);
        System.out.println("---------------------------------------");
    }

    /**
     * 初始化种群
     */
    private void init() {
        System.out.println("0、初始化种群");
        popCount++;
        for (int i = 0; i < POP_SIZE; i++) {
            pop.add(new Chromosome(CHROMOSOME_SIZE));
        }
    }

    /**
     * 生成较坏的父代
     */
    private void mockBadPop() {
        System.out.println("0、初始化坏种群");
        popCount++;
        for (int i = 0; i < POP_SIZE; i++) {
            Chromosome chromosome = new Chromosome(CHROMOSOME_SIZE);
            boolean[] gene = chromosome.getGene();
            gene[0]=false;
            gene[1]=false;
            pop.add(chromosome);
        }
    }

    /**
     * 计算种群适应度
     */
    private void calculateScore() {
        if (pop==null || pop.size()==0) {
            pop = new ArrayList<Chromosome>();
            init();
        }
        bestScore = Double.MIN_VALUE;
        worstScore = Double.MAX_VALUE;
        // 因为 totalScore 记录的是当前群体适应度总和
        // 所以每次计算适应度之前，都必须清空前一代群体的适应度总和
        totalScore = 0;
        for (Chromosome chromosome : pop) {
            chromosome.setScore(calculateChromosomeScore(chromosome));
            totalScore+= chromosome.getScore();
            bestScore = Math.max(bestScore, chromosome.getScore());
            worstScore = Math.min(worstScore,chromosome.getScore());
        }
        averageScore = totalScore/POP_SIZE;
        // 因为精度问题，要注意 averageScore 会不会大于 bestScore
        if (averageScore>bestScore) averageScore = bestScore;
    }

    /**
     * 计算个体适应度
     * @param chromosome 被计算个体
     * @return 个体适应度
     */
    private int calculateChromosomeScore(Chromosome chromosome) {
        int x = changeX(chromosome);
        // 借助位运算，获取 x1，x2 的值
        return changeY((x&56)>>3,x&7);
    }

    @Override
    int changeX(Chromosome chromosome) {
        if (chromosome!=null) return Integer.valueOf(chromosome.decoder());
        return 0;
    }

    @Override
    int changeY(int x1,int x2) {
        return x1*x1 + x2*x2;
    }

    /**
     * 生成新种群
     */
    private void evolve() {
        List<Chromosome> newPop = new ArrayList<>();
        while (newPop.size()<POP_SIZE) {
            Chromosome p1 = getParentChromosome();
            Chromosome p2 = getParentChromosome();
            if (p1==null || p2==null) continue;
            List<Chromosome> children = Chromosome.genetic(p1, p2);
            for (Chromosome child : children) {
                newPop.add(child);
            }
        }
        pop.clear();
        pop=newPop;
        popCount++;
    }

    /**
     * 使用轮盘赌法，获取相对较好的父类个体
     * 这里在选择个体的时候要注意：
     * 1、个体被选择的概率要遵循轮盘赌法
     * 2、个体的适应度要大于平均适应度
     * 轮盘赌法相关文章：https://blog.csdn.net/weixin_44062380/article/details/123255853
     * @return
     */
    private Chromosome getParentChromosome() {
        if (pop==null || pop.size()==0) return null;
        int iterCount = 0;
        while (true) {
            double slice = totalScore * Math.random();
            double sum = 0d;
            for (Chromosome chromosome : pop) {
                sum+=chromosome.getScore();
                // 一定要保证个体适应度大于平均适应度
                if (sum>slice && chromosome.getScore()>averageScore) {
                    return chromosome;
                }
            }
            iterCount++;
            // 防止迭代次数过高
            // 迭代次数超过阈值，返回种群最优个体
            if (iterCount>400) {
                return pop.stream()
                        .max(Comparator.comparing(Chromosome::getScore))
                        .get();
            }
        }
    }

    /**
     * 种群染色体发生变异
     */
    private void mutation() {
        for (Chromosome chromosome : pop) {
            if (Math.random()<MUTATION_RATE) {
                System.out.println("在" + popCount +"代发生了变异！");
                chromosome.mutation((int) (MAX_MUTATION_NUM*Math.random()));
            }
        }
    }



}
