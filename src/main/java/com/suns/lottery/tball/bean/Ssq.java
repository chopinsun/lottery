package com.suns.lottery.tball.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ssq {
    private Integer id;

    private String code;

    private Integer r1;

    private Integer r2;

    private Integer r3;

    private Integer r4;

    private Integer r5;

    private Integer r6;

    private Integer b1;

    private String b2;

    private Integer sales;

    private Integer poolmoney;

    private String content;

    private String type1Num;

    private String type1Money;

    private String type2Num;

    private String type2Money;

    private String type3Num;

    private String type3Money;

    private String type4Num;

    private String type4Money;

    private String type5Num;

    private String type5Money;

    private String type6Num;

    private String type6Money;

    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lotteryDate;

}