package cn.com.helloclyde.ygoService.controller;

import cn.com.helloclyde.ygoService.service.HallService;
import cn.com.helloclyde.ygoService.vo.*;
import cn.com.helloclyde.ygoService.service.DuelService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
@Controller
@RequestMapping(value = "/hall")
public class HallController {
    @Autowired
    DuelService duelService;
    @Autowired
    HallService hallService;

    @ResponseBody
    @RequestMapping(value = "/lists", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String lists() {
        try {
            List<RoomInfo> roomInfos = new ArrayList<>();
            List<Room> rooms = Hall.getRooms();
            for (Room room : rooms) {
                RoomInfo tempRoomInfo = new RoomInfo();
                List<PlayerInfo> playerInfos = new ArrayList<>();
                for (UserVO userVO:room.getPlayers()){
                    PlayerInfo playerInfo = new PlayerInfo();
                    playerInfo.setUserName(userVO.getUser().getEmail());
                    playerInfo.setWinGame(userVO.getUser().getWinGame());
                    playerInfo.setAllGame(userVO.getUser().getAllGame());
                    playerInfos.add(playerInfo);
                }
                tempRoomInfo.setPlayerInfos(playerInfos);
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
    public String joinRoom(@RequestParam int roomIdx,@RequestParam  String token) {
        try {
            UserVO userVO = (UserVO) LoginedUser.get(token);
            if (userVO == null) {
                throw new Exception("未登录");
            }
            this.hallService.joinRoom(userVO, roomIdx);
            return new Gson().toJson(new ResponseResult(roomIdx));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/exit-room", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String exitRoom(@RequestParam String token) {
        try {
            UserVO userVO = (UserVO) LoginedUser.get(token);
            if (userVO == null) {
                throw new Exception("未登录");
            }
            int roomIdx = userVO.getRoomIdx();
            if (roomIdx != -1) {
                hallService.exitRoom(userVO);
            }
            return new Gson().toJson(new ResponseResult(roomIdx));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }
}

class RoomInfo {
    private List<PlayerInfo> playerInfos;

    public List<PlayerInfo> getPlayerInfos() {
        return playerInfos;
    }

    public void setPlayerInfos(List<PlayerInfo> playerInfos) {
        this.playerInfos = playerInfos;
    }
}

class PlayerInfo{
    private String userName;
    private int winGame;
    private int allGame;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWinGame() {
        return winGame;
    }

    public void setWinGame(int winGame) {
        this.winGame = winGame;
    }

    public int getAllGame() {
        return allGame;
    }

    public void setAllGame(int allGame) {
        this.allGame = allGame;
    }
}