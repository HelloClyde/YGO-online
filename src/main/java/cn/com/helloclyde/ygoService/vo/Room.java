package cn.com.helloclyde.ygoService.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class Room {
    private List<UserVO> players = new ArrayList<>();

    public List<UserVO> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserVO> players) {
        this.players = players;
    }

    public void addPlayer(UserVO userVO) throws Exception {
        if (this.players.size() < 2) {
            this.players.add(userVO);
        }else{
            throw new Exception("该房间玩家已满");
        }
    }

    public void removePlayer(UserVO userVO){
        this.players.remove(userVO);
    }
}
