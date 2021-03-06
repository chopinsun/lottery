package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.common.JsonResult;
import com.suns.lottery.tball.service.DltService;
import com.suns.lottery.tball.bean.Dlt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public JsonResult pullDltdate(){
        dltService.pullAllData();
        return JsonResult.success("done!");
    }

    @ResponseBody
    @RequestMapping("/generate/min/{n}")
    public JsonResult generateMin(@PathVariable("n")  Integer n){
        if(n==null || n==0){
            n = 10;
        }
        if(n>2000){
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = dltService.generateNumByMin(n);
        return JsonResult.success(list);
    }

    @ResponseBody
    @RequestMapping("/generate/max/{n}")
    public JsonResult generateMax(@PathVariable("n")  Integer n){
        if(n==null || n==0){
            n = 10;
        }
        if(n>2000){
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = dltService.generateNumByMax(n);
        return JsonResult.success(list);
    }


    @ResponseBody
    @RequestMapping("/generate/mix/{n}")
    public JsonResult generateMix(@PathVariable("n") Integer n){
        if(n==null || n==0){
            n = 10;
        }
        if(n>2000){
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = dltService.generateNumByMix(n);
        return JsonResult.success(list);
    }


    @ResponseBody
    @RequestMapping("/gtl/{type}/{n}")
    public JsonResult generateMix(@PathVariable("type") String type ,@PathVariable("n") Integer n){
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
        return JsonResult.success("done!");
    }


    @ResponseBody
    @RequestMapping("/count")
    public JsonResult count(){
        final List<Map<String,Integer>> redBallCnts = dltService.redBallCount();
        final List<Map<String,Integer>> blueBallCnts = dltService.blueBallCount();

        return JsonResult.success(IntStream.range(0,36).mapToObj(i->i).map(i->{
            Map<String,Object> map = new HashMap<>();
            map.put("num",i);
            Optional<Map<String,Integer>> rb = redBallCnts.stream().filter(x->x.get("ball").equals(i)).findAny();
            if(rb.isPresent()){
                map.put("redBall",rb.get().get("cnt"));
            }else{
                map.put("redBall",0);
            }
            Optional<Map<String,Integer>> bb = blueBallCnts.stream().filter(x->x.get("ball").equals(i)).findAny();
            if(bb.isPresent()){
                map.put("blueBall",bb.get().get("cnt"));
            }
            return map;
        }).collect(Collectors.toList()));
    }

    @ResponseBody
    @RequestMapping("/history/{n}")
    public JsonResult history(@PathVariable("n") Integer n){
        return JsonResult.success(dltService.history(n));
    }

}
