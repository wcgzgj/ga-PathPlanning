package ga_simple_planning;

/**
 * @ClassName MaxFuncAdapter
 * @Description 用来
 * @Author faro_z
 * @Date 2022/3/3 1:35 下午
 * @Version 1.0
 **/
public abstract class MaxFuncAdapter {
    /**
     * 计算解码后的 x1x2序列（此时两个变量还未分离）
     * @return
     */
    abstract int changeX(Chromosome chromosome);

    /**
     * 计算方程结果
     * @return
     */
    abstract int changeY(int x1,int x2);
}
