package ga_complex_planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Codec
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/5 8:31 下午
 * @Version 1.0
 **/
public abstract class Codec {
    protected static Map<String,String> codeMap = new HashMap<>();

    /**
     * 对染色体基因进行解码
     * @param chromosome
     * @return
     */
    public static List<List<String>> decodeGene(Chromosome chromosome) {
        // 以下解码算法能正常运行的前提，是染色体符合预期
        if (!chromosome.isGoodChromosome(chromosome)) return null;
        List<List<String>> res = new ArrayList<>();
        int[] gene = chromosome.getGene();
        int resSize = chromosome.getCarNum();
        int l = 0;
        for (int i = 0; i < resSize; i++) {
            List<String> currRoute = new ArrayList<>();
            currRoute.add(codeMap.get(String.valueOf(gene[l++])));
            while (gene[l]!=0) {
                currRoute.add(codeMap.get(String.valueOf(gene[l])));
                l++;
            }
            // 每条路径中，都要包括收尾信息
            currRoute.add(codeMap.get("0"));
            res.add(currRoute);
        }
        return res;
    }
}
