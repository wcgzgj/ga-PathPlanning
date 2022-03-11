package util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static org.junit.Assert.*;

public class DrawingToolsTest {

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

        DrawingTools.drawLineChart("TestWindow","测试","x轴","y轴",dataSet,types);
        stop();
    }

    private static void stop() {
        Scanner in = new Scanner(System.in);
        in.hasNext();
    }
}