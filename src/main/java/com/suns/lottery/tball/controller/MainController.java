package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-13 19:56
 **/
@Controller
@RequestMapping("/lottery")
public class MainController {
    @Autowired
    private LotteryService lotteryService;

    @ResponseBody
    @RequestMapping("/pullData")
    public String pulldate(){
//        lotteryService.pullAllData();
        return "done!";
    }

    @ResponseBody
    @RequestMapping("/generate/min")
    public List<String> generateMin(Integer n){
        List<String> list = lotteryService.generateNumByMin(n);
        return list;
    }

    @ResponseBody
    @RequestMapping("/generate/max")
    public List<String> generateMax(Integer n){
        List<String> list = lotteryService.generateNumByMax(n);
        return list;
    }

}
