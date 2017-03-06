package cn.com.helloclyde.ygoService.vo;

import java.util.List;

/**
 * Created by HelloClyde on 2017/2/17.
 */
public class DuelSession {

    /**
     * 战局id
     */
    private String id;
    /**
     * 当前轮操作的玩家，0和1
     */
    private int turnId;
    /**
     * 玩家所在状态
     */
    private TurnState[] turnStates;
    /**
     * 玩家血量
     */
    private int[] HP;
    /**
     * 双方主卡组
     */
    private List<CardInfo>[] decks;
    /**
     * 双方额外卡组
     */
    private List<CardInfo>[] extraDecks;
    /**
     * 双方墓地
     */
    private List<CardInfo>[] cemeteryCards;
    /**
     * 被排除在游戏外的牌
     */
    private List<CardInfo>[] paralleledCards;
    /**
     * 双方手牌
     */
    private List<CardInfo>[] handCards;
    /**
     * 双方怪兽区
     */
    private List<CardInfo>[] monsterCards;
    /**
     * 双方魔法陷阱区
     */
    private List<CardInfo>[] magicCards;
    /**
     * 环境卡
     */
    private CardInfo envCard;

    public List<CardInfo>[] getMonsterCards() {
        return monsterCards;
    }

    public void setMonsterCards(List<CardInfo>[] monsterCards) {
        this.monsterCards = monsterCards;
    }

    public List<CardInfo>[] getMagicCards() {
        return magicCards;
    }

    public void setMagicCards(List<CardInfo>[] magicCards) {
        this.magicCards = magicCards;
    }

    public int[] getHP() {
        return HP;
    }

    public void setHP(int[] HP) {
        this.HP = HP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }

    public TurnState[] getTurnStates() {
        return turnStates;
    }

    public void setTurnStates(TurnState[] turnStates) {
        this.turnStates = turnStates;
    }

    public List<CardInfo>[] getDecks() {
        return decks;
    }

    public void setDecks(List<CardInfo>[] decks) {
        this.decks = decks;
    }

    public List<CardInfo>[] getExtraDecks() {
        return extraDecks;
    }

    public void setExtraDecks(List<CardInfo>[] extraDecks) {
        this.extraDecks = extraDecks;
    }

    public List<CardInfo>[] getCemeteryCards() {
        return cemeteryCards;
    }

    public void setCemeteryCards(List<CardInfo>[] cemeteryCards) {
        this.cemeteryCards = cemeteryCards;
    }

    public List<CardInfo>[] getParalleledCards() {
        return paralleledCards;
    }

    public void setParalleledCards(List<CardInfo>[] paralleledCards) {
        this.paralleledCards = paralleledCards;
    }

    public List<CardInfo>[] getHandCards() {
        return handCards;
    }

    public void setHandCards(List<CardInfo>[] handCards) {
        this.handCards = handCards;
    }

    public CardInfo getEnvCard() {
        return envCard;
    }

    public void setEnvCard(CardInfo envCard) {
        this.envCard = envCard;
    }
}
