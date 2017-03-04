import com.google.gson.Gson;
import org.junit.Test;

import java.util.*;
import java.util.Random;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class TestRnd {
    @Test
    public void random() {
        System.out.println(new Random().nextInt(100));
    }

    @Test
    public void json() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@qq.com");
        map.put("password", "123456a");
        System.out.println(new Gson().toJson(map));
    }
}
