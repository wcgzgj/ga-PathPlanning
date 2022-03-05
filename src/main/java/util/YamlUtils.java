package util;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName YamlUtils
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/5 3:56 下午
 * @Version 1.0
 **/
public class YamlUtils {
    private static Map<String, Map<String,Object>> properties;


    static {
        Yaml yaml = new Yaml();
        properties = yaml.loadAs(YamlUtils.class.getClassLoader().getResourceAsStream("planning_info.properties"),HashMap.class);
    }

    public static Object getValByKey(String key) {
        return properties.get(key);
    }

}
