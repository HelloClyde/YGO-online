package cn.com.helloclyde.ygoService.vo;

import java.util.Date;
import java.util.Map;

/**
 * Created by HelloClyde on 2017/3/5.
 */
public class DuelLogItem {
    private Date time;
    private String email;
    private String action;
    private Map<String, String> paramsMap;
    private String duelId;

    public DuelLogItem(Date time, String email, String duelId, String action, Map<String, String> params) {
        this.time = time;
        this.email = email;
        this.action = action;
        this.paramsMap = params;
        this.duelId = duelId;
    }

    public DuelLogItem(String email, String duelId, String action, Map<String, String> params) {
        this(new Date(), email, duelId, action, params);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String> getParams() {
        return paramsMap;
    }

    public void setParams(Map<String, String> params) {
        this.paramsMap = params;
    }

    public String getDuelId() {
        return duelId;
    }

    public void setDuelId(String duelId) {
        this.duelId = duelId;
    }
}
