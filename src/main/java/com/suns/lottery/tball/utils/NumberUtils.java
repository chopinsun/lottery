package com.suns.lottery.tball.utils;

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

}
