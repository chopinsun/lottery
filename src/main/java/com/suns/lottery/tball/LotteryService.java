package com.suns.lottery.tball;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.HistoryData;
import com.suns.lottery.tball.bean.Lottery;
import com.suns.lottery.tball.bean.LotteryResult;
import com.suns.lottery.tball.common.Constants;
import com.suns.lottery.tball.common.Sort;
import com.suns.lottery.tball.mapper.LotteryMapper;
import com.suns.lottery.tball.utils.HttpInvoker;
import com.suns.lottery.tball.utils.NumberUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    public Set<List<Integer>> generateNumByMin(int num){
        Set<List<Integer>> result = new HashSet<>(num);
        /**
         *红球共33个
         *每个数字查一遍，统计最少出现的12个数字
         *随机取6个数字出来，组成号码
         * 将号码按照从小到大排序，便于观看
         * 篮球共16个
         * 篮球取最少出现的3个球，随机取一个
         **/
        Map<Integer,Integer> map = new HashMap<>();
        IntStream.range(1, 34).forEach(i->{
            int n = lotteryMapper.countByRedNum(i);
            map.put(i,n);
        });
        log.info(map);
        Set<Map.Entry<Integer,Integer>> entrys = map.entrySet();
        log.info("红球-size:{}",entrys.size());
        log.info("红球-原始数据:{}",JSONObject.toJSONString(map));
        log.info("红球-总排序:{}",JSONArray.toJSONString(entrys.stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList())));
        List<Integer> redBalls = entrys.stream().sorted(Map.Entry.comparingByValue()).limit(Constants.REDBALLLIMIT).map(n->n.getKey()).collect(Collectors.toList());
        log.info("红球-前{}位:{}",Constants.REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Integer> blueBallsResult = lotteryMapper.countByBlueNum(Sort.ASC.getCode());
        log.info("蓝球-总排序:{}",JSONArray.toJSONString(blueBallsResult));
        List<Integer> blueBalls = blueBallsResult.stream().limit(Constants.BLUEBALLLIMIT).collect(Collectors.toList());
        log.info("蓝球-前{}位:{}",JSONArray.toJSONString(blueBalls));

        while (result.size()<num){
            Random rand = new Random();
            Set<Integer> rbs = new HashSet<>();
            while(rbs.size()<6){
                rbs.add(redBalls.get(rand.nextInt(Constants.REDBALLLIMIT)));
            }
            List<Integer> balls = rbs.stream().sorted(Comparator.comparingInt(o->o)).collect(Collectors.toList());
            balls.add(blueBalls.get(rand.nextInt(Constants.BLUEBALLLIMIT)));
            result.add(balls);
        }
        return result;
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
    public List<String> generateNumByMax(int num){
        return null;
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
    public Set<List<Integer>> generateNumByMix(int num){
        Set<List<Integer>> result = new HashSet<>(num);
        /**
         *红球：
         *最少出现的15个数字，取4个
         *出现次数居于16-28之间的，取1个
         *最多出现的5个数字，取1个
         * 将号码按照从小到大排序，便于观看
         * 蓝球：
         * 随机取一个
         **/

        Map<Integer,Integer> map = new HashMap<>();
        IntStream.range(1, 34).forEach(i->{
            int n = lotteryMapper.countByRedNum(i);
            map.put(i,n);
        });
        log.info(map);
        Set<Map.Entry<Integer,Integer>> entrys = map.entrySet();
        log.info("红球-size:{}",entrys.size());
        log.info("红球-原始数据:{}",JSONObject.toJSONString(map));
        List<Integer> redBalls = entrys.stream().sorted(Map.Entry.comparingByValue()).map(n->n.getKey()).collect(Collectors.toList());
        log.info("红球-总排序:{}",Constants.REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Integer> blueBalls = lotteryMapper.countByBlueNum(Sort.ASC.getCode());
        log.info("蓝球-总排序:{}",JSONArray.toJSONString(blueBalls));

        while(result.size()<num){
            Set<Integer> rbs = new HashSet<>();
            //前15个取4个
            while(rbs.size()<4){
                rbs.add(redBalls.get(RandomUtils.nextInt(1,Constants.REDBALLLIMIT)));
            }
            //中间取1个
            while(rbs.size()<5){
                rbs.add(redBalls.get(RandomUtils.nextInt(Constants.REDBALLLIMIT_MID[0],Constants.REDBALLLIMIT_MID[1])));
            }
            //后5个取1个
            while(rbs.size()<6){
                rbs.add(redBalls.get(RandomUtils.nextInt(Constants.REDBALLLIMIT_MID[1],33)));
            }
            List<Integer> balls = rbs.stream().sorted(Comparator.comparingInt(o->o)).collect(Collectors.toList());
            //排除历史数据
            if(lotteryMapper.exists(balls.get(0),balls.get(1),balls.get(2),balls.get(3),balls.get(4),balls.get(5))==0){
                balls.add(blueBalls.get(RandomUtils.nextInt(1,16)));
                result.add(balls);
            }
        };
        return result;
    }

    public List<Map<String,Integer>> redBallCount(){
        List<Map<String,Integer>> redBallsResult= new ArrayList<>();
        IntStream.range(1, 34).forEach(i->{
            Map<String,Integer> map = new HashMap<>();
            int n = lotteryMapper.countByRedNum(i);
            map.put("ball",i);
            map.put("cnt",n);
            redBallsResult.add(map);
        });
        log.info("红球-原始数据:{}",JSONArray.toJSONString(redBallsResult));

        List<Map<String,Integer>> blueBallsResult = lotteryMapper.countByBlueNum2();
        log.info("蓝球-原始数据:{}",JSONArray.toJSONString(blueBallsResult));
        return redBallsResult;
    }

    public List<Map<String,Integer>> blueBallCount(){
        List<Map<String,Integer>> blueBallsResult = lotteryMapper.countByBlueNum2();
        log.info("蓝球-原始数据:{}",JSONArray.toJSONString(blueBallsResult));
        return blueBallsResult;
    }


    public List<Lottery> history(int n){
        List<Lottery> list = lotteryMapper.history(n);
        return list;
    }



}
