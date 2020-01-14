package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lottery {
    private Integer id;

    private String code;

    private Integer r1;

    private Integer r2;

    private Integer r3;

    private Integer r4;

    private Integer r5;

    private Integer r6;

    private Integer b1;

    private Integer b2;

    private Integer sales;

    private Integer poolmoney;

    private String content;

    private Integer type1Num;

    private Integer type1Money;

    private Integer type2Num;

    private Integer type2Money;

    private Integer type3Num;

    private Integer type3Money;

    private Date lotteryDate;

}