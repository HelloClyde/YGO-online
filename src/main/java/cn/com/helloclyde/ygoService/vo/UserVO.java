package cn.com.helloclyde.ygoService.vo;

import cn.com.helloclyde.ygoService.mapper.model.User;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class UserVO {
    private User user;
    private int roomIdx;

    public UserVO(User user) {
        this.user = user;
        this.roomIdx = -1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRoomIdx() {
        return roomIdx;
    }

    public void setRoomIdx(int roomIdx) {
        this.roomIdx = roomIdx;
    }
}
