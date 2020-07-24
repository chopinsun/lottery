package com.suns.lottery.tball.bean;

import com.suns.lottery.tball.utils.NumberUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        String[] red = this.red.split(",");
        Ssq lottery = Ssq.builder()
                .code(this.code)
                .lotteryDate(Date.from(LocalDate.parse(this.date.substring(0,10),DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                .r1(Integer.valueOf(red[0]))
                .r2(Integer.valueOf(red[1]))
                .r3(Integer.valueOf(red[2]))
                .r4(Integer.valueOf(red[3]))
                .r5(Integer.valueOf(red[4]))
                .r6(Integer.valueOf(red[5]))
                .b1(Integer.valueOf(this.blue))
                .poolmoney(NumberUtils.parseLong(this.poolmoney))
                .content(this.content)
                .sales(NumberUtils.parseLong(this.sales))
                .build();

        List<SsqDetail> details = IntStream.range(0,this.prizegrades.size()-1).mapToObj(i->i).map(i->SsqDetail.builder()
                .code(this.code)
                .level(i+1)
                .levelName(intToHz(i+1)+"等奖")
                .num(StringUtils.isNotBlank(prizegrades.get(i).getTypenum())?Integer.valueOf(prizegrades.get(i).getTypenum()):0)
                .money(StringUtils.isNotBlank(prizegrades.get(i).getTypemoney())?Integer.valueOf(prizegrades.get(i).getTypemoney()):0)
                .allMoney(StringUtils.isNotBlank(prizegrades.get(i).getTypenum()) && StringUtils.isNotBlank(prizegrades.get(i).getTypemoney()) ?Long.valueOf(prizegrades.get(i).getTypenum()) * Long.valueOf(prizegrades.get(i).getTypemoney()):0)
                .build())
                .collect(Collectors.toList());
        lottery.setDetails(details);
        return lottery;
    }

    private String intToHz(Integer i){
        String[] s = {"零","一","二","三","四","五","六","七","八","九","十"};
        return s[i];
    }

}
