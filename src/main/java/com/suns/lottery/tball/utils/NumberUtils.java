package com.suns.lottery.tball.utils;

import com.alibaba.fastjson.JSONArray;
import com.suns.lottery.tball.bean.Kv;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-14 19:32
 **/
public class NumberUtils {

    private static int N = 300;

    public static Integer parseInt(String num){
        return Integer.valueOf(num.replaceAll(",",""));
    }

    public static Long parseLong(String num){
        num = num.replaceAll("_","").replaceAll("-","").trim();
        if(StringUtils.isBlank(num)){
            return  null;
        }
        num = num.substring(0,num.indexOf(".")<=0?num.length():num.indexOf("."));
        return Long.valueOf(num.replaceAll(",",""));
    }

    public static Double parseDouble(String num){
        return Double.valueOf(num.replaceAll(",",""));
    }

    public static String toString(List<Integer> list){
        StringBuilder str = new StringBuilder();
        list.stream().forEach(x->str.append(x).append(","));
        str.deleteCharAt(str.lastIndexOf(","));
        return str.toString();
    }

    /**
    * @Description: 随机算法
     * 随机取其中[num]个数据返回
    * @Param:  [list]
    * @Return:  java.util.List<java.lang.Integer>
    * @Author:  sunxiaobo
    * @Date:  16:44 2020/7/15
    **/
    public static List<Integer> distinctRandom(List<Integer> list,int num){
        List<Integer> result = random(list,num).stream().distinct().collect(Collectors.toList());
        while (result.size()<num){
            result = Stream.of(result,random(list,1)).flatMap(x->x.stream()).distinct().collect(Collectors.toList());
        }
        return result;
    }


    /**
    * @Description: 随机最小加权算法
     * 按照权重，随机取其中[num]个返回，权重小的几率大
    * @Param:  [list]
    * @Return:  java.util.List<java.lang.Integer>
    * @Author:  sunxiaobo
    * @Date:  16:45 2020/7/15
    **/
    public static List<Integer> randomMinWeight(List<Kv<Integer,Integer>> list, Integer num){

        //反向加权
        List<Kv<Integer,Integer>> rList = getReverseWeight(list);
        List<Integer> nList= rList.stream().map(x-> IntStream.range(0,x.getValue()).mapToObj(i->x.getKey())).flatMap(x->x).collect(Collectors.toList());


        return distinctRandom(nList,num);
    }

    /**
     * @Description: 随机最大加权算法
     * 按照权重，随机取其中[num]个返回，权重大的几率大
     * @Param:  [list]
     * @Return:  java.util.List<java.lang.Integer>
     * @Author:  sunxiaobo
     * @Date:  16:45 2020/7/15
     **/
    public static List<Integer> randomMaxWeight(List<Kv<Integer,Integer>> list, Integer num){
        //加权
        List<Integer> nList= list.stream().map(x-> IntStream.range(0,x.getValue()).mapToObj(i->x.getKey())).flatMap(x->x).collect(Collectors.toList());
        return distinctRandom(nList,num);
    }


    private static List<Kv<Integer,Integer>> getReverseWeight(List<Kv<Integer,Integer>> m){
        List<Kv<Integer,Integer>> n = m.stream().map(x-> new Kv<Integer,Integer>(x.getKey(),x.getValue() * -1 +N)).collect(Collectors.toList());
        Optional<Integer> min = n.stream().map(x->x.getValue()).min(Comparator.comparingInt(Integer::intValue));
        if(min.isPresent() && min.get()<0){
            N+=100;
            return getReverseWeight(m);
        }else{
            return n;
        }
    }

    private static List<Integer> random(List<Integer> list,Integer num){
        return IntStream.range(0,num)
                .mapToObj(i->i)
                .map(i->list.get(RandomUtils.nextInt(0,list.size()-1)))
                .collect(Collectors.toList());
    }


}
