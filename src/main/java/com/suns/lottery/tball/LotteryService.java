package com.suns.lottery.tball;

import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.HistoryData;
import com.suns.lottery.tball.bean.Lottery;
import com.suns.lottery.tball.bean.LotteryResult;
import com.suns.lottery.tball.mapper.LotteryMapper;
import com.suns.lottery.tball.utils.HttpInvoker;
import com.suns.lottery.tball.utils.NumberUtils;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: lottery-tball
 * @description: 乐透
 * @author: sunxiaobo
 * @create: 2020-01-13 19:37
 **/
@Log4j2
@Component
public class LotteryService {
    @Autowired
    private HttpInvoker httpInvoker;
    @Autowired
    private LotteryMapper lotteryMapper;

    @Autowired
    private RestTemplate template;

    /**
     * @Description: 定时同步最新数据
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
    public JSONObject upateData(int rowNum){
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
        HistoryData response = httpInvoker.get("http://www.cwl.gov.cn/cwl_admin/kjxx/findDrawNotice?name=ssq&issueCount="+rowNum, HistoryData.class,headers,new LinkedMultiValueMap());
        log.info(JSONObject.toJSONString(response));
        if(response.getState() ==0){
            List<LotteryResult> result = response.getResult();
            log.info("-----------------------------");
            log.info("查询到{}条",result.size());
            List<Lottery> list = result.stream().map(x->x.convert()).collect(Collectors.toList());
            lotteryMapper.batchInsert(list);
        }
        return JSONObject.parseObject(JSONObject.toJSONString(response));
    }

    /**
     * @Description: 一次性拉取所有历史数据
     * @param null
     * @return:
     *
     * @Creator: sunxiaobo
     * @Date: 2020/1/14 19:47
     *
     * @Modify: sunxiaobo
     * @Date: 2020/1/14 19:47
     *
     **/
    public void pullAllData(){
        HttpHeaders headers  = new HttpHeaders();

        try {
            Document doc = Jsoup.connect("http://datachart.500.com/ssq/history/newinc/history.php?start=03001&end=20005").get();
            Elements content = doc.getElementsByClass("t_tr1");//获取id为content的dom节点
            List<Lottery> list = new ArrayList<>();
            //遍历所有的a标签
            for (Element link : content) {
                Elements tds = link.children();
                Lottery item = Lottery.builder()
                        .code(tds.get(0).text())
                        .r1(Integer.valueOf(tds.get(1).text()))
                        .r2(Integer.valueOf(tds.get(2).text()))
                        .r3(Integer.valueOf(tds.get(3).text()))
                        .r4(Integer.valueOf(tds.get(4).text()))
                        .r5(Integer.valueOf(tds.get(5).text()))
                        .r6(Integer.valueOf(tds.get(6).text()))
                        .b1(Integer.valueOf(tds.get(7).text()))
                        .poolmoney(NumberUtils.parseInt(tds.get(9).text()))
                        .type1Num(NumberUtils.parseInt(tds.get(10).text()))
                        .type1Money(NumberUtils.parseInt(tds.get(11).text()))
                        .type2Num(NumberUtils.parseInt(tds.get(12).text()))
                        .type2Money(NumberUtils.parseInt(tds.get(13).text()))
                        .sales(NumberUtils.parseInt(tds.get(14).text()))
                        .lotteryDate(Date.from(LocalDate.parse(tds.get(15).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                        .build();
                list.add(item);
            }
            lotteryMapper.batchInsert(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ResponseEntity<String> response = template.exchange("http://datachart.500.com/ssq/history/newinc/history.php?start=03001&end=20005", HttpMethod.GET,new HttpEntity<>(new LinkedMultiValueMap(),headers),String.class,new HashMap<>(2));
//        log.info(response.getBody());
    }
    /**
     * @Description: 最少出现的数字生成号码
     * @param null
     * @return:
     *
     * @Creator: sunxiaobo
     * @Date: 2020/1/14 19:48
     *
     * @Modify: sunxiaobo
     * @Date: 2020/1/14 19:48
     *
     **/
    public void generateNumByMin(){
        /**
         *红球共33个
         *每个数字查一遍，统计最少出现的12个数字
         *随机取6个数字出来，组成号码
         * 将号码按照从小到大排序，便于观看
         * 篮球共16个
         * 篮球取最少出现的3个球，随机取一个
         **/
        for (int i = 1; i <= 33; i++) {
            int n = lotteryMapper.countByNum(i);
        }





    }

    /**
     * @Description: 最常出现的数字生成号码
     * @param null
     * @return:
     *
     * @Creator: sunxiaobo
     * @Date: 2020/1/14 19:49
     *
     * @Modify: sunxiaobo
     * @Date: 2020/1/14 19:49
     *
     **/
    public void generateNumByMax(){

    }
}
