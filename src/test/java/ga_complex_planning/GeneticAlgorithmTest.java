package ga_complex_planning;

import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeneticAlgorithmTest {

    private static final GeneticAlgorithm ga = new GeneticAlgorithm();

    @Test
    public void decodeGene() {
        Chromosome chromosome = new Chromosome(2, 4);
        chromosome.setGene(new int[]{0,1,2,0,3,4,0});
        //chromosome.setGene(new int[]{0,1,0,2,2,5,0});
        System.out.println(GeneticAlgorithm.decodeGene(chromosome));
    }

    /**
     * 测试个体适应度计算函数
     */
    @Test
    public void calculateScore() {
        //Chromosome chromosome = new Chromosome(2, 5);
        //chromosome.setGene(new int[]{0,5,3,0,4,1,2,0});
        //ga.calculateScore(chromosome);
        //System.out.println(chromosome.getScore());
        //
        //chromosome.setGene(new int[]{0,2,1,4,0,3,5,0});
        //ga.calculateScore(chromosome);
        //System.out.println(chromosome.getScore());
    }

    @Test
    public void pairTest() {
        Pair<Integer, Integer> pair = new Pair<>(1, 2);
        Integer fst = pair.fst;
        Integer snd = pair.snd;
        System.out.println(pair);
    }

}