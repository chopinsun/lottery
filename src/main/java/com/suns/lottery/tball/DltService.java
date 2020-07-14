package com.suns.lottery.tball;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.Dlt;

import com.suns.lottery.tball.bean.DltDetail;
import com.suns.lottery.tball.bean.SsqDetail;
import com.suns.lottery.tball.common.Constants;
import com.suns.lottery.tball.common.Sort;
import com.suns.lottery.tball.mapper.DltDetailMapper;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.utils.HttpInvoker;
import com.suns.lottery.tball.utils.NumberUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

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
public class DltService {
    @Autowired
    private HttpInvoker httpInvoker;
    @Autowired
    private DltMapper dltMapper;
    @Autowired
    private DltDetailMapper dltDetailMapper;

    /**
     * @Description: 一次性拉取大乐透所有历史数据
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
            Document doc = Jsoup.connect("http://datachart.500.com/dlt/history/newinc/history.php?start=07002&end=20062").get();
            Elements content = doc.getElementById("tdata").children();//获取id为tdata的tbody下的tr
            List<Dlt> list = new ArrayList<>();
            //遍历所有的a标签
            for (Element link : content) {
                Elements tds = link.children();
                Dlt item = Dlt.builder()
                        .code(tds.get(0).text())
                        .r1(Integer.valueOf(tds.get(1).text()))
                        .r2(Integer.valueOf(tds.get(2).text()))
                        .r3(Integer.valueOf(tds.get(3).text()))
                        .r4(Integer.valueOf(tds.get(4).text()))
                        .r5(Integer.valueOf(tds.get(5).text()))
                        .b1(Integer.valueOf(tds.get(6).text()))
                        .b2(Integer.valueOf(tds.get(7).text()))
                        .poolmoney(NumberUtils.parseLong(tds.get(8).text()))
                        .sales(NumberUtils.parseLong(tds.get(13).text()))
                        .lotteryDate(Date.from(LocalDate.parse(tds.get(14).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                        .build();
                DltDetail d1 = DltDetail.builder()
                        .code(tds.get(0).text())
                        .level(1)
                        .levelName("一等奖")
                        .num(NumberUtils.parseInt(tds.get(9).text()))
                        .money(NumberUtils.parseInt(tds.get(10).text()))
                        .allMoney(NumberUtils.parseInt(tds.get(9).text()) * Long.valueOf(NumberUtils.parseInt(tds.get(10).text())))
                        .build();

                DltDetail d2 = DltDetail.builder()
                        .code(tds.get(0).text())
                        .level(2)
                        .levelName("二等奖")
                        .num(NumberUtils.parseInt(tds.get(11).text()))
                        .money(NumberUtils.parseInt(tds.get(12).text()))
                        .allMoney(NumberUtils.parseInt(tds.get(11).text()) * Long.valueOf(NumberUtils.parseInt(tds.get(12).text())))
                        .build();
                list.add(item);
                dltDetailMapper.batchInsert(Arrays.asList(d1,d2));
            }
            dltMapper.batchInsert(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            int n = dltMapper.countByRedNum(i);
            map.put(i,n);
        });
        log.info(map);
        Set<Map.Entry<Integer,Integer>> entrys = map.entrySet();
        log.info("红球-size:{}",entrys.size());
        log.info("红球-原始数据:{}",JSONObject.toJSONString(map));
        log.info("红球-总排序:{}",JSONArray.toJSONString(entrys.stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList())));
        List<Integer> redBalls = entrys.stream().sorted(Map.Entry.comparingByValue()).limit(Constants.REDBALLLIMIT).map(n->n.getKey()).collect(Collectors.toList());
        log.info("红球-前{}位:{}",Constants.REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Integer> blueBallsResult = dltMapper.countByBlue1Num(Sort.ASC.getCode());
//        List<Integer> b2BallsResult = dltMapper.countByBlue2Num(Sort.ASC.getCode());
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
            int n = dltMapper.countByRedNum(i);
            map.put(i,n);
        });
        log.info(map);
        Set<Map.Entry<Integer,Integer>> entrys = map.entrySet();
        log.info("红球-size:{}",entrys.size());
        log.info("红球-原始数据:{}",JSONObject.toJSONString(map));
        List<Integer> redBalls = entrys.stream().sorted(Map.Entry.comparingByValue()).map(n->n.getKey()).collect(Collectors.toList());
        log.info("红球-总排序:{}",JSONArray.toJSONString(redBalls));
        List<Integer> blueBalls = dltMapper.countByBlue1Num(Sort.ASC.getCode());
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
            if(dltMapper.exists(balls.get(0),balls.get(1),balls.get(2),balls.get(3),balls.get(4),balls.get(5))==0){
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
            int n = dltMapper.countByRedNum(i);
            map.put("ball",i);
            map.put("cnt",n);
            redBallsResult.add(map);
        });
        log.info("红球-原始数据:{}",JSONArray.toJSONString(redBallsResult));

        List<Map<String,Integer>> blueBallsResult = dltMapper.groupByBlueNum();
        log.info("蓝球-原始数据:{}",JSONArray.toJSONString(blueBallsResult));
        return redBallsResult;
    }

    public List<Map<String,Integer>> blueBallCount(){
        List<Map<String,Integer>> blueBallsResult = dltMapper.groupByBlueNum();
        log.info("蓝球-原始数据:{}",JSONArray.toJSONString(blueBallsResult));
        return blueBallsResult;
    }


    public List<Dlt> history(int n){
        List<Dlt> list = dltMapper.history(n);
        return list;
    }

}
