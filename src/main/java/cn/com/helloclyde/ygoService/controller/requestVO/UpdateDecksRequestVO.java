package cn.com.helloclyde.ygoService.controller.requestVO;

import java.util.List;

/**
 * Created by HelloClyde on 2017/3/22.
 */
public class UpdateDecksRequestVO {
    private String token;
    private List<Integer> newDecks;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Integer> getNewDecks() {
        return newDecks;
    }

    public void setNewDecks(List<Integer> newDecks) {
        this.newDecks = newDecks;
    }
}
