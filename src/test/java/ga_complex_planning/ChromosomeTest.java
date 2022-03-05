package ga_complex_planning;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ChromosomeTest {

    @Test
    public void testInit() {
        List<Chromosome> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Chromosome(5,30));
        }
        for (Chromosome chromosome : list) {
            System.out.println(chromosome);
        }
    }

    @Test
    public void isGoodChromosome() {
        Chromosome chromosome = new Chromosome(2, 4);
        System.out.println(chromosome);
        System.out.println("没有问题的点、"+Chromosome.isGoodChromosome(chromosome));
        // 1、起始点紧邻 false
        chromosome.setGene(new int[]{0,0,1,2,3,4,0});
        System.out.println("1、"+Chromosome.isGoodChromosome(chromosome));
        // 2、起始点个数不对
        chromosome.setGene(new int[]{0,1,0,2,0,3,0});
        System.out.println("2、"+Chromosome.isGoodChromosome(chromosome));
        // 3、头尾没有起始点
        chromosome.setGene(new int[]{0,1,0,2,3,0,4});
        System.out.println("3、"+Chromosome.isGoodChromosome(chromosome));
        // 3、头尾没有起始点
        chromosome.setGene(new int[]{0,1,0,2,3,0,4});
        System.out.println("3、"+Chromosome.isGoodChromosome(chromosome));
        // 4、受灾点顺序不正确
        chromosome.setGene(new int[]{0,1,0,2,3,5,0});
        System.out.println("4、"+Chromosome.isGoodChromosome(chromosome));
        // 5、有重复的受灾点
        chromosome.setGene(new int[]{0,1,0,2,2,5,0});
        System.out.println("5、"+Chromosome.isGoodChromosome(chromosome));
        // 6、没有问题的点
        chromosome.setGene(new int[]{0,1,0,2,3,4,0});
        System.out.println("6、"+Chromosome.isGoodChromosome(chromosome));
    }

    @Test
    public void sortTest() {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(3);
        //list.sort((o1, o2) -> o1-o2);
        list.sort(null);
        System.out.println(list);
    }
}