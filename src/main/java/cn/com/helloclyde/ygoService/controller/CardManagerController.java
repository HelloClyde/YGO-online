package cn.com.helloclyde.ygoService.controller;

import cn.com.helloclyde.ygoService.service.CardManagerService;
import cn.com.helloclyde.ygoService.vo.ResponseResult;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by HelloClyde on 2017/3/3.
 */
@Controller
@RequestMapping(value = "/card-manager")
public class CardManagerController {
    @Autowired
    private CardManagerService cardManagerService;

    @ResponseBody
    @RequestMapping(value = "/card-info", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public String getCardInfo(@RequestParam int cardId) {
        try {
            return new Gson().toJson(new ResponseResult(cardManagerService.getCardInfo(cardId)));
        } catch (Exception e) {
            e.printStackTrace();
            return new Gson().toJson(new ResponseResult(e.hashCode(), e.getMessage()));
        }
    }
}
