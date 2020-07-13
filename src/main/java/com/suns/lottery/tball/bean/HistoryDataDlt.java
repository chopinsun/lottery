package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @title: lottery-tball
 * @description: 双色球历史数据
 * @author: sunxiaobo
 * @create: 2020-01-13 19:40
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDataDlt {

    private List<String> codeNumber;
    private List<DltResult> details;
    private List<String> eventName;
    private DltDetail lottery;


    public Dlt convert(){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
            String s= lottery.getFTime();
            Date date =  formatter.parse(s);
            return Dlt.builder()
                    .code(lottery.getTerm())
                    .r1(Integer.valueOf(codeNumber.get(0)))
                    .r2(Integer.valueOf(codeNumber.get(1)))
                    .r3(Integer.valueOf(codeNumber.get(2)))
                    .r4(Integer.valueOf(codeNumber.get(3)))
                    .r5(Integer.valueOf(codeNumber.get(4)))
                    .b1(Integer.valueOf(codeNumber.get(5)))
                    .b2(Integer.valueOf(codeNumber.get(6)))
                    .sales(Integer.valueOf(lottery.getTotalSales().replaceAll(",","")))
                    .type1Num(details.get(0).getNum())
                    .type1Money(Integer.valueOf(details.get(0).getMoney().replaceAll(",","")))
                    .type2Num(details.get(1).getNum())
                    .type2Money(Integer.valueOf(details.get(1).getMoney().replaceAll(",","")))
                    .type3Num(details.get(2).getNum())
                    .type3Money(Integer.valueOf(details.get(2).getMoney().replaceAll(",","")))
                    .type4Num(details.get(3).getNum())
                    .type4Money(Integer.valueOf(details.get(3).getMoney().replaceAll(",","")))
                    .type5Num(details.get(4).getNum())
                    .type5Money(Integer.valueOf(details.get(4).getMoney().replaceAll(",","")))
                    .type6Num(details.get(5).getNum())
                    .type6Money(Integer.valueOf(details.get(5).getMoney().replaceAll(",","")))
                    .type7Num(details.get(6).getNum())
                    .type7Money(Integer.valueOf(details.get(6).getMoney().replaceAll(",","")))
                    .type8Num(details.get(7).getNum())
                    .type8Money(Integer.valueOf(details.get(7).getMoney().replaceAll(",","")))
                    .type9Num(details.get(8).getNum())
                    .type9Money(Integer.valueOf(details.get(8).getMoney().replaceAll(",","")))
                    .lotteryTimestamp(String.valueOf(lottery.getOpenTime().getTime()))
                    .lotteryDate(date)
                .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
