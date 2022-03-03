package ga_simple_planning;

/**
 * @ClassName SImplePlanningApplication
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:36 下午
 * @Version 1.0
 **/
public class SimplePlanningApplication {
    public static void main(String[] args) {
        //Chromosome chromosome = new Chromosome(6);
        //System.out.println(chromosome);
        //System.out.println(chromosome.decoder());
        GeneticAlgorithm ga = new GeneticAlgorithm();
        System.out.println(ga.getPOP_SIZE());
    }
}
