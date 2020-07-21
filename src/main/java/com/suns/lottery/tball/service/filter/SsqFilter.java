package com.suns.lottery.tball.service.filter;

import com.alibaba.fastjson.JSONArray;
import com.suns.lottery.tball.bean.Kv;
import com.suns.lottery.tball.bean.Ssq;
import com.suns.lottery.tball.utils.Cache;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-16 19:00
 **/
@Log4j2
@Component
public class SsqFilter {

    @Autowired
    private Cache cache;

    public List<Integer> cold() {
        List<Ssq> list = (List<Ssq>)cache.get(Cache.Type.SSQ);
        List<Kv<Integer,Integer>> l = IntStream.range(1,34).mapToObj(x->x).map(i->{
            Long n = list.stream().limit(15).filter(x->x.getR1().equals(i)
                    || x.getR2().equals(i)
                    || x.getR3().equals(i)
                    || x.getR4().equals(i)
                    || x.getR5().equals(i)
                    || x.getR6().equals(i)).count();
            return new Kv<Integer,Integer>(i,n.intValue());
        }).sorted(Comparator.comparing(Kv<Integer,Integer>::getValue)).collect(Collectors.toList());
        return l.stream().map(Kv::getKey).collect(Collectors.toList());
    }


    public List<Integer> hot() {
        List<Ssq> list = (List<Ssq>)cache.get(Cache.Type.SSQ);
        List<Kv<Integer,Integer>> l = IntStream.range(1,34).mapToObj(x->x).map(i->{
            Long n = list.stream().limit(15).filter(x->x.getR1().equals(i)
                    || x.getR2().equals(i)
                    || x.getR3().equals(i)
                    || x.getR4().equals(i)
                    || x.getR5().equals(i)
                    || x.getR6().equals(i)).count();
            return new Kv<Integer,Integer>(i,n.intValue());
        }).sorted(Comparator.comparing(Kv<Integer,Integer>::getValue).reversed()).collect(Collectors.toList());
        log.info(JSONArray.toJSONString(l));
        return l.stream().map(Kv::getKey).collect(Collectors.toList());
    }
}
