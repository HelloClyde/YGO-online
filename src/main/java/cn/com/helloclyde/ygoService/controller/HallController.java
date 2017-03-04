package cn.com.helloclyde.ygoService.controller;

import cn.com.helloclyde.ygoService.vo.Hall;
import cn.com.helloclyde.ygoService.vo.ResponseResult;
import cn.com.helloclyde.ygoService.vo.Room;
import cn.com.helloclyde.ygoService.vo.UserVO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
@Controller
@RequestMapping(value = "/hall")
public class HallController {
    @Autowired
    private HttpServletRequest request;

    @ResponseBody
    @RequestMapping(value = "/lists", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String lists() {
        try {
            List<RoomInfo> roomInfos = new ArrayList<>();
            List<Room> rooms = Hall.lists();
            for (Room room : rooms) {
                RoomInfo tempRoomInfo = new RoomInfo();
                tempRoomInfo.setPlayerNum(room.getPlayers().size());
                roomInfos.add(tempRoomInfo);
            }
            return new Gson().toJson(new ResponseResult(roomInfos));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/join-room", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String joinRoom(int roomIdx) {
        try {
            UserVO userVO = (UserVO) this.request.getSession().getAttribute("loginedUser");
            if (userVO == null) {
                throw new Exception("未登录");
            }
            Hall.joinRoom(userVO, roomIdx);
            return new Gson().toJson(new ResponseResult(roomIdx));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }
}

class RoomInfo {
    private int playerNum;

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
}