package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.DltService;
import com.suns.lottery.tball.SsqService;
import com.suns.lottery.tball.bean.Ssq;
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
@RequestMapping("lottery")
public class MainController {
    @Autowired
    private SsqService ssqService;

    @Autowired
    private DltService dltService;


    @RequestMapping
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping("/dlt/pullAllData")
    public String pullDltdate(){
        dltService.pullAllData();
        return "done!";
    }

    @RequestMapping("/ssq/pullAllData")
    public String pullSsqdate(){
        ssqService.pullAllData();
        return "done!";
    }

    @ResponseBody
    @RequestMapping("/generate/min")
    public Set<List<Integer>> generateMin(Integer n){
        Set<List<Integer>> list = ssqService.generateNumByMin(n);
        return list;
    }

    @ResponseBody
    @RequestMapping("/generate/max")
    public List<String> generateMax(Integer n){
        List<String> list = ssqService.generateNumByMax(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/generate/mix")
    public Set<List<Integer>> generateMix(Integer n){
        Set<List<Integer>> list = ssqService.generateNumByMix(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/count")
    public JSONObject count(){
        JSONObject result = new JSONObject();

        result.put("redBall", ssqService.redBallCount());
        result.put("blueBall", ssqService.blueBallCount());

        return result;
    }

    @ResponseBody
    @RequestMapping("/history")
    public List<Ssq> history(Integer n){
        return ssqService.history(n);
    }

}
