package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.mapper.model.UserExample;
import cn.com.helloclyde.ygoService.mapper.model.UserWithBLOBs;
import cn.com.helloclyde.ygoService.mapper.persistence.UserMapper;
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
}
