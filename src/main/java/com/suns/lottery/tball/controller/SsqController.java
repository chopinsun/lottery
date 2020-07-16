package com.suns.lottery.tball.controller;

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

import java.util.List;
import java.util.Set;

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
    public JSONObject count() {
        JSONObject result = new JSONObject();

        result.put("redBall", ssqService.redBallCount());
        result.put("blueBall", ssqService.blueBallCount());

        return result;
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
