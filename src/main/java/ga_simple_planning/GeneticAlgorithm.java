package ga_simple_planning;

import com.sun.jdi.PrimitiveValue;
import util.PropertyUtil;

import java.util.ArrayList;
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

    // 最佳适应度
    private double bestScore = Double.MIN_VALUE;
    // 最坏适应度
    private double worstScore = Double.MAX_VALUE;
    // 群体适应度得分（这里只限于当前群体）
    private double totalScore = 0;
    // 平均群体适应度（需要注意，因为精度问题，我们要确保平均得分不得超过最好得分）
    private double averageScore = 0;



    public GeneticAlgorithm() {
        pop = new ArrayList<Chromosome>();
        init();
    }

    private void init() {
        for (int i = 0; i < POP_SIZE; i++) {
            pop.add(new Chromosome(CHROMOSOME_SIZE));
        }
    }

    private void calculateScore() {
        if (pop==null || pop.size()==0) {
            pop = new ArrayList<Chromosome>();
            init();
        }
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

    private int calculateChromosomeScore(Chromosome chromosome) {
        int x = changeX(chromosome);
        // 借助位运算，获取 x1，x2 的值
        return changeY(x&56>>>3,x&7);
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
}
