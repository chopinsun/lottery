package com.suns.lottery.tball;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.Dlt;
import com.suns.lottery.tball.bean.DltResult;
import com.suns.lottery.tball.bean.Ssq;
import com.suns.lottery.tball.common.Constants;
import com.suns.lottery.tball.common.Sort;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.mapper.SsqMapper;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private SsqMapper ssqMapper;

    @Autowired
    private DltMapper dltMapper;

    @Autowired
    private RestTemplate template;


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
            List<Ssq> list = new ArrayList<>();
            //遍历所有的a标签
            for (Element link : content) {
                Elements tds = link.children();
                Ssq item = Ssq.builder()
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
            ssqMapper.batchInsert(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ResponseEntity<String> response = template.exchange("http://datachart.500.com/ssq/history/newinc/history.php?start=03001&end=20005", HttpMethod.GET,new HttpEntity<>(new LinkedMultiValueMap(),headers),String.class,new HashMap<>(2));
//        log.info(response.getBody());
    }


    public void pullAllDLTData(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<Dlt> list = new ArrayList<>();
        IntStream.range(1,131).forEach(n->{
            MultiValueMap<String, Object> body= new LinkedMultiValueMap<>();
            body.add("current_page",n);
            body.add("all_count",1946);
            try {
                JSONObject response = httpInvoker.post("https://www.js-lottery.com/PlayZone/ajaxLottoData?current_page={}&all_count={}", JSONObject.class,new HttpEntity(body,headers),new LinkedMultiValueMap());
                if(response!=null && response.getBoolean("result")){
                    JSONArray items = response.getJSONArray("items");
                    log.info(items.toJSONString());
                    for (int i = 0; i < items.size(); i++) {
                        DltResult r = items.getObject(i,DltResult.class);
                        list.add(r.convert());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dltMapper.batchInsert(list);
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
            int n = ssqMapper.countByRedNum(i);
            map.put(i,n);
        });
        log.info(map);
        Set<Map.Entry<Integer,Integer>> entrys = map.entrySet();
        log.info("红球-size:{}",entrys.size());
        log.info("红球-原始数据:{}",JSONObject.toJSONString(map));
        log.info("红球-总排序:{}",JSONArray.toJSONString(entrys.stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList())));
        List<Integer> redBalls = entrys.stream().sorted(Map.Entry.comparingByValue()).limit(Constants.REDBALLLIMIT).map(n->n.getKey()).collect(Collectors.toList());
        log.info("红球-前{}位:{}",Constants.REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Integer> blueBallsResult = ssqMapper.countByBlueNum(Sort.ASC.getCode());
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
            int n = ssqMapper.countByRedNum(i);
            map.put(i,n);
        });
        log.info(map);
        Set<Map.Entry<Integer,Integer>> entrys = map.entrySet();
        log.info("红球-size:{}",entrys.size());
        log.info("红球-原始数据:{}",JSONObject.toJSONString(map));
        List<Integer> redBalls = entrys.stream().sorted(Map.Entry.comparingByValue()).map(n->n.getKey()).collect(Collectors.toList());
        log.info("红球-总排序:{}",JSONArray.toJSONString(redBalls));
        List<Integer> blueBalls = ssqMapper.countByBlueNum(Sort.ASC.getCode());
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
            if(ssqMapper.exists(balls.get(0),balls.get(1),balls.get(2),balls.get(3),balls.get(4),balls.get(5))==0){
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
            int n = ssqMapper.countByRedNum(i);
            map.put("ball",i);
            map.put("cnt",n);
            redBallsResult.add(map);
        });
        log.info("红球-原始数据:{}",JSONArray.toJSONString(redBallsResult));

        List<Map<String,Integer>> blueBallsResult = ssqMapper.countByBlueNum2();
        log.info("蓝球-原始数据:{}",JSONArray.toJSONString(blueBallsResult));
        return redBallsResult;
    }

    public List<Map<String,Integer>> blueBallCount(){
        List<Map<String,Integer>> blueBallsResult = ssqMapper.countByBlueNum2();
        log.info("蓝球-原始数据:{}",JSONArray.toJSONString(blueBallsResult));
        return blueBallsResult;
    }


    public List<Ssq> history(int n){
        List<Ssq> list = ssqMapper.history(n);
        return list;
    }

}
