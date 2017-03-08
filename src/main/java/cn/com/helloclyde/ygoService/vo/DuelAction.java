package cn.com.helloclyde.ygoService.vo;

/**
 * Created by HelloClyde on 2017/3/8.
 */
public enum DuelAction {
    // 流程相关
    TurnOperator("TurnOperator"), // (email) 操作人邮箱覆盖原email
    GotoDP("GotoDP"),GotoSP("GotoSP"),GotoM1P("GotoM1P"),GotoBP("GotoBP"),GotoM2P("GotoM2P"),GotoEP("GotoEP"), // 无参数
    GiveUp("GiveUp"), // 放弃，无参数
    // 自动操作
    DrawCard("DrawCard"), // 无参数
    // M1P和M2P流程操作
    CallNormalMonster("CallNormalMonster"), // (HandCardIdx,Status)
    CallMiddleMonster("CallMiddleMonster"), // (MonsterIdx,HandCardIdx,Status)
    CallHighMonster("CallHighMonster"), // (MonsterIdx1,MonsterIdx2,HandCardIdx,Status)
    ChangeMonsterStatus("ChangeMonsterStatus"), // (MonsterIdx, Status)
    PutMagic("PutMagic"), // (HandCardIdx,Status)
    CallMagic("CallMagic"), // (CardIdx)
    // BP流程操作
    AtkMonster("AtkMonster"); // (SrcMonsterIdx, DesMonsterIdx) -1 代表直接攻击玩家

    String action;

    DuelAction(String action){
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
