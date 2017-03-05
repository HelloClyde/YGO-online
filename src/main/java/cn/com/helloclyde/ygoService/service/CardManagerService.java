package cn.com.helloclyde.ygoService.service;

import cn.com.helloclyde.ygoService.mapper.model.YgodataExample;
import cn.com.helloclyde.ygoService.mapper.model.YgodataWithBLOBs;
import cn.com.helloclyde.ygoService.mapper.persistence.YgodataMapper;
import cn.com.helloclyde.ygoService.vo.CardInfo;
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

    public CardInfo getCardInfo(int cardId) throws Exception {
        YgodataExample ygodataExample = new YgodataExample();
        ygodataExample.createCriteria().andIdEqualTo(cardId + 1);
        List<YgodataWithBLOBs> ygodataWithBLOBses = ygodataMapper.selectByExampleWithBLOBs(ygodataExample);
        if (ygodataWithBLOBses.size() == 0) {
            throw new Exception("找不到该卡片");
        }
        return new CardInfo(ygodataWithBLOBses.get(0));
    }
}


