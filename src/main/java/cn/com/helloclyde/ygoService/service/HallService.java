package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.vo.Hall;
import cn.com.helloclyde.ygoService.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by HelloClyde on 2017/3/5.
 */
@Service("hallService")
public class HallService {
    @Autowired
    private RoomService roomService;

    public void joinRoom(UserVO userVO, int roomIdx) throws Exception {
        synchronized (Hall.getRooms()) {
            if (userVO.getRoomIdx() != -1) {
                // 退出先前房间
                this.exitRoom(userVO);
            }
            roomService.addPlayer(Hall.getRooms().get(roomIdx), userVO);
            userVO.setRoomIdx(roomIdx);
        }

    }

    public void exitRoom(UserVO userVO) {
        synchronized (Hall.getRooms()) {
            roomService.removePlayer(Hall.getRooms().get(userVO.getRoomIdx()), userVO);
            userVO.setRoomIdx(-1);
        }
    }
}
