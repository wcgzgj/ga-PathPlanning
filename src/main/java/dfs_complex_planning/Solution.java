package dfs_complex_planning;

import ga_complex_planning.planning_info.Info;
import ga_complex_planning.pojo.Point;

import java.util.Map;

/**
 * @ClassName Solution
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/31 1:48 下午
 * @Version 1.0
 **/
public class Solution {
    private Map<String, Point> info = Info.getInfo();
    private static int count = 0;

    public void conductBF() {
        long startTime = System.currentTimeMillis();
        dfs(10);
        long endTime = System.currentTimeMillis();
        long secondsUsed = (endTime - startTime) / 1000 / 1000;
        System.out.println("共进行了"+count+"次运算");
    }

    private void dfs(int currDepth) {
        if (currDepth==0) return;
        //try {
        //    // 假设每次适应度计算都需要 0.1 秒
        //    Thread.sleep(100);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        count++;
        for (int i = 0; i < currDepth; i++) {
            dfs(currDepth-1);
        }
    }
}
