package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.vo.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HelloClyde on 2017/3/8.
 */
@Service("clientActionService")
public class ClientActionService {

    public void win(Room room, UserVO userVO) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.Win.getAction(), new HashMap<>())
        );
    }

    public void turnOperate(Room room, UserVO userVO) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.TurnOperator.getAction(), new HashMap<>()));
    }

    private void goTo(Room room, UserVO userVO, TurnState turnState) {
        room.getDuelSession().getTurnStates()[room.getPlayers().indexOf(userVO)] = turnState;
        ClientAction clientAction = null;
        if (turnState == TurnState.DP) {
            clientAction = ClientAction.GotoDP;
        } else if (turnState == TurnState.SP) {
            clientAction = ClientAction.GotoSP;
        } else if (turnState == TurnState.M1P) {
            clientAction = ClientAction.GotoM1P;
        } else if (turnState == TurnState.BP) {
            clientAction = ClientAction.GotoBP;
        } else if (turnState == TurnState.M2P) {
            clientAction = ClientAction.GotoM2P;
        } else if (turnState == TurnState.EP) {
            clientAction = ClientAction.GotoEP;
        } else {
            return;
        }
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        clientAction.getAction(), new HashMap<>()));
    }

    public void gotoDP(Room room, UserVO userVO) {
        goTo(room, userVO, TurnState.DP);
    }

    public void gotoSP(Room room, UserVO userVO) {
        goTo(room, userVO, TurnState.SP);
    }

    public void gotoM1P(Room room, UserVO userVO) {
        goTo(room, userVO, TurnState.M1P);
    }

    public void gotoBP(Room room, UserVO userVO) {
        goTo(room, userVO, TurnState.BP);
    }

    public void gotoM2P(Room room, UserVO userVO) {
        goTo(room, userVO, TurnState.M2P);
    }

    public void gotoEP(Room room, UserVO userVO) {
        goTo(room, userVO, TurnState.EP);
    }

    public void handAdd(Room room, UserVO userVO, int cardId) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.HandAdd.getAction(), new HashMap<String, Object>() {{
                    this.put("CardId", cardId);
                }})
        );
    }

    public void handSub(Room room, UserVO userVO, int cardIdx) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.HandSub.getAction(), new HashMap<String, Object>() {{
                    this.put("CardIdx", cardIdx);
                }})
        );
    }

    public void monsterAdd(Room room, UserVO userVO, int cardIdx, int cardId, int status) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.MonsterAdd.getAction(), new HashMap<String, Object>() {{
                    this.put("CardIdx", cardIdx);
                    this.put("CardId", cardId);
                    this.put("Status", status);
                }})
        );
    }

    public void monsterSub(Room room, UserVO userVO, int cardIdx) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.MonsterSub.getAction(), new HashMap<String, Object>() {{
                    this.put("CardIdx", cardIdx);
                }})
        );
    }

    public void cemeteryAdd(Room room, UserVO userVO, int cardId) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.CemeteryAdd.getAction(), new HashMap<String, Object>() {{
                    this.put("CardId", cardId);
                }})
        );
    }

    public void cemeterySub(Room room, UserVO userVO, int cardIdx) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.CemeterySub.getAction(), new HashMap<String, Object>() {{
                    this.put("CardIdx", cardIdx);
                }})
        );
    }

    public void magicAdd(Room room, UserVO userVO, int cardIdx, int cardId, int status) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.MagicAdd.getAction(), new HashMap<String, Object>() {{
                    this.put("CardIdx", cardIdx);
                    this.put("CardId", cardId);
                    this.put("Status", status);
                }})
        );
    }

    public void magicSub(Room room, UserVO userVO, int cardIdx) {
        room.getDuelLogItems().add(
                new DuelLogItem(userVO.getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.MagicSub.getAction(), new HashMap<String, Object>() {{
                    this.put("CardIdx", cardIdx);
                }})
        );
    }

    public void hPChange(Room room, int userIdx, int hPValue) {
        room.getDuelLogItems().add(
                new DuelLogItem(room.getPlayers().get(userIdx).getUser().getEmail(), room.getDuelSession().getId(),
                        ClientAction.HPChange.getAction(), new HashMap<String, Object>() {{
                    this.put("HPValue", hPValue);
                }})
        );
    }
}
