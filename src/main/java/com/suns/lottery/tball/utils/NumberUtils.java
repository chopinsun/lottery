package com.suns.lottery.tball.utils;

import java.util.List;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-14 19:32
 **/
public class NumberUtils {

    public static Integer parseInt(String num){
        return Integer.valueOf(num.replaceAll(",",""));
    }

    public static String toString(List<Integer> list){
        StringBuilder str = new StringBuilder();
        list.stream().forEach(x->str.append(x).append(","));
        str.deleteCharAt(str.lastIndexOf(","));
        return str.toString();
    }

}
