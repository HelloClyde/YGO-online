import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;
import cn.com.helloclyde.ygoService.service.UserOpService;
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
}
