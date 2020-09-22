package com.suns.lottery.tball.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.*;
import com.suns.lottery.tball.mapper.DltDetailMapper;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.mapper.SsqMapper;
import com.suns.lottery.tball.utils.HttpInvoker;
import com.suns.lottery.tball.utils.NumberUtils;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by chopinsun on 2020/2/1.
 */
@Log4j2
@Component
public class TaskDlt {
    @Autowired
    private HttpInvoker httpInvoker;

    @Autowired
    private DltMapper dltMapper;
    @Autowired
    private DltDetailMapper dltDetailMapper;

    /**
     * @Description: 定时同步 大乐透 最新数据
     * @param null
     * @return:
     *
     * @Creator: sunxiaobo
     * @Date: 2020/1/14 19:46
     *
     * @Modify: sunxiaobo
     * @Date: 2020/1/14 19:46
     *
     **/
    @Scheduled(cron = "0 30 21 * * 1,3,6")
    public void upateDltData(){
        log.info("------------启动定时任务，拉取最新一期数据 [大乐透]-----------");
        String dbLastCode = dbLastCode();
        String lastCode = remoteLastCode(dbLastCode);
        IntStream.range(Integer.valueOf(dbLastCode),Integer.valueOf(lastCode)+1)
                .mapToObj(i->i)
                .map(x->String.valueOf(x))
                .map(term->historyData(term))
                .forEach(x->{
                    int exists = dltMapper.countByCode(x.getLottery().getTerm());
                    if(exists == 0){
                        log.info("是新数据，执行插入:{}",JSONObject.toJSONString(x));
                        Dlt dlt= x.convert();
                        dltMapper.insert(dlt);
                        dltDetailMapper.batchInsert(dlt.getDetails());
                    }
                });
    }
    private String dbLastCode(){
        List<Dlt> histories = dltMapper.history(1);
        return histories.get(0).getCode();
    }

    private String remoteLastCode(String term){
        HistoryDataDlt lastData = historyData(term);
        List<String> result = lastData.getEventName();
        return result.get(0);
    }

    /**
    * @Description: 从【大乐透官网】拉取最新大乐透历史数据
    * @Param:  [issueCount]
    * @Return:  com.suns.lottery.tball.bean.HistoryData
    * @Author:  sunxiaobo
    * @Date:  14:43 2020/7/13
    **/
    private HistoryDataDlt historyData(String term){
        HttpHeaders headers  = new HttpHeaders();
        String html = httpInvoker.get("https://www.lottery.gov.cn/api/lottery_kj_detail_new.jspx?_ltype=4&_term="+ term,String.class,headers,new LinkedMultiValueMap<>());
        HistoryDataDlt response = JSONArray.parseArray(html,HistoryDataDlt.class).get(0);
        return response;
    }
}
