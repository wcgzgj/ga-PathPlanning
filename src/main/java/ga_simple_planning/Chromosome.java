package ga_simple_planning;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName Chromosome
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:35 下午
 * @Version 1.0
 **/
public class Chromosome implements Codec {
    private boolean[] gene;
    private int geneSize;
    private double score;

    public Chromosome(int size) {
        if (size<0) size=0;
        geneSize=size;
        gene = new boolean[geneSize];
        init();
    }

    /**
     * 初始化基因序列
     */
    private void init() {
        for (int i = 0; i < geneSize; i++) {
            if (Math.random()<0.5d) gene[i]=true;
        }
    }

    /**
     * 解码器
     * 这里是将基因序列解码为数字
     * @return
     */
    @Override
    public String decoder() {
        if (gene==null || gene.length==0) return "0";
        int sum = 0;
        for (int i = 0; i < gene.length; i++) {
            sum = sum<<1;
            if (gene[i]) sum+=1;
        }
        return String.valueOf(sum);
    }

    /**
     * 两个父代进行交叉运算，获取新的两个子代
     * @param parent1
     * @param parent2
     * @return
     */
    public static List<Chromosome> genetic(Chromosome parent1, Chromosome parent2) {
        //if (parent1==null || parent2==null)
        return null;
    }

    /**
     * 获取基因可读序列
     * @return
     */
    private String getGeneSeq() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < geneSize; i++) {
            if (gene[i]) builder.append(1);
            else builder.append(0);
        }
        return builder.toString();
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        return builder.append("gene:")
                .append(getGeneSeq())
                .append("\n")
                .append("secore:")
                .append(score)
                .append("\n")
                .toString();
    }
}
