package cn.com.helloclyde.ygoService.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HelloClyde on 2017/3/4.
 */
public class LoginedUser {
    private static volatile Map<String, Object> userSession = new HashMap<>();

    static public Object get(String key){
        return userSession.get(key);
    }

    synchronized static public void set(String key, Object value){
        userSession.put(key, value);
    }

}
