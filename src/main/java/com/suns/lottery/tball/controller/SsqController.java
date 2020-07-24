package com.suns.lottery.tball.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.Kv;
import com.suns.lottery.tball.service.SsqService;
import com.suns.lottery.tball.bean.Ssq;
import com.suns.lottery.tball.service.filter.SsqFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-13 19:56
 **/
@Log4j2
@Controller
@RequestMapping("lottery/ssq")
public class SsqController {
    @Autowired
    private SsqService ssqService;
    @Autowired
    private SsqFilter ssqFilter;

    @RequestMapping
    public String index() {
        return "index";
    }

    @RequestMapping("/pullAllData")
    public String pullSsqdate() {
        ssqService.pullAllData();
        return "done!";
    }

    @ResponseBody
    @RequestMapping("/generate/min/{n}")
    public Set<List<Integer>> generateMin(@PathVariable("n") Integer n) {
        if (n == null || n == 0) {
            n = 10;
        }
        if (n > 2000) {
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = ssqService.generateNumByMin(n);
        return list;
    }

    @ResponseBody
    @RequestMapping("/generate/max/{n}")
    public Set<List<Integer>> generateMax(@PathVariable("n") Integer n) {
        if (n == null || n == 0) {
            n = 10;
        }
        if (n > 2000) {
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = ssqService.generateNumByMax(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/generate/mix/{n}")
    public Set<List<Integer>> generateMix(@PathVariable("n") Integer n) {
        if (n == null || n == 0) {
            n = 10;
        }
        if (n > 2000) {
            throw new RuntimeException("一次生成的数据不能超过2千");
        }
        Set<List<Integer>> list = ssqService.generateNumByMix(n);
        return list;
    }


    @ResponseBody
    @RequestMapping("/gtl/{type}/{n}")
    public String generateMix(@PathVariable("type") String type, @PathVariable("n") Integer n) {
        if (n == null || n == 0) {
            n = 10;
        }
        if (n > 50000) {
            throw new RuntimeException("一次生成的数据不能超过5万");
        }
        Set<List<Integer>> list;
        if ("min".equals(type)) {
            list = ssqService.generateNumByMin(n);
        } else if ("max".equals(type)) {
            list = ssqService.generateNumByMax(n);
        } else {
            list = ssqService.generateNumByMix(n);
        }
        ssqService.saveToLib(list, "ssq");
        return "done!";
    }


    @ResponseBody
    @RequestMapping("/count")
    public List<Map<String,Object>> count() {
        final List<Map<String,Integer>> redBallCnts = ssqService.redBallCount();
        final List<Map<String,Integer>> blueBallCnts = ssqService.blueBallCount();

        return IntStream.range(0,34).mapToObj(i->i).map(i->{
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
        }).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping("/history/{n}")
    public List<Ssq> history(@PathVariable("n") Integer n) {
        return ssqService.history(n);
    }

    @ResponseBody
    @RequestMapping("/ch/{name}")
    public String ch(@PathVariable("name") String name) {
        if(name.equals("cold")){
            List<Integer>list = ssqFilter.cold();
            log.info(list.size());
        }else{
            List<Integer>list = ssqFilter.hot();
            log.info(list.size());
        }
        return "done!";
    }
}
