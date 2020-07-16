package com.suns.lottery.tball.service;

import com.alibaba.fastjson.JSONArray;
import com.suns.lottery.tball.bean.Dlt;

import com.suns.lottery.tball.bean.DltDetail;
import com.suns.lottery.tball.bean.Kv;
import com.suns.lottery.tball.mapper.DltDetailMapper;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.mapper.LibMapper;
import com.suns.lottery.tball.utils.Cache;
import com.suns.lottery.tball.utils.NumberUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Stream;

import static com.suns.lottery.tball.common.Constants.REDBALLLIMIT;

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
    private Cache cache;
    @Autowired
    private DltMapper dltMapper;
    @Autowired
    private DltDetailMapper dltDetailMapper;
    @Autowired
    private LibMapper libMapper;

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
            Document doc = Jsoup.connect("http://datachart.500.com/dlt/history/newinc/history.php?start=07001&end=19113").get();
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
         *红球共35个
         *每个数字查一遍，统计最少出现的12个数字
         *随机取6个数字出来，组成号码
         * 将号码按照从小到大排序，便于观看
         * 篮球共12个
         * 篮球取最少出现的3个球，随机取一个
         **/
        List<Kv<Integer,Integer>> redBalls = getSortedRedBalls();
        log.info("红球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Kv<Integer,Integer>> blueBalls = getSortedBlueBalls();
        log.info("蓝球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(blueBalls));

        while (result.size()<num){
            List<Integer> reds = NumberUtils.randomMinWeight(redBalls,5).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            if(historyExists(reds)){
                continue;
            }
            List<Integer> blues = NumberUtils.randomMinWeight(blueBalls,2).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            result.add(Stream.of(reds,blues).flatMap(x->x.stream()).collect(Collectors.toList()));
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
    public Set<List<Integer>> generateNumByMax(int num){
        Set<List<Integer>> result = new HashSet<>(num);
        /**
         *红球共35个
         *每个数字查一遍，统计最少出现的12个数字
         *随机取6个数字出来，组成号码
         * 将号码按照从小到大排序，便于观看
         * 篮球共12个
         * 篮球取最少出现的3个球，随机取一个
         **/
        List<Kv<Integer,Integer>> redBalls = getSortedRedBalls();
        log.info("红球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Kv<Integer,Integer>> blueBalls = getSortedBlueBalls();
        log.info("蓝球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(blueBalls));

        while (result.size()<num){
            List<Integer> reds = NumberUtils.randomMaxWeight(redBalls,5).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            if(historyExists(reds)){
                continue;
            }
            List<Integer> blues = NumberUtils.randomMaxWeight(blueBalls,2).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            result.add(Stream.of(reds,blues).flatMap(x->x.stream()).collect(Collectors.toList()));
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
    public Set<List<Integer>> generateNumByMix(int num){
        Set<List<Integer>> result = new HashSet<>(num);
        /**
         *红球：
         *最少出现的13个数字，取3个
         *出现次数居于16-28之间的，取1个
         *最多出现的5个数字，取1个
         * 将号码按照从小到大排序，便于观看
         * 蓝球：
         * 随机取一个
         **/
        List<Kv<Integer,Integer>> redBalls = getSortedRedBalls();
        log.info("红球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Kv<Integer,Integer>> blueBalls = getSortedBlueBalls();
        log.info("蓝球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(blueBalls));

        while (result.size()<num){
            //最少出现
            List<Integer> redmins = NumberUtils.distinctRandom(redBalls.stream().sorted(Comparator.comparing(Kv::getValue)).limit(13).map(x->x.getKey()).collect(Collectors.toList()),3);
            //居中出现
            List<Integer> redmids = NumberUtils.distinctRandom(IntStream.range(0,redBalls.size()-1).mapToObj(i->i).map(i->i>15 && i< 28?redBalls.get(i).getKey():-1).filter(i->i!=-1).collect(Collectors.toList()),1);
            //最多出现
            List<Integer> redmaxs = NumberUtils.distinctRandom(redBalls.stream().sorted(Comparator.comparingInt(Kv<Integer,Integer>::getValue).reversed()).limit(5).map(x->x.getKey()).collect(Collectors.toList()),1);
            List<Integer> reds = Stream.of(redmins,redmids,redmaxs).flatMap(x->x.stream()).sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());

            if(historyExists(reds)){
                continue;
            }
            List<Integer> blues = NumberUtils.distinctRandom(blueBalls.stream().map(x->x.getKey()).collect(Collectors.toList()),1);
            result.add(Stream.of(reds,blues).flatMap(x->x.stream()).collect(Collectors.toList()));
        }
        return result;
    }


    public void saveToLib(Set<List<Integer>> list,String type){
        log.info("saveToLib :{}-{}",type,list.size());
        List<String> save = list.stream().map(x-> StringUtils.join(x,",")).collect(Collectors.toList());
        libMapper.saveAll(save,type);
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


    private List<Kv<Integer,Integer>> getSortedRedBalls(){
        return IntStream.range(1, 35).mapToObj(i->i).map(i->new Kv<Integer,Integer>(i,dltMapper.countByRedNum(i))).collect(Collectors.toList());
    }


    private List<Kv<Integer,Integer>> getSortedBlueBalls(){
        return IntStream.range(1, 12).mapToObj(i->i).map(i->new Kv<Integer,Integer>(i,dltMapper.countByBlueNum(i))).collect(Collectors.toList());
    }

    private Boolean historyExists(List<Integer> balls){
        List<Dlt> ssqList =(List<Dlt>) cache.get(Cache.Type.SSQ);
        Optional<Dlt> exists= ssqList.stream().filter(x->balls.get(0).equals(x.getR1())
                && balls.get(1).equals(x.getR2())
                && balls.get(3).equals(x.getR3())
                && balls.get(4).equals(x.getR5())
                && balls.get(5).equals(x.getR6())).findAny();
        return exists.isPresent();
    }
}
