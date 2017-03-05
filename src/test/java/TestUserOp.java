import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;
import cn.com.helloclyde.ygoService.service.UserOpService;
import cn.com.helloclyde.ygoService.utils.Security;
import com.google.gson.Gson;
import org.junit.Test;

/**
 * Created by HelloClyde on 2017/3/4.
 */
public class TestUserOp {
    @Test
    public void login() {
        UserOpService userOpService = new UserOpService();
        UserWithBLOBs userWithBLOBs = userOpService.getUser("test@qq.com", "123456a");
        System.out.println(new Gson().toJson(userWithBLOBs));
    }

    @Test
    public void calToken(){
        System.out.println(Security.md5("test@qq.com123456a"));
        // a089c70ba702efcbb6214a79c840283c
    }
}
