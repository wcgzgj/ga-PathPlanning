package ga_complex_planning.planning_info;

import ga_complex_planning.pojo.Point;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Info
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/5 4:16 下午
 * @Version 1.0
 **/
public class Info {
    private static Map<String, Point> info = new HashMap<>();
    static {
        info.put("A",new Point("A",100d,100d,1.5d,0.78d,0d,7.1d));
        info.put("B",new Point("B",100d,100d,1.5d,0.78d,0d,7.1d));
        info.put("C",new Point("C",100d,100d,1.5d,0.78d,0d,7.1d));
        info.put("D",new Point("D",100d,100d,1.5d,0.78d,0d,7.1d));
        info.put("E",new Point("E",100d,100d,1.5d,0.78d,0d,7.1d));
    }

    public static Map<String, Point> getInfo() {
        return info;
    }
}
