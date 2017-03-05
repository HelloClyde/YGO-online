package cn.com.helloclyde.ygoService.vo;

import cn.com.helloclyde.ygoService.service.DuelService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
public class Room {
    private List<UserVO> players = new ArrayList<>();
    private DuelSession duelSession;
    private List<DuelLogItem> duelLogItems = new ArrayList<>();
    private List<Integer> LogPos = new ArrayList<>();

    public List<Integer> getLogPos() {
        return LogPos;
    }

    public void setLogPos(List<Integer> logPos) {
        LogPos = logPos;
    }

    public List<DuelLogItem> getDuelLogItems() {
        return duelLogItems;
    }

    public void setDuelLogItems(List<DuelLogItem> duelLogItems) {
        this.duelLogItems = duelLogItems;
    }

    public DuelSession getDuelSession() {
        return duelSession;
    }

    public void setDuelSession(DuelSession duelSession) {
        this.duelSession = duelSession;
    }

    public List<UserVO> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserVO> players) {
        this.players = players;
    }

}
