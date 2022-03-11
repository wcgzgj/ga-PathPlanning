package util;

import ga_complex_planning.Chromosome;

import java.util.*;

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
     * 绘制当前种群适应度函数分布
     * @param pop
     */
    public static void drawCurrentPopScoreDistributeGraph(List<Chromosome> pop,int iterNum) {
        List<Double> list = new ArrayList<>();
        for (Chromosome chrom : pop) {
            list.add(chrom.getScore());
        }
        Map<Double, Double> map = new HashMap<>();
        Double randomData = list.get(0);
        System.out.println("randomData:"+randomData);
        int mod = 1;
        while (randomData>=10) {
            mod*=10;
            randomData/=10;
        }
        for (Double elem : list) {
            int key = (int) (elem / mod);
            System.out.println("key:"+key);
            Double origin = map.get(key);
            if (origin==null) origin=0d;
            origin++;
            map.put((double) key,origin);
        }
        System.out.println("Map值为"+map);
        System.out.println("mod:"+mod);
        Map[] dataSet = new Map[]{map};
        String[] types = {"种群适应度分数分布"};
        DrawingTools.drawLineChart("当前代数："+iterNum,"种群适应度分数分布"+iterNum,"分数范围","出现次数",dataSet,types);
    }

    /**
     * 阻塞进程，避免绘制的图像一闪而过
     */
    public static void blockUtil() {
        Scanner sc = new Scanner(System.in);
        sc.next();
    }
}
