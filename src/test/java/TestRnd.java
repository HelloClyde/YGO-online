import org.junit.Test;

import java.util.Random;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class TestRnd {
    @Test
    public void random(){
        System.out.println(new Random().nextInt(100));
    }
}
