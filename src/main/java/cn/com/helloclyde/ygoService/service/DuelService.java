package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.mapper.model.YgodataExample;
import cn.com.helloclyde.ygoService.mapper.model.YgodataWithBLOBs;
import cn.com.helloclyde.ygoService.mapper.persistence.YgodataMapper;
import cn.com.helloclyde.ygoService.utils.Security;
import cn.com.helloclyde.ygoService.vo.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by HelloClyde on 2017/3/5.
 */
@Service("duelService")
public class DuelService {

    private static Logger logger = LoggerFactory.getLogger(DuelService.class);

    @Autowired
    private YgodataMapper ygodataMapper;

    // 这个方法没有被控制器调用，是model直接调用的
    public void initDuel(Room room) throws Exception {
        DuelSession duelSession = new DuelSession();
        // 初始化各个卡牌放置区域
        duelSession.setCemeteryCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setDecks(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setExtraDecks(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setParalleledCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setHandCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setHP(new int[]{8000, 8000});
        duelSession.setMonsterCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setMagicCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        // 设置先手玩家
        duelSession.setTurnId(new Random().nextInt() % 2);
        duelSession.setTurnStates(new TurnState[]{TurnState.DP, TurnState.DP});
        // 生成战局id
        duelSession.setId(Security.md5(String.valueOf(room.getPlayers().get(0).getUser().getId()) +
                String.valueOf(room.getPlayers().get(0).getUser().getId())) + String.valueOf(System.currentTimeMillis()));
        // 读取用户牌组卡牌
        readUserPackage(room, duelSession);
        // 写入room
        room.setDuelSession(duelSession);
        room.setDuelLogItems(new ArrayList<>());
        room.setLogPos(new ArrayList<Integer>() {{
            this.add(0);
            this.add(0);
        }});
        // 初始抽牌
        for (int userIdx = 0;userIdx < 2;userIdx ++) {
            UserVO userVO = room.getPlayers().get(userIdx);
            for (int i = 0; i < 5; i++) {
                DrawCard(room,userIdx, userVO,"DrawCard");
            }
        }
    }

    private void readUserPackage(Room room, DuelSession duelSession) {
        for (int userIdx = 0; userIdx < room.getPlayers().size(); userIdx++) {
            UserPackage userPackage = new Gson().fromJson(room.getPlayers().get(userIdx).getUser().getCardPackages(), UserPackage.class);
            duelSession.getDecks()[userIdx].addAll(readCardsInArray(userPackage.getDecks()));
            duelSession.getExtraDecks()[userIdx].addAll(readCardsInArray(userPackage.getExtraDecks()));
        }
    }

    public List<CardInfo> readCardsInArray(List<Integer> idList) {
        List<CardInfo> cards = new ArrayList<>();
        if (idList.size() == 0) {
            return cards;
        }
        YgodataExample ygodataExample = new YgodataExample();
        ygodataExample.createCriteria().andIdIn(idList);
        System.out.println(ygodataMapper);
        List<YgodataWithBLOBs> resultCards = ygodataMapper.selectByExampleWithBLOBs(ygodataExample);
        for (YgodataWithBLOBs resultCard : resultCards) {
            CardInfo card = new CardInfo(resultCard);
            cards.add(card);
        }
        return cards;
    }

    public void doClientAction(UserVO userVO, String action, Map<String, String> params) throws Exception {
        Room room = Hall.getRooms().get(userVO.getRoomIdx());
        int userIdx = room.getPlayers().indexOf(userVO);
        if (userIdx != room.getDuelSession().getTurnId()) {
            throw new Exception("不是当前操作玩家");
        }
        synchronized (room) {
            switch (action) {
                case "GoDP":
                    // 跳到SP
                    Goto(room, userIdx, userVO, action, TurnState.DP);
                    break;
                case "GoSP":
                    // 跳到SP
                    Goto(room, userIdx, userVO, action, TurnState.SP);
                    break;
                case "GoM1P":
                    // 跳到M1
                    Goto(room, userIdx, userVO, action, TurnState.M1P);
                    break;
                case "GoBP":
                    // 跳到BP
                    Goto(room, userIdx, userVO, action, TurnState.BP);
                    break;
                case "GoM2P":
                    // 跳到M2
                    Goto(room, userIdx, userVO, action, TurnState.M2P);
                    break;
                case "EP":
                    // 跳到EP
                    Goto(room, userIdx, userVO, action, TurnState.EP);
                    break;
                case "SwitchTurn":
                    // 轮换对方玩家操作
                    SwitchTurn(room, userVO, action);
                    break;
                case "DrawCard":
                    // 从牌组抽一张卡到手牌
                    DrawCard(room, userIdx, userVO, action);
                    break;
                case "CallMonsterFromHand":
                    // 从手牌往自己场上召唤一只怪兽
                    CallMonsterFromHand(room, userIdx, userVO, action,
                            Integer.parseInt(params.get("HandCardIdx")), Integer.parseInt(params.get("MonsterStatus")));
                    break;
                case "PutMagicFromHand":
                    // 从手牌往自己场上放置一张魔法陷阱卡
                    PutMagicFromHand(room, userIdx, userVO, action,
                            Integer.parseInt("HandCardIdx"));
                    break;
                case "ChangeMonsterStatus":
                    // 改变自己场上一只怪兽的表示形态(里侧表示，表侧表示，攻击表示)
                    ChangeMonsterStatus(room, userIdx, userVO, action,
                            Integer.parseInt("MonsterCardIdx"), Integer.parseInt("MonsterStatus"));
                    break;
                case "CallMagic":
                    // 发动自己场上的一张魔法卡
                    break;
                case "AckMonster":
                    // 控制自己场上的怪兽攻击敌方场上的怪兽
                    break;
                case "AckEnemy":
                    // 控制自己场上的怪兽攻击敌方玩家
                    break;
                default:
                    //
            }

        }
    }

    public List<CardInfo> getDuelCardList(Room room, int userIdx, DuelCardListType duelCardListType) {
        switch (duelCardListType) {
            case CEMETERY:
                return room.getDuelSession().getCemeteryCards()[userIdx];
            case DECK:
                return room.getDuelSession().getDecks()[userIdx];
            case EXTRA_DECK:
                return room.getDuelSession().getExtraDecks()[userIdx];
            case HAND:
                return room.getDuelSession().getHandCards()[userIdx];
            case PARALLELED:
                return room.getDuelSession().getParalleledCards()[userIdx];
            case MONSTER:
                return room.getDuelSession().getMonsterCards()[userIdx];
            case MAGIC:
                return room.getDuelSession().getMagicCards()[userIdx];
            default:
                return new ArrayList<>();
        }
    }

    /**
     * 跳转游戏状态
     *
     * @param room
     * @param userIdx
     * @param userVO
     * @param action
     * @param turnState DP -> SP -> M1P -> BP -> M2P -> EP
     */
    public void Goto(Room room, int userIdx, UserVO userVO, String action, TurnState turnState) {
        room.getDuelSession().getTurnStates()[userIdx] = turnState;
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        action, new HashMap<String, String>()));
    }

    /**
     * 轮到对方玩家回合
     *
     * @param room
     * @param userVO
     * @param action
     */
    public void SwitchTurn(Room room, UserVO userVO, String action) {
        room.getDuelSession().setTurnId(room.getDuelSession().getTurnId() == 0 ? 1 : 0);
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        action, new HashMap<String, String>()));
    }

    public CardInfo moveCard(List<CardInfo> src, int srcIdx, List<CardInfo> des, int desLimit) throws Exception {
        if (des.size() >= desLimit) {
            throw new Exception("目标已满");
        }
        CardInfo cardInfo = src.remove(srcIdx);
        des.add(cardInfo);
        return cardInfo;
    }

    /**
     * 从牌组抽一张卡到手牌
     *
     * @param room
     * @param userIdx
     * @param userVO
     * @param action
     * @throws Exception
     */
    public void DrawCard(Room room, int userIdx, UserVO userVO, String action) throws Exception {
        List<CardInfo> deckList = getDuelCardList(room, userIdx, DuelCardListType.DECK);
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        int randomIdx = new Random().nextInt(deckList.size());
        CardInfo randomCardInfo = moveCard(deckList, randomIdx, handList, 5);
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        action, new HashMap<String, String>() {{
                    this.put("CardId", String.valueOf(randomCardInfo.getId()));
                }}));
    }

    /**
     * 从手牌往自己场上召唤一只怪兽
     *
     * @param room
     * @param userIdx
     * @param userVO
     * @param action
     * @param handCardIdx
     * @param monsterStatus 里侧表示，表侧表示，攻击表示 （0，1，2）
     * @throws Exception
     */
    public void CallMonsterFromHand(Room room, int userIdx, UserVO userVO, String action, int handCardIdx, int monsterStatus) throws Exception {
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        List<CardInfo> monsterList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        CardInfo monsterCard = moveCard(handList, handCardIdx, monsterList, 5);
        monsterCard.getParams().put("MonsterStatus", String.valueOf(monsterStatus));
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        action, new HashMap<String, String>() {{
                    this.put("HandCardIdx", String.valueOf(handCardIdx));
                    this.put("MonsterStatus", String.valueOf(monsterStatus));
                    this.put("CardId", String.valueOf(monsterCard.getId()));
                }}));
    }

    public void PutMagicFromHand(Room room, int userIdx, UserVO userVO, String action, int handCardIdx) throws Exception {
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        List<CardInfo> magicList = getDuelCardList(room, userIdx, DuelCardListType.MAGIC);
        CardInfo magicCard = moveCard(handList, handCardIdx, magicList, 5);
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        action, new HashMap<String, String>() {{
                    this.put("HandCardIdx", String.valueOf(handCardIdx));
                    this.put("CardId", String.valueOf(magicCard.getId()));
                }}));
    }

    private void ChangeMonsterStatus(Room room, int userIdx, UserVO userVO, String action, int monsterCardIdx, int monsterStatus) {
        List<CardInfo> magicList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        CardInfo monsterCard = magicList.get(monsterCardIdx);
        monsterCard.getParams().put("MonsterStatus", String.valueOf(monsterStatus));
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        action, new HashMap<String, String>() {{
                    this.put("MonsterCardIdx", String.valueOf(monsterCardIdx));
                    this.put("CardId", String.valueOf(monsterCard.getId()));
                }}));
    }
}

