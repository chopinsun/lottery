package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.LotteryService;
import com.suns.lottery.tball.bean.Lottery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-13 19:56
 **/
@Controller
@RequestMapping("lottery")
public class MainController {
    @Autowired
    private LotteryService lotteryService;


    @RequestMapping
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/pullData")
    public String pulldate(){
//        lotteryService.pullAllData();
        return "done!";
    }

    @ResponseBody
    @RequestMapping("/generate/min")
    public Set<List<Integer>> generateMin(Integer n){
        Set<List<Integer>> list = lotteryService.generateNumByMin(n);
        return list;
    }

    @ResponseBody
    @RequestMapping("/generate/max")
    public List<String> generateMax(Integer n){
        List<String> list = lotteryService.generateNumByMax(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/generate/mix")
    public Set<List<Integer>> generateMix(Integer n){
        Set<List<Integer>> list = lotteryService.generateNumByMix(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/count")
    public JSONObject count(){
        JSONObject result = new JSONObject();

        result.put("redBall",lotteryService.redBallCount());
        result.put("blueBall",lotteryService.blueBallCount());

        return result;
    }

    @ResponseBody
    @RequestMapping("/history")
    public List<Lottery> history(Integer n){
        return lotteryService.history(n);
    }

}
