package cn.com.helloclyde.ygoService.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class Hall {
    private static volatile List<Room> rooms = new ArrayList<>();

    static {
        for (int i = 0; i < 60; i++) {
            Hall.rooms.add(new Room());
        }
    }

    static public synchronized void joinRoom(UserVO userVO, int roomIdx) throws Exception {
        if (userVO.getRoomIdx() != -1){
            // 退出先前房间
            exitRoom(userVO);
        }
        Hall.rooms.get(roomIdx).addPlayer(userVO);
        userVO.setRoomIdx(roomIdx);
    }

    static public synchronized void exitRoom(UserVO userVO) {
        Hall.rooms.get(userVO.getRoomIdx()).removePlayer(userVO);
        userVO.setRoomIdx(-1);
    }

    static public List<Room> lists() {
        return rooms;
    }
}
