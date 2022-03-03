package ga_simple_planning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    public static List<Chromosome> genetic(final Chromosome parent1,final Chromosome parent2) {
        List<Chromosome> children = new ArrayList<>();
        if (parent1==null || parent2==null ||
                parent1.gene.length!=parent2.gene.length) {
            return children;
        }
        int geneSize = parent1.geneSize;
        Random random = new Random();
        // 获取随机父代位交换位置
        int l = random.nextInt(geneSize);
        int r = random.nextInt(geneSize);
        if (l>r) {
            int tmp = l;
            l=r;
            r=tmp;
        }
        // 这里最好使用深拷贝
        Chromosome child1 = clone(parent1);
        Chromosome child2 = clone(parent2);
        boolean[] gene1 = child1.getGene();
        boolean[] gene2 = child2.getGene();
        for (int i = l; i <=r ; i++) {
            gene1[i]=!gene1[i];
            gene2[i]=!gene2[i];
        }
        children.add(child1);
        children.add(child2);
        return children;
    }

    /**
     * 基因变异
     * @param mutationNum 变异点个数
     */
    public void mutation(int mutationNum) {
        for (int i = 0; i < mutationNum; i++) {
            int at =(int) Math.random() * geneSize;
            gene[at] = !gene[at];
        }
    }

    /**
     * 深拷贝当前染色体
     * @param chromosome
     * @return
     */
    public static Chromosome clone(final Chromosome chromosome) {
        if (chromosome==null || chromosome.gene==null) return null;
        Chromosome copy = new Chromosome(chromosome.getGeneSize());
        boolean[] geneCopy = new boolean[chromosome.getGeneSize()];
        for (int i = 0; i < chromosome.getGeneSize(); i++) {
            geneCopy[i]=chromosome.getGene()[i];
        }
        copy.setGene(geneCopy);
        return copy;
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

    public boolean[] getGene() {
        return gene;
    }

    public void setGene(boolean[] gene) {
        this.gene = gene;
    }

    public int getGeneSize() {
        return geneSize;
    }

    public void setGeneSize(int geneSize) {
        this.geneSize = geneSize;
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
