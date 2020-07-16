package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.service.DltService;
import com.suns.lottery.tball.bean.Dlt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    @RequestMapping("/generate/min/{n}")
    public Set<List<Integer>> generateMin(@PathVariable("n")  Integer n){
        if(n==null || n==0){
            n = 10;
        }
        if(n>2000){
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = dltService.generateNumByMin(n);
        return list;
    }

    @ResponseBody
    @RequestMapping("/generate/max/{n}")
    public Set<List<Integer>> generateMax(@PathVariable("n")  Integer n){
        if(n==null || n==0){
            n = 10;
        }
        if(n>2000){
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = dltService.generateNumByMax(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/generate/mix/{n}")
    public Set<List<Integer>> generateMix(@PathVariable("n") Integer n){
        if(n==null || n==0){
            n = 10;
        }
        if(n>2000){
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = dltService.generateNumByMix(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/gtl/{type}/{n}")
    public String generateMix(@PathVariable("type") String type ,@PathVariable("n") Integer n){
        if(n==null || n==0){
             n = 100;
        }
        if(n>50000){
            throw new RuntimeException("一次生成的数据不能超过5万");
        }
        Set<List<Integer>> list;
        if("min".equals(type)){
            list = dltService.generateNumByMin(n);
        }else if("max".equals(type)){
            list = dltService.generateNumByMax(n);
        }else{
            list = dltService.generateNumByMix(n);
        }
        dltService.saveToLib(list,"dlt");
        return "done!";
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
    @RequestMapping("/history/{n}")
    public List<Dlt> history(@PathVariable("n") Integer n){
        return dltService.history(n);
    }

}
