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
        info.put("start",new Point("start",100d,100d,0d,0d,0d,Double.MAX_VALUE,0)); // 起始点信息
        info.put("A",new Point("A",160d,180d,0.6d,0.18d,0d,3.4d,7));
        info.put("B",new Point("B",170d,50d,0.8d,0.48d,0d,7.1d,9));
        info.put("C",new Point("C",30d,120d,1.3d,0.78d,0d,5.6d,2));
        info.put("D",new Point("D",60d,90d,0.7d,1.08d,0d,3.2d,4));
        info.put("E",new Point("E",250d,30d,1.3d,1.38d,0d,10d,1));
        info.put("F",new Point("F",190d,110d,1.4d,1.68d,0d,6.2d,3));
        info.put("G",new Point("G",80d,130d,1.7d,1.98d,0d,3.8d,4));
        info.put("H",new Point("H",140d,50d,2.8d,2.28d,0d,4.7d,2));
        info.put("I",new Point("I",30d,20d,2.6d,2.58d,0d,7d,1));
        info.put("J",new Point("J",10d,70d,2.6d,2.88d,0d,6d,1));
    }

    public static Map<String, Point> getInfo() {
        return info;
    }
}
