package cn.com.helloclyde.ygoService.vo;

/**
 * Created by HelloClyde on 2017/3/8.
 */
public enum DuelAction {
    // 流程相关
    TurnOperator, // (email) 操作人邮箱
    GotoDP,GotoSP,GotoM1P,GotoBP,GotoM2P,GotoEP, // 无参数
    GiveUp, // 放弃，无参数
    // 自动操作
    DrawCard, // 无参数
    // M1P和M2P流程操作
    CallNormalMonster, // (handCardIdx,status) 手牌序列
    CallMiddleMonster, // (monsterIdx1,handCardIdx,status)
    CallHighMonster, // (monsterIdx1,monsterIdx2,handCardIdx,status)
    PutMagic, // (handCardIdx)
    CallMagic, // (handCardIdx)
    ChangeMonsterStatus, // (monsterIdx, status)
    // BP流程操作
    AtkMonster; // (srcMonster, desMonster)

}
