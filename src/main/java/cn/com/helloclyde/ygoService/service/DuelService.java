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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by HelloClyde on 2017/3/5.
 */
@Service("duelService")
public class DuelService {

    private static Logger logger = LoggerFactory.getLogger(DuelService.class);

    @Autowired
    private YgodataMapper ygodataMapper;

    @Autowired
    private ClientActionService clientActionService;

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
        // 怪物区和魔法区预先生成5个空位置
        for (int i = 0; i < 5; i++) {
            duelSession.getMonsterCards()[0].add(null);
            duelSession.getMonsterCards()[1].add(null);
            duelSession.getMagicCards()[0].add(null);
            duelSession.getMagicCards()[1].add(null);
        }
        // 设置其他参数
        duelSession.setIsCalled(new boolean[]{false, false});// 是否通常召唤过怪兽
        // 设置先手玩家
        duelSession.setTurnId(Math.abs(new Random().nextInt(2)));
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
        for (int userIdx = 0; userIdx < 2; userIdx++) {
            UserVO userVO = room.getPlayers().get(userIdx);
            for (int i = 0; i < 5; i++) {
                drawCard(room, userIdx, userVO);
            }
        }
        // 转到先手玩家
        clientActionService.turnOperate(room, room.getPlayers().get(room.getDuelSession().getTurnId()));
        room.getDuelSession().setTurnNum(1);
        // 进入DP
        clientActionService.gotoDP(room, room.getPlayers().get(room.getDuelSession().getTurnId()));
        // 抽卡
        drawCard(room, room.getDuelSession().getTurnId(), room.getPlayers().get(room.getDuelSession().getTurnId()));
        // 进入SP
        clientActionService.gotoSP(room, room.getPlayers().get(room.getDuelSession().getTurnId()));
        // 进入M1P
        clientActionService.gotoM1P(room, room.getPlayers().get(room.getDuelSession().getTurnId()));
    }

    private void readUserPackage(Room room, DuelSession duelSession) {
        for (int userIdx = 0; userIdx < room.getPlayers().size(); userIdx++) {
            UserPackage userPackage = new Gson().fromJson(room.getPlayers().get(userIdx).getUser().getCardPackages(), UserPackage.class);
            duelSession.getDecks()[userIdx].addAll(readCardsInArray(userPackage.getDecks()));
            duelSession.getExtraDecks()[userIdx].addAll(readCardsInArray(userPackage.getExtraDecks()));
        }
    }

    private List<CardInfo> readCardsInArray(List<Integer> idList) {
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
            if (action.equals(DuelAction.TurnOperator.getAction())) {
                turnOperator(room);
            } else if (action.equals(DuelAction.GotoDP.getAction())) {
                clientActionService.gotoDP(room, userVO);
            } else if (action.equals(DuelAction.GotoSP.getAction())) {
                clientActionService.gotoSP(room, userVO);
            } else if (action.equals(DuelAction.GotoM1P.getAction())) {
                clientActionService.gotoM1P(room, userVO);
            } else if (action.equals(DuelAction.GotoBP.getAction())) {
                clientActionService.gotoBP(room, userVO);
            } else if (action.equals(DuelAction.GotoM2P.getAction())) {
                clientActionService.gotoM2P(room, userVO);
            } else if (action.equals(DuelAction.GotoEP.getAction())) {
                clientActionService.gotoEP(room, userVO);
            } else if (action.equals(DuelAction.GiveUp.getAction())) {
                giveUp(room, userVO);
            } else if (action.equals(DuelAction.DrawCard.getAction())) {
                drawCard(room, userIdx, userVO);
            } else if (action.equals(DuelAction.CallNormalMonster.getAction())) {
                callNormalMonster(room, userVO, userIdx, params);
            } else if (action.equals(DuelAction.CallMiddleMonster.getAction())) {
                callMiddleMonster(room, userVO, userIdx, params);
            } else if (action.equals(DuelAction.CallHighMonster.getAction())) {
                callHighMonster(room, userVO, userIdx, params);
            } else if (action.equals(DuelAction.ChangeMonsterStatus.getAction())) {
                changeMonsterStatus(room, userVO, userIdx, params);
            } else if (action.equals(DuelAction.PutMagic.getAction())) {
                putMagic(room, userVO, userIdx, params);
            } else if (action.equals(DuelAction.CallMagic.getAction())) {
                callMagic(room, userVO, userIdx, params);
            } else if (action.equals(DuelAction.AtkMonster.getAction())) {
                atkMonster(room, userVO, userIdx, params);
            }
        }

    }

    private void atkMonster(Room room, UserVO userVO, int userIdx, Map<String, String> params) throws Exception {
        if (room.getDuelSession().getTurnNum() == 1){
            throw new Exception("第一回合不能直接攻击");
        }
        int srcMonsterIdx = Integer.parseInt(params.get("SrcMonsterIdx"));
        int desMonsterIdx = Integer.parseInt(params.get("DesMonsterIdx"));
        List<CardInfo> srcMonsterList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        List<CardInfo> srcCemeteryList = getDuelCardList(room, userIdx, DuelCardListType.CEMETERY);
        List<CardInfo> desCemeteryList = getDuelCardList(room, getEnemyIdx(room, userIdx), DuelCardListType.CEMETERY);
        CardInfo srcMonster = srcMonsterList.get(srcMonsterIdx);
        if (srcMonster.getParams().get("isAtk") != null && (boolean) srcMonster.getParams().get("isAtk")) {
            throw new Exception("该怪兽已经攻击过");
        }
        if ((int) srcMonster.getParams().get("Status") != 2) {
            throw new Exception("怪兽未在攻击状态");
        }
        srcMonster.getParams().put("isAtk", true);
        if (desMonsterIdx == -1) {
            int enemyHP = changeHP(room, getEnemy(room, userIdx), -srcMonster.getAtk());
            if (enemyHP <= 0) {
                clientActionService.win(room, userVO);
            } else {
                clientActionService.hPChange(room, getEnemyIdx(room, userIdx), -srcMonster.getAtk());
            }
            return;
        }
        List<CardInfo> desMonsterList = getDuelCardList(room, getEnemyIdx(room, userIdx), DuelCardListType.MONSTER);
        CardInfo desMonster = desMonsterList.get(desMonsterIdx);
        int desStatus = (int) desMonster.getParams().get("Status");
        switch (desStatus) {
            case 0:
                // 变为守备表示
                desMonster.getParams().put("Status", 1);
                clientActionService.monsterSub(room, getEnemy(room, userVO), desMonsterIdx);
                clientActionService.monsterAdd(room, getEnemy(room, userVO), desMonsterIdx, desMonster.getId(), 1);
            case 1:
                // 比较攻击力和防御力
                if (srcMonster.getAtk() > desMonster.getDef()) {
                    // 防守怪兽进入墓地
                    ArrayMoveToList(desMonsterList, desMonsterIdx, desCemeteryList, -1);
                    clientActionService.monsterSub(room, getEnemy(room, userVO), desMonsterIdx);
                    clientActionService.cemeteryAdd(room, getEnemy(room, userVO), desMonster.getId());
                } else {
                    // 扣血
                    int myHP = changeHP(room, userVO, srcMonster.getAtk() - desMonster.getDef());
                    if (myHP <= 0) {
                        clientActionService.win(room, getEnemy(room, userVO));
                    } else {
                        clientActionService.hPChange(room, userIdx, srcMonster.getAtk() - desMonster.getDef());
                    }
                }
                break;
            case 2:
                if (srcMonster.getAtk() == desMonster.getAtk()) {
                    // 两张怪兽都进入墓地
                    ArrayMoveToList(desMonsterList, desMonsterIdx, desCemeteryList, -1);
                    clientActionService.monsterSub(room, getEnemy(room, userVO), desMonsterIdx);
                    clientActionService.cemeteryAdd(room, getEnemy(room, userVO), desMonster.getId());
                    ArrayMoveToList(srcMonsterList, srcMonsterIdx, srcCemeteryList, -1);
                    clientActionService.monsterSub(room, userVO, srcMonsterIdx);
                    clientActionService.cemeteryAdd(room, userVO, srcMonster.getId());
                } else if (srcMonster.getAtk() > desMonster.getAtk()) {
                    // 敌方怪兽进入墓地
                    ArrayMoveToList(desMonsterList, desMonsterIdx, desCemeteryList, -1);
                    clientActionService.monsterSub(room, getEnemy(room, userVO), desMonsterIdx);
                    clientActionService.cemeteryAdd(room, getEnemy(room, userVO), desMonster.getId());
                    // 扣血
                    int enemyHP = changeHP(room, getEnemy(room, userIdx), desMonster.getAtk() - srcMonster.getAtk());
                    if (enemyHP <= 0) {
                        clientActionService.win(room, userVO);
                    } else {
                        clientActionService.hPChange(room, getEnemyIdx(room, userIdx), desMonster.getAtk() - srcMonster.getAtk());
                    }
                } else {
                    // 我方怪兽进入墓地
                    ArrayMoveToList(srcMonsterList, srcMonsterIdx, srcCemeteryList, -1);
                    clientActionService.monsterSub(room, userVO, srcMonsterIdx);
                    clientActionService.cemeteryAdd(room, userVO, srcMonster.getId());
                    // 扣血
                    int myHP = changeHP(room, userVO, srcMonster.getAtk() - desMonster.getAtk());
                    if (myHP <= 0) {
                        clientActionService.win(room, getEnemy(room, userVO));
                    } else {
                        clientActionService.hPChange(room, userIdx, srcMonster.getAtk() - desMonster.getAtk());
                    }
                }
        }
    }

    private void callMagic(Room room, UserVO userVO, int userIdx, Map<String, String> params) {
        int cardIdx = Integer.parseInt(params.get("CardIdx"));
        int status = Integer.parseInt(params.get("Status"));
        List<CardInfo> magicList = getDuelCardList(room, userIdx, DuelCardListType.MAGIC);
        magicList.get(cardIdx).getParams().put("Status", status);
        clientActionService.magicSub(room, userVO, cardIdx);
        clientActionService.magicAdd(room, userVO, cardIdx, magicList.get(cardIdx).getId(), status);
    }

    private void putMagic(Room room, UserVO userVO, int userIdx, Map<String, String> params) throws Exception {
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        List<CardInfo> magicList = getDuelCardList(room, userIdx, DuelCardListType.MAGIC);
        int handCardIdx = Integer.parseInt(params.get("HandCardIdx"));
        int status = Integer.parseInt(params.get("Status"));
        int cardIdx = ListMoveToArray(handList, handCardIdx, magicList);
        magicList.get(cardIdx).getParams().put("Status", status);
        clientActionService.handSub(room, userVO, handCardIdx);
        clientActionService.magicAdd(room, userVO, cardIdx, magicList.get(cardIdx).getId(), status);
    }

    private void changeMonsterStatus(Room room, UserVO userVO, int userIdx, Map<String, String> params) {
        int monsterIdx = Integer.parseInt(params.get("MonsterIdx"));
        int status = Integer.parseInt(params.get("Status"));
        List<CardInfo> monsterList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        CardInfo cardInfo = monsterList.get(monsterIdx);
        cardInfo.getParams().put("Status", status);
        // 删除原来的那只
        clientActionService.monsterSub(room, userVO, monsterIdx);
        clientActionService.monsterAdd(room, userVO, monsterIdx, cardInfo.getId(), status);
    }

    private void callHighMonster(Room room, UserVO userVO, int userIdx, Map<String, String> params) throws Exception {
        if (room.getDuelSession().getIsCalled()[userIdx]) {
            throw new Exception("每回合只能普通召唤一次");
        }
        int monsterIdx1 = Integer.parseInt(params.get("MonsterIdx1"));
        int monsterIdx2 = Integer.parseInt(params.get("MonsterIdx2"));
        int handCardIdx = Integer.parseInt(params.get("HandCardIdx"));
        int status = Integer.parseInt(params.get("Status"));
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        List<CardInfo> monsterList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        List<CardInfo> cemeteryList = getDuelCardList(room, userIdx, DuelCardListType.CEMETERY);
        CardInfo srcHandCard = handList.get(handCardIdx);
        if (srcHandCard.getStarNum() > 6) {
            // 祭品进墓地
            CardInfo monsterCardSrc1 = ArrayMoveToList(monsterList, monsterIdx1, cemeteryList, -1);
            clientActionService.monsterSub(room, userVO, monsterIdx1);
            clientActionService.cemeteryAdd(room, userVO, monsterCardSrc1.getId());
            CardInfo monsterCardSrc2 = ArrayMoveToList(monsterList, monsterIdx2, cemeteryList, -1);
            clientActionService.monsterSub(room, userVO, monsterIdx2);
            clientActionService.cemeteryAdd(room, userVO, monsterCardSrc2.getId());
            // 怪兽召唤到场地
            int cardIdx = ListMoveToArray(handList, handCardIdx, monsterList);
            srcHandCard.getParams().put("Status", status);
            clientActionService.handSub(room, userVO, handCardIdx);
            clientActionService.monsterAdd(room, userVO, cardIdx, srcHandCard.getId(), status);
            room.getDuelSession().getIsCalled()[userIdx] = true;
        } else {
            throw new Exception("该怪兽不符合召唤条件");
        }
    }

    private void callMiddleMonster(Room room, UserVO userVO, int userIdx, Map<String, String> params) throws Exception {
        if (room.getDuelSession().getIsCalled()[userIdx]) {
            throw new Exception("每回合只能普通召唤一次");
        }
        int monsterIdx = Integer.parseInt(params.get("MonsterIdx"));
        int handCardIdx = Integer.parseInt(params.get("HandCardIdx"));
        int status = Integer.parseInt(params.get("Status"));
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        List<CardInfo> monsterList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        List<CardInfo> cemeteryList = getDuelCardList(room, userIdx, DuelCardListType.CEMETERY);
        CardInfo srcHandCard = handList.get(handCardIdx);
        if (srcHandCard.getStarNum() > 4 && srcHandCard.getStarNum() <= 6) {
            // 祭品进墓地
            CardInfo monsterCardSrc = ArrayMoveToList(monsterList, monsterIdx, cemeteryList, -1);
            clientActionService.monsterSub(room, userVO, monsterIdx);
            clientActionService.cemeteryAdd(room, userVO, monsterCardSrc.getId());
            // 怪兽召唤到场地
            int cardIdx = ListMoveToArray(handList, handCardIdx, monsterList);
            srcHandCard.getParams().put("Status", status);
            clientActionService.handSub(room, userVO, handCardIdx);
            clientActionService.monsterAdd(room, userVO, cardIdx, srcHandCard.getId(), status);
            room.getDuelSession().getIsCalled()[userIdx] = true;
        } else {
            throw new Exception("该怪兽不符合召唤条件");
        }
    }

    private List<CardInfo> getDuelCardList(Room room, int userIdx, DuelCardListType duelCardListType) {
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

    private int changeHP(Room room, UserVO userVO, int hpValue) {
        int userIdx = room.getPlayers().indexOf(userVO);
        room.getDuelSession().getHP()[userIdx] += hpValue;
        return room.getDuelSession().getHP()[userIdx];
    }

    private int changeHP(Room room, int userIdx, int hpValue) {
        room.getDuelSession().getHP()[userIdx] += hpValue;
        return room.getDuelSession().getHP()[userIdx];
    }

    private UserVO getEnemy(Room room, int userIdx) {
        return room.getPlayers().get(userIdx == 0 ? 1 : 0);
    }

    private UserVO getEnemy(Room room, UserVO userVO) {
        return room.getPlayers().get(room.getPlayers().indexOf(userVO) == 0 ? 1 : 0);
    }

    private int getEnemyIdx(Room room, UserVO userVO) {
        int idx = room.getPlayers().indexOf(userVO);
        return idx == 0 ? 1 : 0;
    }

    private int getEnemyIdx(Room room, int userIdx) {
        return userIdx == 0 ? 1 : 0;
    }

    /**
     * 轮到对方玩家回合
     */
    private void turnOperator(Room room) throws Exception {
        int turnId = room.getDuelSession().getTurnId() == 0 ? 1 : 0;
        room.getDuelSession().setTurnId(turnId);
        clientActionService.turnOperate(room, room.getPlayers().get(turnId));
        clientActionService.gotoDP(room, room.getPlayers().get(turnId));
        try {
            drawCard(room, turnId, room.getPlayers().get(turnId));
        } catch (Exception e) {
            logger.error("err msg:", e);
        }
        clientActionService.gotoSP(room, room.getPlayers().get(turnId));
        room.getDuelSession().getIsCalled()[room.getDuelSession().getTurnId()] = false;
        // 清空怪兽攻击标志
        for (CardInfo monsterCard : room.getDuelSession().getMonsterCards()[room.getDuelSession().getTurnId()]) {
            if (monsterCard != null){
                monsterCard.getParams().put("isAtk", false);
            }
        }
        // 回合数加一
        room.getDuelSession().setTurnNum(room.getDuelSession().getTurnNum() + 1);
        clientActionService.gotoM1P(room, room.getPlayers().get(turnId));
    }

    /**
     * 放弃这次战局
     *
     * @param room
     * @param userVO
     */
    private void giveUp(Room room, UserVO userVO) {
        int winId = room.getPlayers().indexOf(userVO) == 0 ? 1 : 0;
        clientActionService.win(room, room.getPlayers().get(winId));
    }

    private CardInfo moveCard(List<CardInfo> src, int srcIdx, List<CardInfo> des, int desLimit) throws Exception {
        if (desLimit != -1 && des.size() >= desLimit) {
            throw new Exception("目标已满");
        }
        CardInfo cardInfo = src.remove(srcIdx);
        des.add(cardInfo);
        return cardInfo;
    }

    private CardInfo ArrayMoveToList(List<CardInfo> src, int srcIdx, List<CardInfo> des, int desLimit) throws Exception {
        if (desLimit != -1 && des.size() >= desLimit) {
            throw new Exception("目标已满");
        }
        if (srcIdx >= src.size()) {
            throw new Exception("Idx越界");
        }
        CardInfo cardInfo = src.get(srcIdx);
        if (cardInfo == null) {
            throw new Exception("Idx处没有内容");
        }
        src.set(srcIdx, null);
        des.add(cardInfo);
        return cardInfo;
    }

    private int ListMoveToArray(List<CardInfo> src, int srcIdx, List<CardInfo> des) throws Exception {
        CardInfo cardInfo = src.remove(srcIdx);
        for (int i = 0; i < des.size(); i++) {
            if (des.get(i) == null) {
                des.set(i, cardInfo);
                return i;
            }
        }
        throw new Exception("目标已满");
    }

    /**
     * 从牌组抽一张卡到手牌
     *
     * @param room
     * @param userIdx
     * @param userVO
     * @throws Exception
     */
    public void drawCard(Room room, int userIdx, UserVO userVO) throws Exception {
        List<CardInfo> deckList = getDuelCardList(room, userIdx, DuelCardListType.DECK);
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        int randomIdx = new Random().nextInt(deckList.size());
        CardInfo randomCardInfo = moveCard(deckList, randomIdx, handList, 6);
        clientActionService.handAdd(room, userVO, randomCardInfo.getId());
    }

    private void callNormalMonster(Room room, UserVO userVO, int userIdx, Map<String, String> params) throws Exception {
        if (room.getDuelSession().getIsCalled()[userIdx]) {
            throw new Exception("每回合只能普通召唤一次");
        }
        int handCardIdx = Integer.parseInt(params.get("HandCardIdx"));
        int status = Integer.parseInt(params.get("Status"));
        List<CardInfo> handList = getDuelCardList(room, userIdx, DuelCardListType.HAND);
        List<CardInfo> monsterList = getDuelCardList(room, userIdx, DuelCardListType.MONSTER);
        CardInfo srcHandCard = handList.get(handCardIdx);
        if (srcHandCard.getStarNum() <= 4) {
            int cardIdx = ListMoveToArray(handList, handCardIdx, monsterList);
            srcHandCard.getParams().put("Status", status);
            clientActionService.handSub(room, userVO, handCardIdx);
            clientActionService.monsterAdd(room, userVO, cardIdx, srcHandCard.getId(), status);
            room.getDuelSession().getIsCalled()[userIdx] = true;
        } else {
            throw new Exception("该怪兽不符合召唤条件");
        }
    }

}

