package ga_complex_planning;

import com.sun.tools.javac.util.Pair;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

public class ChromosomeTest {

    @Test
    public void testInit() {
        List<Chromosome> list = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            list.add(new Chromosome(5,30));
        }
        for (Chromosome chromosome : list) {
            System.out.println(chromosome);
            System.out.println(Chromosome.isGoodChromosome(chromosome));
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

    @Test
    public void asciiTest() {
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= 26; i++) {
            map.put(String.valueOf(i),String.valueOf((char) (64+i)));
        }
        for (String key : map.keySet()) {
            System.out.println(map.get(key));
        }
    }

    @Test
    public void cloneTest() {
        Chromosome c1 = new Chromosome(2, 10);
        System.out.println(c1);
        System.out.println(c1.clone());
    }


    @Test
    public void getPossibleGeneticPair() {
        Set<Integer> set = new HashSet<>();
        set.add(0);
        Chromosome chromosome1 = new Chromosome(2, 5);
        chromosome1.setGene(new int[]{0,1,2,3,0,4,5,0});

        Chromosome chromosome2 = new Chromosome(2, 5);
        chromosome2.setGene(new int[]{0,1,0,3,2,4,5,0});

        List<Pair<Integer, Integer>> list = Chromosome.getPossibleGeneticPair(chromosome1, chromosome2, set);
        System.out.println(list);
        // [Pair[1,1], Pair[1,4], Pair[1,5], Pair[1,6], Pair[2,4],
        // Pair[2,5], Pair[2,6], Pair[3,3], Pair[5,5], Pair[5,6], Pair[6,6]]
    }

    @Test
    public void geneticTest() {
        Chromosome chromosome1 = new Chromosome(2, 5);
        chromosome1.setGene(new int[]{0,1,2,3,0,4,5,0});

        Chromosome chromosome2 = new Chromosome(2, 5);
        chromosome2.setGene(new int[]{0,1,0,3,2,4,5,0});

        List<Chromosome> children = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<Chromosome> tmp = Chromosome.genetic(chromosome1, chromosome2);
            for (Chromosome chromosome : tmp) {
                children.add(chromosome);
            }
        }
        int errorCount = 0;
        for (Chromosome child : children) {
            System.out.println(child);
            if (!Chromosome.isGoodChromosome(child)) {
                errorCount++;
                System.out.println("出现不合适的染色体！！！！！");
            }
        }
        System.out.println("出现不合适染色体概率为："+errorCount+"/100");
    }

    @Test
    public void mutation() {
        Chromosome chromosome1 = new Chromosome(2, 5);
        chromosome1.setGene(new int[]{0,1,2,3,0,4,5,0});
        List<Chromosome> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Chromosome dummy = chromosome1.clone();
            dummy.mutation(dummy.getGeneSize()/2);
            list.add(dummy);
        }
        int errCount = 0;
        for (Chromosome chromosome : list) {
            System.out.println(chromosome);
            if (!Chromosome.isGoodChromosome(chromosome)) {
                errCount++;
                System.out.println("出现变异异常的个体！！！");
            }
        }
        System.out.println("编译失败概率为："+(double)errCount/100 +"%");
    }
}