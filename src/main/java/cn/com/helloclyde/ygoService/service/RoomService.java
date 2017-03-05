package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.vo.Room;
import cn.com.helloclyde.ygoService.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by HelloClyde on 2017/3/5.
 */
@Service("roomService")
public class RoomService {
    @Autowired
    private DuelService duelService;

    synchronized public void addPlayer(Room room, UserVO userVO) throws Exception {
        synchronized (room) {
            if (room.getPlayers().size() < 2) {
                room.getPlayers().add(userVO);
            } else {
                throw new Exception("该房间玩家已满");
            }
            if (room.getPlayers().size() == 2) {
                // 初始化战局
                this.duelService.initDuel(room);
            }
        }
    }

    public void removePlayer(Room room, UserVO userVO) {
        synchronized (room) {
            room.getPlayers().remove(userVO);
        }
    }
}
