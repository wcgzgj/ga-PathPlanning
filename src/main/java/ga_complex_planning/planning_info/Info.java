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
        info.put("A",new Point("A",160d,180d,0.6d,0.18d,0d,3.4d));
        info.put("B",new Point("B",170d,50d,0.8d,0.48d,0d,7.1d));
        info.put("C",new Point("C",30d,120d,1.3d,0.78d,0d,5.6d));
        info.put("D",new Point("D",60d,90d,0.7d,1.08d,0d,3.2d));
        info.put("E",new Point("E",250d,30d,1.3d,1.38d,0d,10d));
    }

    public static Map<String, Point> getInfo() {
        return info;
    }
}
