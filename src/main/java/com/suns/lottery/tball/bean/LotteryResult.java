package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @title: lottery-tball
 * @description: 中奖结果
 * @author: sunxiaobo
 * @create: 2020-01-13 19:42
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LotteryResult {
    private String name;
    private String code;
    private String detailsLink;
    private String videoLink;
    private String date;
    private String week;
    private String red;
    private String blue;
    private String blue2;
    private String sales;
    private String poolmoney;
    private String content;
    private String addmoney;
    private String addmoney2;
    private String msg;
    private String z2add;
    private String m2add;
    private List<Prizegrade> prizegrades;


    public Ssq convert(){
        Ssq lottery = new Ssq();
        lottery.setCode(this.code);
        lottery.setLotteryDate(Date.from(LocalDate.parse(this.date.substring(0,10),DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        String[] red = this.red.split(",");
        lottery.setR1(Integer.valueOf(red[0]));
        lottery.setR2(Integer.valueOf(red[1]));
        lottery.setR3(Integer.valueOf(red[2]));
        lottery.setR4(Integer.valueOf(red[3]));
        lottery.setR5(Integer.valueOf(red[4]));
        lottery.setR6(Integer.valueOf(red[5]));
        lottery.setB1(Integer.valueOf(this.blue));
        lottery.setPoolmoney(Integer.valueOf(this.poolmoney));
        lottery.setContent(this.content);
        lottery.setSales(Integer.valueOf(this.sales));
        List<Prizegrade> prizegrades = this.prizegrades;
        lottery.setType1Money(prizegrades.get(0).getTypemoney());
        lottery.setType1Num(prizegrades.get(0).getTypenum());
        lottery.setType2Money(prizegrades.get(1).getTypemoney());
        lottery.setType2Num(prizegrades.get(1).getTypenum());
        lottery.setType3Money(prizegrades.get(2).getTypemoney());
        lottery.setType3Num(prizegrades.get(2).getTypenum());
        if(prizegrades.size()>3){
            lottery.setType4Money(prizegrades.get(4).getTypemoney());
            lottery.setType4Num(prizegrades.get(4).getTypenum());
        }
        if(prizegrades.size()>4){
            lottery.setType4Money(prizegrades.get(5).getTypemoney());
            lottery.setType4Num(prizegrades.get(5).getTypenum());
        }
        if(prizegrades.size()>5){
            lottery.setType4Money(prizegrades.get(6).getTypemoney());
            lottery.setType4Num(prizegrades.get(6).getTypenum());
        }

        return lottery;
    }

}
