package cn.com.helloclyde.ygoOnline.controller;

import cn.com.helloclyde.ygoOnline.vo.ResponseResult;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by HelloClyde on 2016/12/11.
 */
@Controller
@RequestMapping(value = "/user")
public class User {

    @Autowired
    cn.com.helloclyde.ygoOnline.service.User userService;

    /**
     * 注册用户
     *
     * @param name
     * @param email
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String register(@RequestParam(value = "name") String name, @RequestParam(value = "email") String email,
                           @RequestParam(value = "password") String password) {
        try {
            if (!userService.checkEmail(email)) {
                throw new Exception("邮箱格式不正确");
            }
            if (userService.emailIsExisted(email)) {
                throw new Exception("邮箱已经被注册过");
            }
            if (!userService.checkPassword(password)) {
                throw new Exception("密码不符合要求");
            }
            if (name.equals("")) {
                throw new Exception("昵称不能为空");
            }
            userService.registerUser(name, email, password);
            return new Gson().toJson(new ResponseResult(null));
        } catch (Exception e) {
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }

}
