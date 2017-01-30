package cn.com.helloclyde.ygoOnline.service;

import org.springframework.stereotype.Service;

/**
 * Created by HelloClyde on 2016/12/11.
 */
@Service
public class User {
    /**
     * 检查邮件格式
     * @param email
     * @return
     */
    public boolean checkEmail(String email){
        if (email == null)
            return false;
        if (email.equals(""))
            return false;
        if (email.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$"))
            return true;
        return false;
    }

    /**
     * 检查邮件是否已经被注册
     * @param email
     * @return
     */
    public boolean emailIsExisted(String email){
        return true;
    }

    /**
     * 检查密码是否符合要求
     * @param password
     * @return
     */
    public boolean checkPassword(String password){
        if (password.length() < 6){
            return false;
        }
        if (!password.toLowerCase().matches(".*[a-z].*")){
            return false;
        }
        return true;
    }

    /**
     * 添加用户
     * @param name
     * @param email
     * @param password
     * @return
     */
    public boolean registerUser(String name,String email,String password){
        return true;
    }
}
