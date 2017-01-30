package UserTest;

import cn.com.helloclyde.ygoOnline.service.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by HelloClyde on 2017/1/30.
 */
public class UserService {
    @Test
    public void TestEmailCheck(){
        User userService = new User();
        Assert.assertTrue(userService.checkEmail("helloclyde@qq.com"));
    }

}
