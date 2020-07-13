package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-13 15:06
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DltDetail {

    private String drawNews;
    private String fTime;
    private String isAP;
    private String ispool;
    private String lType;
    private String numSequence;
    private String number;
    private String number_pool;
    private OpenTime openTime;
    private String openTime_fmt;
    private String openTime_fmt1;
    private String pool;
    private String sTime;
    private Integer status;
    private String term;
    private String totalSales;
    private Integer verify;

    @Data
    @NoArgsConstructor
    public class OpenTime{
        private Integer date;
        private Integer day;
        private Integer hours;
        private Integer month;
        private Integer nanos;
        private Integer seconds;
        private Long time;
        private Integer timezoneOffset;
        private Integer year;
    }

}
