package cn.com.helloclyde.ygoService.controller;

import cn.com.helloclyde.ygoService.controller.requestVO.LoginRequestVO;
import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;
import cn.com.helloclyde.ygoService.service.UserOpService;
import cn.com.helloclyde.ygoService.utils.Security;
import cn.com.helloclyde.ygoService.vo.Hall;
import cn.com.helloclyde.ygoService.vo.LoginedUser;
import cn.com.helloclyde.ygoService.vo.ResponseResult;
import cn.com.helloclyde.ygoService.vo.UserVO;
import com.google.gson.Gson;
import com.mysql.jdbc.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by HelloClyde on 2017/3/4.
 */
@Controller
@RequestMapping(value = "/user-op")
public class UserOpController {

    @Autowired
    private UserOpService userOpService;

    @ResponseBody
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String login(@RequestBody LoginRequestVO loginResquestVO) {
        try {
            UserWithBLOBs userWithBLOBs = userOpService.getUser(loginResquestVO.getEmail(), loginResquestVO.getPassword());
            if (userWithBLOBs == null) {
                throw new Exception("账号或者密码错误");
            }
            UserVO userVO = new UserVO(userWithBLOBs);
            String token = Security.md5(loginResquestVO.getEmail() + loginResquestVO.getPassword());
            // 处理已经登陆的用户
            UserVO loginedUser = (UserVO) LoginedUser.get(token);
            if (loginedUser != null){
                if (loginedUser.getRoomIdx() != -1){
                    Hall.exitRoom(loginedUser);
                }
            }
            LoginedUser.set(token, userVO);
            return new Gson().toJson(new ResponseResult(token));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }


}
