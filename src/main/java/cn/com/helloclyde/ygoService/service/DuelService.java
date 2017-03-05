package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.mapper.model.YgodataExample;
import cn.com.helloclyde.ygoService.mapper.model.YgodataWithBLOBs;
import cn.com.helloclyde.ygoService.mapper.persistence.YgodataMapper;
import cn.com.helloclyde.ygoService.vo.*;
import cn.com.helloclyde.ygoService.utils.Security;
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

    // 这个方法没有被控制器调用，是model直接调用的
    public void initDuel(Room room) {
        DuelSession duelSession = new DuelSession();
        // 初始化各个卡牌放置区域
        duelSession.setCemeteryCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setDecks(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setExtraDecks(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setParalleledCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
        duelSession.setHandCards(new List[]{new ArrayList<CardInfo>(), new ArrayList<CardInfo>()});
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
        logger.info("room:{}", new Gson().toJson(room));
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

    public void doClientAction(UserVO userVO, String action, Map<String,String> params){
        switch (action){
            case "GoDP":
                break;
            default:

        }
    }
}

