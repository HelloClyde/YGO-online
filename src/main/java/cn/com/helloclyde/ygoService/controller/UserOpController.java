package cn.com.helloclyde.ygoService.controller;

import cn.com.helloclyde.ygoService.controller.requestVO.LoginRequestVO;
import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;
import cn.com.helloclyde.ygoService.service.UserOpService;
import cn.com.helloclyde.ygoService.vo.ResponseResult;
import cn.com.helloclyde.ygoService.vo.UserVO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by HelloClyde on 2017/3/4.
 */
@Controller
@RequestMapping(value = "/user-op")
public class UserOpController {

    @Autowired
    private UserOpService userOpService;

    @Autowired
    private HttpServletRequest request;

    @ResponseBody
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String login(@RequestBody LoginRequestVO loginResquestVO) {
        try {
            UserWithBLOBs userWithBLOBs = userOpService.getUser(loginResquestVO.getEmail(), loginResquestVO.getPassword());
            if (userWithBLOBs == null){
                throw new Exception("账号或者密码错误");
            }
            UserVO userVO = new UserVO(userWithBLOBs);
            this.request.getSession().setAttribute("loginedUser", userVO);
            return new Gson().toJson(new ResponseResult("success"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }


}
