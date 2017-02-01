package cn.com.helloclyde.ygoOnline.service;

import cn.com.helloclyde.ygoOnline.mapper.user.model.UserExample;
import cn.com.helloclyde.ygoOnline.mapper.user.model.UserWithBLOBs;
import cn.com.helloclyde.ygoOnline.mapper.user.persistence.UserMapper;
import com.jcabi.aspects.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HelloClyde on 2016/12/11.
 */
@Service("userService")
public class User {
    @Autowired
    private UserMapper userMapper;
    /**
     * 检查邮件格式
     * @param email
     * @return
     */
    public boolean checkEmail(String email) {
        return email != null && !email.equals("") && email.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
    }

    /**
     * 检查邮件是否已经被注册
     * @param email
     * @return
     */
    public boolean emailIsExisted(String email){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<cn.com.helloclyde.ygoOnline.mapper.user.model.User> users = userMapper.selectByExample(userExample);
        return users.size() != 0;
    }

    /**
     * 检查密码是否符合要求
     * 密码长度大于等于6，并且密码至少有一个字母
     * @param password
     * @return
     */
    public boolean checkPassword(String password) {
        return password.length() >= 6 && password.toLowerCase().matches(".*[a-z].*");
    }

    /**
     * 添加用户
     * @param name
     * @param email
     * @param password
     * @return
     */
    @Loggable(Loggable.INFO)
    public boolean registerUser(String name,String email,String password){
        UserWithBLOBs userWithBLOBs = new UserWithBLOBs();
        userWithBLOBs.setEmail(email);
        userWithBLOBs.setNickname(name);
        userWithBLOBs.setPassword(password);
        userMapper.insert(userWithBLOBs);
        return true;
    }
}
