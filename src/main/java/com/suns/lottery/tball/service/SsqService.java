package com.suns.lottery.tball.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suns.lottery.tball.bean.*;
import com.suns.lottery.tball.common.Constants;
import com.suns.lottery.tball.common.Sort;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.mapper.LibMapper;
import com.suns.lottery.tball.mapper.SsqDetailMapper;
import com.suns.lottery.tball.mapper.SsqMapper;
import com.suns.lottery.tball.service.filter.SsqFilter;
import com.suns.lottery.tball.utils.Cache;
import com.suns.lottery.tball.utils.HttpInvoker;
import com.suns.lottery.tball.utils.NumberUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
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
public class SsqService {
    @Autowired
    private Cache cache;
    @Autowired
    private SsqMapper ssqMapper;
    @Autowired
    private SsqDetailMapper ssqDetailMapper;
    @Autowired
    private LibMapper libMapper;
    @Autowired
    private SsqFilter ssqFilter;
    @Autowired
    private RestTemplate template;


    /**
     * @Description: 一次性拉取双色球所有历史数据
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
            Document doc = Jsoup.connect("http://datachart.500.com/ssq/history/newinc/history.php?start=03001&end=1911519115").get();
            Elements content = doc.getElementById("tdata").children();//获取id为content的dom节点
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
                        .poolmoney(NumberUtils.parseLong(tds.get(9).text()))
                        .sales(NumberUtils.parseLong(tds.get(14).text()))
                        .lotteryDate(Date.from(LocalDate.parse(tds.get(15).text(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                        .build();
                SsqDetail d1 = SsqDetail.builder()
                        .code(tds.get(0).text())
                        .level(1)
                        .levelName("一等奖")
                        .num(NumberUtils.parseInt(tds.get(10).text()))
                        .money(NumberUtils.parseInt(tds.get(11).text()))
                        .allMoney(NumberUtils.parseInt(tds.get(10).text()) * Long.valueOf(NumberUtils.parseInt(tds.get(11).text())))
                        .build();

                SsqDetail d2 = SsqDetail.builder()
                        .code(tds.get(0).text())
                        .level(2)
                        .levelName("二等奖")
                        .num(NumberUtils.parseInt(tds.get(12).text()))
                        .money(NumberUtils.parseInt(tds.get(13).text()))
                        .allMoney(NumberUtils.parseInt(tds.get(12).text()) * Long.valueOf(NumberUtils.parseInt(tds.get(13).text())))
                        .build();
                list.add(item);
                ssqDetailMapper.batchInsert(Arrays.asList(d1,d2));
            }
            ssqMapper.batchInsert(list);

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
         *每个数字查一遍，随机权重取6个
         * 将号码按照从小到大排序，便于观看
         * 篮球共16个
         * 随机权重取1个
         **/
        List<Kv<Integer,Integer>> redBalls = getSortedRedBalls();
        log.info("红球:{}",JSONArray.toJSONString(redBalls));
        List<Kv<Integer,Integer>> blueBalls = getSortedBlueBalls();
        log.info("蓝球:{}",JSONArray.toJSONString(blueBalls));
        while (result.size()<num){
            List<Integer> reds = NumberUtils.randomMinWeight(redBalls,6).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            if(historyExists(reds)){
                continue;
            }
            List<Integer> blues = NumberUtils.randomMinWeight(blueBalls,1).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
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
         *红球共33个
         *每个数字查一遍，随机权重取6个
         * 将号码按照从小到大排序，便于观看
         * 篮球共16个
         * 随机权重取1个
         **/
        List<Kv<Integer,Integer>> redBalls = getSortedRedBalls();
        log.info("红球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(redBalls));
        List<Kv<Integer,Integer>> blueBalls = getSortedBlueBalls();
        log.info("蓝球-前{}位:{}", REDBALLLIMIT,JSONArray.toJSONString(blueBalls));

        while (result.size()<num){
            List<Integer> reds = NumberUtils.randomMaxWeight(redBalls,6).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
            if(historyExists(reds)){
                continue;
            }
            List<Integer> blues = NumberUtils.randomMaxWeight(blueBalls,1).stream().sorted(Comparator.comparingInt(Integer::intValue)).collect(Collectors.toList());
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
         *最少出现的15个数字，取4个
         *出现次数居于16-28之间的，取1个
         *最多出现的6个数字，取1个
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
            List<Integer> redmins = NumberUtils.distinctRandom(redBalls.stream().sorted(Comparator.comparing(Kv::getValue)).limit(15).map(x->x.getKey()).collect(Collectors.toList()),4);
            //居中出现
            List<Integer> redmids = NumberUtils.distinctRandom(IntStream.range(0,redBalls.size()-1).mapToObj(i->i).map(i->i>15 && i< 28?redBalls.get(i).getKey():-1).filter(i->i!=-1).collect(Collectors.toList()),1);
            //最多出现
            List<Integer> redmaxs = NumberUtils.distinctRandom(redBalls.stream().sorted(Comparator.comparingInt(Kv<Integer,Integer>::getValue).reversed()).limit(6).map(x->x.getKey()).collect(Collectors.toList()),1);
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
        List<String> save = list.stream().map(x->StringUtils.join(x,",")).collect(Collectors.toList());
        libMapper.saveAll(save,type);
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


    private List<Kv<Integer,Integer>> getSortedRedBalls(){
        return IntStream.range(1, 34).mapToObj(i->i).map(i->new Kv<Integer,Integer>(i,ssqMapper.countByRedNum(i))).collect(Collectors.toList());
    }


    private List<Kv<Integer,Integer>> getSortedBlueBalls(){
        return IntStream.range(1, 17).mapToObj(i->i).map(i->new Kv<Integer,Integer>(i,ssqMapper.countByBlueNum(i))).collect(Collectors.toList());
    }

    private Boolean historyExists(List<Integer> balls){
        List<Ssq> ssqList =(List<Ssq>) cache.get(Cache.Type.SSQ);
        Optional<Ssq> exists= ssqList.stream().filter(x->balls.get(0).equals(x.getR1())
                && balls.get(1).equals(x.getR2())
                && balls.get(3).equals(x.getR3())
                && balls.get(4).equals(x.getR5())
                && balls.get(5).equals(x.getR6())).findAny();
        return exists.isPresent();
    }

}
