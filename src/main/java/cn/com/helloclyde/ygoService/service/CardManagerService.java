package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.mapper.model.Config;
import cn.com.helloclyde.ygoService.mapper.model.ConfigExample;
import cn.com.helloclyde.ygoService.mapper.model.YgodataExample;
import cn.com.helloclyde.ygoService.mapper.model.YgodataWithBLOBs;
import cn.com.helloclyde.ygoService.mapper.persistence.ConfigMapper;
import cn.com.helloclyde.ygoService.mapper.persistence.YgodataMapper;
import cn.com.helloclyde.ygoService.vo.CardInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HelloClyde on 2017/3/3.
 */
@Service("cardManagerService")
public class CardManagerService {
    @Autowired
    private YgodataMapper ygodataMapper;
    @Autowired
    private ConfigMapper configMapper;

    public CardInfo getCardInfo(int cardId) throws Exception {
        YgodataExample ygodataExample = new YgodataExample();
        ygodataExample.createCriteria().andIdEqualTo(cardId + 1);
        List<YgodataWithBLOBs> ygodataWithBLOBses = ygodataMapper.selectByExampleWithBLOBs(ygodataExample);
        if (ygodataWithBLOBses.size() == 0) {
            throw new Exception("找不到该卡片");
        }
        return new CardInfo(ygodataWithBLOBses.get(0));
    }

    public List<Integer> getEnableCards(){
        ConfigExample configExample = new ConfigExample();
        configExample.createCriteria().andKeyEqualTo("enable_cards");
        List<Config> configs = configMapper.selectByExample(configExample);
        return new Gson().fromJson(configs.get(0).getValue(), List.class);
    }
}


