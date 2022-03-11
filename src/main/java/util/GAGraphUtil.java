package util;

import java.util.Map;
import java.util.Scanner;

/**
 * @ClassName GAGraphUtil
 * @Description 遗传算法相关数据图标生成工具
 * @Author faro_z
 * @Date 2022/3/11 10:51 上午
 * @Version 1.0
 **/
public class GAGraphUtil {

    /**
     * 绘制总适应度函数变化折线图
     * @param dataSet
     */
    public static void drawTotalScoreGraph(Map[] dataSet) {
        String[] types = {"总适应度"};
        DrawingTools.drawLineChart("总适应度变化","总适应度变化","迭代次数","适应度得分",dataSet,types);
    }


    /**
     * 绘制最优/最差 适应度变化折线图
     * @param dataSet 绘制图标使用的数据集
     */
    public static void drawBestWorstScoreGraph(Map[] dataSet) {
        String[] types = {"最佳适应度", "最坏适应度"};
        DrawingTools.drawLineChart("最佳/最坏适应度变化","最佳/最坏适应度变化","迭代次数","适应度得分",dataSet,types);
    }

    /**
     * 阻塞进程，避免绘制的图像一闪而过
     */
    public static void blockUtil() {
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
