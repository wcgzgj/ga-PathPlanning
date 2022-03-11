package util;

import org.junit.Test;

/**
 * @ClassName RandomDataTest
 * @Description TODO
 * @Author faro_z
 * @Date 2022/3/11 11:52 上午
 * @Version 1.0
 **/
public class RandomDataTest {
    @Test
    public void randomTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Math.random());
        }
    }
}
