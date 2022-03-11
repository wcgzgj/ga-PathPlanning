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
        info.put("A",new Point("A",160d,180d,5.6d,5.18d,0d,3.4d,7));
        info.put("B",new Point("B",170d,50d,9.8d,1.48d,0d,7.1d,190));
        info.put("C",new Point("C",30d,120d,1.3d,3.78d,0d,5.6d,2));
        info.put("D",new Point("D",60d,90d,4.7d,1.08d,0d,3.2d,41));
        info.put("E",new Point("E",250d,30d,8.3d,3.38d,0d,10d,50));
        info.put("F",new Point("F",190d,110d,1.4d,3.68d,0d,6.2d,23));
        info.put("G",new Point("G",80d,130d,4.7d,1.98d,0d,3.8d,4));
        info.put("H",new Point("H",140d,50d,2.8d,2.28d,0d,4.7d,420));
        info.put("I",new Point("I",30d,20d,4.6d,2.58d,0d,7d,10));
        info.put("J",new Point("J",10d,70d,2.6d,2.88d,0d,6d,1));
        info.put("K",new Point("K",70d,120d,2.6d,3.44d,0d,4d,100));
        info.put("L",new Point("L",90d,170d,4.6d,5.88d,0d,9d,1));
        info.put("M",new Point("M",30d,180d,2.6d,3.24d,0d,3d,1));
        info.put("N",new Point("N",20d,150d,6.6d,1.89d,0d,3d,9));
        info.put("O",new Point("O",90d,140d,2.6d,5.88d,0d,5.3d,122));
        info.put("P",new Point("P",160d,40d,7.6d,4.88d,0d,9d,39));


        info.put("Q",new Point("Q",190d,80d,2.6d,2.97d,0d,2d,210));
        info.put("R",new Point("R",140d,150d,5.6d,3.65d,0d,4d,33));
        info.put("S",new Point("S",70d,50d,8.6d,1.97d,0d,5d,23));
        info.put("T",new Point("T",200d,50d,6.6d,1.48d,0d,1d,723));
        info.put("U",new Point("U",220d,90d,2.6d,4.88d,0d,2d,34));
        info.put("V",new Point("V",40d,60d,7.6d,1.88d,0d,2d,782));
        info.put("W",new Point("W",90d,110d,9.6d,0.88d,0d,4d,123));
        info.put("X",new Point("X",0d,80d,8.6d,2.89d,0d,2d,73));
        info.put("Y",new Point("Y",10d,120d,2.6d,1.49d,0d,6d,35));
        info.put("Z",new Point("Z",40d,150d,9.6d,4.81d,0d,9d,92));
    }

    public static Map<String, Point> getInfo() {
        return info;
    }
}
