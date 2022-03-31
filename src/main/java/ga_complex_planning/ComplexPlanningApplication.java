package ga_complex_planning;


/**
 * @ClassName ComplexPlanningApplication
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:36 下午
 * @Version 1.0
 **/
public class ComplexPlanningApplication {
    public static void main(String[] args) {
        //Map<String, Point> info = Info.getInfo();
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.conductGA();

    }
}
