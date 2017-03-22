package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.mapper.model.UserExample;
import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;
import cn.com.helloclyde.ygoService.mapper.persistence.UserMapper;
import cn.com.helloclyde.ygoService.vo.UserPackage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HelloClyde on 2017/3/4.
 */
@Service("userOpService")
public class UserOpService {
    @Autowired
    private UserMapper userMapper;

    public UserWithBLOBs getUser(String email, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email).andPasswordEqualTo(password);
        List<UserWithBLOBs> userList = userMapper.selectByExampleWithBLOBs(userExample);
        if (userList.size() == 0) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    public void postDecks(UserWithBLOBs userWithBLOBs, List<Integer> newDecks){
        Gson gson = new Gson();
        UserPackage userPackage = gson.fromJson(userWithBLOBs.getCardPackages(), UserPackage.class);
        userPackage.setDecks(newDecks);
        userWithBLOBs.setCardPackages(gson.toJson(userPackage));
        userMapper.updateByPrimaryKeyWithBLOBs(userWithBLOBs);
    }
}
