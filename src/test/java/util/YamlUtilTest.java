package util;

import ga_complex_planning.pojo.Point;
import org.junit.Test;

public class YamlUtilTest {

    @Test
    public void testGetByKeys() {
        //System.out.println(YamlUtils.getValByKey("car_speed"));
        Object obj = YamlUtils.getValByKey("k1");
        System.out.println(obj instanceof Object);
        System.out.println(obj);
    }
}