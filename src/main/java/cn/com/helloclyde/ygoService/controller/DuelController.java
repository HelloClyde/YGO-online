package cn.com.helloclyde.ygoService.controller;

import cn.com.helloclyde.ygoService.service.DuelService;
import cn.com.helloclyde.ygoService.service.UserOpService;
import cn.com.helloclyde.ygoService.vo.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by HelloClyde on 2017/3/5.
 */
@Controller
@RequestMapping(value = "/duel-controller")
public class DuelController {

    @Autowired
    private DuelService duelService;

    @Autowired
    private UserOpService userOpService;

    // 获取游戏全部日志
    @ResponseBody
    @RequestMapping(value = "/get-all-log", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String getDuelLog(@RequestParam String token) {
        try {
            UserVO userVO = (UserVO) LoginedUser.get(token);
            if (userVO == null) {
                throw new Exception("未登录");
            }
            if (userVO.getRoomIdx() == -1) {
                throw new Exception("未进房间");
            }
            return new Gson().toJson(new ResponseResult(Hall.getRooms().get(userVO.getRoomIdx()).getDuelLogItems()));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }

    // 获取最新游戏日志
    @ResponseBody
    @RequestMapping(value = "/get-inc-log", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String getIncrementDuelLog(@RequestParam String token) {
        try {
            UserVO userVO = (UserVO) LoginedUser.get(token);
            if (userVO == null) {
                throw new Exception("未登录");
            }
            if (userVO.getRoomIdx() == -1) {
                throw new Exception("未进房间");
            }
            Room room = Hall.getRooms().get(userVO.getRoomIdx());
            int userIdx = room.getPlayers().indexOf(userVO);
            if (room.getLogPos().get(userIdx) >= room.getDuelLogItems().size()) {
                return new Gson().toJson(new ResponseResult(null));
            }
            DuelLogItem duelLogItem = room.getDuelLogItems().get(room.getLogPos().get(userIdx));
            room.getLogPos().set(userIdx, room.getLogPos().get(userIdx) + 1);
            return new Gson().toJson(new ResponseResult(duelLogItem));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/action", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public String action(@RequestBody Map<String, String> params) {
        try {
            String token = params.remove("token");
            UserVO userVO = (UserVO) LoginedUser.get(token);
            if (userVO == null) {
                throw new Exception("未登录");
            }
            if (userVO.getRoomIdx() == -1) {
                throw new Exception("未进房间");
            }
            String action = params.remove("action");
            duelService.doClientAction(userVO, action, params);
            return new Gson().toJson(new ResponseResult("success"));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }


    @ResponseBody
    @RequestMapping(value = "/get-debug", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String getDebug() {
        return new Gson().toJson(new ResponseResult(Hall.getRooms()));
    }


    @ResponseBody
    @RequestMapping(value = "/draw-card", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String drawCard(@RequestParam String token) {
        try{
            UserVO loginedUser = (UserVO) LoginedUser.get(token);
            if (loginedUser == null) {
                throw new Exception("未登陆");
            }
            return new Gson().toJson(new ResponseResult(this.userOpService.drawCard(loginedUser.getUser())));
        }catch (Exception e){
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }
}
