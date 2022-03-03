package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName propertyUtil
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/3 12:45 下午
 * @Version 1.0
 **/
public class PropertyUtil {

    public static Properties getProperty(String proName) {
        Properties pro = new Properties();
        InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(transProName(proName));
        try {
            pro.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }

    private static String transProName(String proName) {
        if (proName.endsWith(".properties")) return proName;
        return proName+".properties";
    }

}
