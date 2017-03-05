package cn.com.helloclyde.ygoService.vo;

/**
 * Created by HelloClyde on 2017/2/28.
 */
public enum TurnState {
    DP(0), SP(1), M1P(2), BP(3), M2P(4), EP(5);
    private int index;

    TurnState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
