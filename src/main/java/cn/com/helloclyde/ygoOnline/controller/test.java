package cn.com.helloclyde.ygoOnline.controller;

import com.google.gson.Gson;
import com.jcabi.aspects.Loggable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by HelloClyde on 2016/12/4.
 */
@Controller
@RequestMapping(value = "/test")
public class test {

    @Loggable(Loggable.INFO)
    @ResponseBody
    @RequestMapping(value = "/add", method = {RequestMethod.GET},produces="application/json;charset=UTF-8")
    public String add(@RequestParam(value = "a", required = true) int a, @RequestParam(value = "b", required = true) int b) {
        return new Gson().toJson(a + b);
    }
}