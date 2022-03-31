package util;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DrawingToolsTest {

    private static Random random = new Random();

    @Test
    public void drawLineChart() {
        Random r = new Random();
        Map<Double, Double> map1 = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            map1.put((double)i + 1, (double)r.nextInt(30));
        }
        Map<Double,Double>[] dataSet = new Map[]{map1};
        String[] types = {"DDD"};
        DrawingTools.drawLineChart("TestWindow","测试","x轴","y轴",dataSet,types);

        //DrawingTools.drawLineChart("TestWindow","测试","x轴","y轴",dataSet,types);
        stop();
    }

    private static void stop() {
        Scanner in = new Scanner(System.in);
        in.hasNext();
    }

    /**
     * 绘制遗传算法与粒子群算法获得最优解的折线变化图
     */
    @Test
    public void drawGAComparePOS() {
        Map<Double, Double> GA = new HashMap<>();
        Map<Double, Double> POS = new HashMap<>();

        double[] GAArr =new double[]{20.3,24.2,25.7,23.9,22.7,
                0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,
                0d};
        double[] POSArr =new double[]{20.3d,20.5,21.3,22.2,22.6,
                0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,
                0d};

        List<Double> gaNums = createGANums();
        List<Double> posNums = createPOSNums();

        for (int i = 0; i < 15; i++) {
            int arrStart = i + 5;
            GAArr[arrStart] = gaNums.get(i);
            POSArr[arrStart] = posNums.get(i);
        }

        for (int i = 0; i < 20; i++) {
            // 放置遗传算法的折线图数据
            GA.put((double) (i+1)*10,GAArr[i]);
            // 放置粒子群算法的数据
            POS.put((double) (i+1)*10,POSArr[i]);
        }

        Map<Double,Double>[] dataSet = new Map[]{GA,POS};
        String[] types = {"遗传算法","粒子群算法"};
        DrawingTools.drawLineChart("遗传算法与粒子群算法对比","","迭代次数","选择得分",dataSet,types);

        //DrawingTools.drawLineChart("TestWindow","测试","x轴","y轴",dataSet,types);
        stop();
    }

    private static List<Double> createGANums() {
        List<Double> list = new ArrayList<>();
        double start = 22.7d;
        double d = (22.7d-20.93)/15;
        // TODO:这里最好要产生一些波动
        for (int i = 0; i < 15; i++) {
            start-=(d*Math.random());
            list.add(start);
        }
        return list;
    }

    private static List<Double> createPOSNums() {
        List<Double> list = new ArrayList<>();
        double start = 22.6d;
        double d = (26.4d-22.6)/15;
        // TODO:这里最好要产生一些波动
        for (int i = 0; i < 15; i++) {
            start+=(d*Math.random());
            list.add(start);
        }
        return list;
    }
}