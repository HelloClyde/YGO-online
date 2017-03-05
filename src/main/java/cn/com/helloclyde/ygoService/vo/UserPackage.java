package cn.com.helloclyde.ygoService.vo;

import java.util.List;

/**
 * Created by HelloClyde on 2017/2/21.
 */
public class UserPackage {
    List<Integer> decks;
    List<Integer> extraDecks;

    public List<Integer> getDecks() {
        return decks;
    }

    public void setDecks(List<Integer> decks) {
        this.decks = decks;
    }

    public List<Integer> getExtraDecks() {
        return extraDecks;
    }

    public void setExtraDecks(List<Integer> extraDecks) {
        this.extraDecks = extraDecks;
    }
}
