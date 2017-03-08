package cn.com.helloclyde.ygoService.vo;

/**
 * Created by HelloClyde on 2017/3/8.
 */
public enum ClientAction {
    // 流程相关
    TurnOperator("TurnOperator"), // (email) 操作人邮箱覆盖原email
    GotoDP("GotoDP"),GotoSP("GotoSP"),GotoM1P("GotoM1P"),GotoBP("GotoBP"),GotoM2P("GotoM2P"),GotoEP("GotoEP"), // 无参数
    Win("Win"), // 获胜 (email) 操作人邮箱覆盖原email
    // 手牌操作
    HandAdd("HandAdd"), // (CardId)
    HandSub("HandSub"), // (CardIdx)
    // 怪兽区操作
    MonsterAdd("MonsterAdd"), // (CardIdx,CardId,Status) Status:0 里侧，1守备，2攻击
    MonsterSub("MonsterSub"), // (CardIdx)
    // 墓地操作
    CemeteryAdd("CemeteryAdd"), // (CardId)
    CemeterySub("CemeterySub"), // (CardIdx)
    // 魔法区操作
    MagicAdd("MagicAdd"), // (CardIdx,CardId,Status) Status:0 里侧，表侧
    MagicSub("MagicSub"), // (CardIdx)
    // 血量改变
    HPChange("HPChange"); // (HPValue)

    String action;

    ClientAction(String action){
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
