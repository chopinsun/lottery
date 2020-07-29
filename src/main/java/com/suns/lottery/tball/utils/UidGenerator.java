package com.suns.lottery.tball.utils;

import java.util.UUID;

/**
 * @program: recordcenter
 * @description: 序号生成工具
 * @author: chopin.sun
 * @create: 2019-06-11 16:11
 **/

public class UidGenerator{
    private static final Object lock =new Object();

    /**
     * 生成不重复的序列号
     * @return
     */
    public static Long nextId(){
        return Long.parseLong(String.valueOf(System.currentTimeMillis()) + randomNum());
    }

    private static int randomNum(){
        int value =UUID.randomUUID().hashCode();
        synchronized (lock){
            value = (value ^ (value >> 31)) - (value >> 31) >> 16;
            while(value<10000){
                value = (value ^ (1<<8)) <<1;
            }
        }
        return value;
    }

}
