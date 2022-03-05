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
}