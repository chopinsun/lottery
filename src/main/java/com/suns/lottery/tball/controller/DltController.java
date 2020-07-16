package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.DltService;
import com.suns.lottery.tball.bean.Dlt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-13 19:56
 **/
@Controller
@RequestMapping("lottery/dlt")
public class DltController {
    @Autowired
    private DltService dltService;


    @RequestMapping
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/pullAllData")
    public String pullDltdate(){
        dltService.pullAllData();
        return "done!";
    }

    @ResponseBody
    @RequestMapping("/generate/min")
    public Set<List<Integer>> generateMin(Integer n){
        Set<List<Integer>> list = dltService.generateNumByMin(n);
        return list;
    }

    @ResponseBody
    @RequestMapping("/generate/max")
    public List<String> generateMax(Integer n){
        List<String> list = dltService.generateNumByMax(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/generate/mix")
    public Set<List<Integer>> generateMix(Integer n){
        Set<List<Integer>> list = dltService.generateNumByMix(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/count")
    public JSONObject count(){
        JSONObject result = new JSONObject();

        result.put("redBall", dltService.redBallCount());
        result.put("blueBall", dltService.blueBallCount());

        return result;
    }

    @ResponseBody
    @RequestMapping("/history")
    public List<Dlt> history(Integer n){
        return dltService.history(n);
    }

}
