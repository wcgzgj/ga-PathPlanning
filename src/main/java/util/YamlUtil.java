package util;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName YamlUtil
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/5 1:46 下午
 * @Version 1.0
 **/
public class YamlUtil {
    private String ymlName;
    private Yaml yaml = new Yaml();
    private Map<String, Map<String, Object>> properties = new HashMap<>();


    public YamlUtil(String ymlName) {
        this.ymlName = ymlName;
        transYamlName();
        initYaml();
    }

    /**
     * 为 ymlName 添加后缀
     */
    private void transYamlName() {
        if (ymlName.endsWith(".yml") || ymlName.endsWith(".yaml")) return;
        ymlName+=".yml";
    }

    /**
     * 初始化 yaml 对象
     */
    private void initYaml() {
        yaml.loadAs(Yaml.class.getClassLoader().getResourceAsStream(ymlName), HashMap.class);
    }

    public Map<String,Object> getByKey(String key) {
        return null;
    }

    public Map<String,Object> getByKeys(String keys) {
        if (this.yaml==null) return null;
        String[] keySeq = keys.split("\\.");
        return null;
    }
}
