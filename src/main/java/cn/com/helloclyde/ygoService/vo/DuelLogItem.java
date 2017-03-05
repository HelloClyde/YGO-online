package cn.com.helloclyde.ygoService.vo;

import java.util.Date;
import java.util.Map;

/**
 * Created by HelloClyde on 2017/3/5.
 */
public class DuelLogItem {
    private Date time;
    private String userToken;
    private String action;
    private Map<String,String> params;
    private String duelId;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getDuelId() {
        return duelId;
    }

    public void setDuelId(String duelId) {
        this.duelId = duelId;
    }
}
