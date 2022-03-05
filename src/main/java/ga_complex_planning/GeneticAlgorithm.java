package ga_complex_planning;

import ga_complex_planning.planning_info.Info;
import ga_complex_planning.pojo.Point;
import util.PropertyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName GeneticAlgorithm
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/2 9:35 下午
 * @Version 1.0
 **/
public class GeneticAlgorithm {
    private Properties gaComplexPro = PropertyUtil.getProperty("ga_complex");
    private Properties planningInfoPro = PropertyUtil.getProperty("planning_info");

    // 受灾地信息
    private Map<String, Point> info = Info.getInfo();

    // 车辆数目
    private int CAR_NUM = Integer.valueOf(planningInfoPro.getProperty("CAR_NUM"));
    // 车辆载重
    private int CAR_CAPACITY = Integer.valueOf(planningInfoPro.getProperty("CAR_CAPACITY"));
    // 车辆速度
    private int CAR_SPEED = Integer.valueOf(planningInfoPro.getProperty("CAR_SPEED"));
    // 受灾点个数
    private int POINT_NUM = info.size();

    // 种群
    private List<Chromosome> pop = new ArrayList<>();
    // 种群大小
    private int POP_SIZE = Integer.valueOf(gaComplexPro.getProperty("POP_SIZE"));
    // 迭代次数
    int ITER_NUM = Integer.valueOf(gaComplexPro.getProperty("ITER_NUM"));

    // 基因长短
    private int geneSize = calculateGeneSize();
    // 当前种群最佳适应度
    private double bestScore = Double.MIN_VALUE;
    // 当前种群最坏适应度
    private double worstScore = Double.MAX_VALUE;
    // 当前种群总适应度
    private double totalScore = 0;
    // 当前种群平均适应度
    private double averageScore = 0;
    // 总迭代中的最佳基因
    private int[] bestGene = null;
    // 总迭代中的最坏基因
    private int[] worstGene = null;

    /**
     * 执行遗传算法函数
     */
    public void conductGA() {
        for (int i = 0; i < ITER_NUM; i++) {

        }
    }

    /**
     * 计算个体适应度
     * 需要考虑：
     * 1、单个车体载重能否满足所有受灾点
     * 2、单个车体到达时间是否在时间窗之内
     * @param chromosome
     */
    private void calculateScore(Chromosome chromosome) {

    }

    /**
     * 计算基因长短
     * 因为路径规划问题中，基因长短和车辆个数和受灾点有关系
     * 所以需要额外开函数计算
     * @return
     */
    private int calculateGeneSize() {
        return CAR_NUM+POINT_NUM+1;
    }
}
