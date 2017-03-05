package cn.com.helloclyde.ygoService.vo;

import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class UserVO {
    private UserWithBLOBs user;
    private int roomIdx;

    public UserVO(UserWithBLOBs user) {
        this.user = user;
        this.roomIdx = -1;
    }

    public UserWithBLOBs getUser() {
        return user;
    }

    public void setUser(UserWithBLOBs user) {
        this.user = user;
    }

    public int getRoomIdx() {
        return roomIdx;
    }

    public void setRoomIdx(int roomIdx) {
        this.roomIdx = roomIdx;
    }
}
