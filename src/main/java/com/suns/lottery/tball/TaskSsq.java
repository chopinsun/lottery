package com.suns.lottery.tball;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.*;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.mapper.SsqDetailMapper;
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
import java.util.List;

/**
 * Created by chopinsun on 2020/2/1.
 */
@Log4j2
@Component
public class TaskSsq {
    @Autowired
    private HttpInvoker httpInvoker;

    @Autowired
    private SsqMapper ssqMapper;
    @Autowired
    private SsqDetailMapper ssqDetailMapper;


    /**
     * @Description: 定时同步 双色球 最新数据
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
    @Scheduled(cron = "0 30 21 * * 2,4,7")
    public void upateSSqData(){
        log.info("------------启动定时任务，拉取最新一期数据-----------");
        //计算差几期数据没有同步
        String lastCode = remoteLastCode();
        String dbLastCode = dbLastCodeSsq();
        Integer num = Integer.valueOf(lastCode) - Integer.valueOf(dbLastCode);
        //按照计算结果，把差的数据同步回来
        HistoryData response = historyData(num);
        if(response.getState() ==0){
            List<LotteryResult> result = response.getResult();
            result.stream().forEach(x->{
                int exists = ssqMapper.countByCode(x.getCode());
                if(exists == 0){
                    log.info("是新数据，执行插入:{}",JSONObject.toJSONString(x));
                    Ssq ssq = x.convert();
                    ssqMapper.insert(ssq);
                    ssqDetailMapper.batchInsert(ssq.getSsqDetail());
                }
            });
        }
    }

    private String dbLastCodeSsq(){
        List<Ssq> histories = ssqMapper.history(1);
        return histories.get(0).getCode();
    }


    private String remoteLastCode(){
        HistoryData lastData = historyData(1);
        if(lastData.getState() ==0){
            List<LotteryResult> result = lastData.getResult();
            return result.get(0).getCode();
        }
        return null;
    }


    /**
    * @Description: 从【双色球官网】拉取双色球历史数据
    * @Param:  [issueCount]
    * @Return:  com.suns.lottery.tball.bean.HistoryData
    * @Author:  sunxiaobo
    * @Date:  14:43 2020/7/13
    **/
    private HistoryData historyData(Integer issueCount){
        HttpHeaders headers  = new HttpHeaders();
        headers.add("X-Requested-With","XMLHttpRequest");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        headers.add("Referer","http://www.cwl.gov.cn/kjxx/ssq/kjgg/");
        headers.add("Accept-Encoding","gzip, deflate");
        headers.add("Connection","keep-alive");
        headers.add("Accept","application/json, text/javascript, */*; q=0.01");
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> cookieList = new ArrayList<String>();
        cookieList.add("UniqueID=gaGBV37PZpk7jVh81578884419537");
        cookieList.add("Sites=_21");
        cookieList.add("_ga=GA1.3.1997877985.1578376727");
        cookieList.add("_Jo0OQK=548D51EE139C34EAF0A11CC98E01E2EC1B556DC1FD1C45ED67C60094C1A2D34AA4E9CD335BBE23D83187C1F1921FC381E7867DBC51FE6EFD7ED2DE98F4E2BF6F8E6F1B3C19C5B2FC5F8E6E66EDA7420CD4BE6E66EDA7420CD4B64AD0BB8F400A335D9A403BF43875897GJ1Z1IQ==");
        cookieList.add("_gat_gtag_UA_113065506_1=1");
        cookieList.add("21_vq=6");
        //"UniqueID=gaGBV37PZpk7jVh81578884419537; Sites=_21; _ga=GA1.3.1997877985.1578376727; _gid=GA1.3.1582248760.1578884420; _Jo0OQK=548D51EE139C34EAF0A11CC98E01E2EC1B556DC1FD1C45ED67C60094C1A2D34AA4E9CD335BBE23D83187C1F1921FC381E7867DBC51FE6EFD7ED2DE98F4E2BF6F8E6F1B3C19C5B2FC5F8E6E66EDA7420CD4BE6E66EDA7420CD4B64AD0BB8F400A335D9A403BF43875897GJ1Z1IQ==; _gat_gtag_UA_113065506_1=1; 21_vq=6"
        headers.put(HttpHeaders.COOKIE,cookieList);
        HistoryData response = httpInvoker.get("http://www.cwl.gov.cn/cwl_admin/kjxx/findDrawNotice?name=ssq&issueCount="+issueCount, HistoryData.class,headers,new LinkedMultiValueMap());
        log.info("数据拉取结果:{}",JSONObject.toJSONString(response));
        return response;
    }

}
