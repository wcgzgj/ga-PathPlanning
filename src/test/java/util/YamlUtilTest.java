package util;

import ga_complex_planning.pojo.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class YamlUtilTest {

    @Test
    public void testGetByKeys() {
        //System.out.println(YamlUtils.getValByKey("car_speed"));
        Object obj = YamlUtils.getValByKey("k1");
        System.out.println(obj instanceof Object);
        System.out.println(obj);
    }

    @Test
    public void listTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        System.out.println(list.size());
        System.out.println(list);
    }
}