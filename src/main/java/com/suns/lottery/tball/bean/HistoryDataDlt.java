package com.suns.lottery.tball.bean;

import com.suns.lottery.tball.utils.NumberUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private DltResultDetail lottery;


    public Dlt convert(){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
            String s= lottery.getFTime();
            Date date =  formatter.parse(s);
            Dlt dlt = Dlt.builder()
                    .code(lottery.getTerm())
                    .r1(Integer.valueOf(codeNumber.get(0)))
                    .r2(Integer.valueOf(codeNumber.get(1)))
                    .r3(Integer.valueOf(codeNumber.get(2)))
                    .r4(Integer.valueOf(codeNumber.get(3)))
                    .r5(Integer.valueOf(codeNumber.get(4)))
                    .b1(Integer.valueOf(codeNumber.get(5)))
                    .b2(Integer.valueOf(codeNumber.get(6)))
                    .sales(NumberUtils.parseLong(lottery.getTotalSales()))
                    .lotteryTimestamp(String.valueOf(lottery.getOpenTime().getTime()))
                    .lotteryDate(date)
                .build();
            List<DltDetail> dltDetails = details.stream()
                    .map(x->DltDetail.builder()
                            .code(lottery.getTerm())
                            .level(x.getNum() - 400)
                            .levelName(x.getLevel())
                            .num(Integer.valueOf(x.getPiece()))
                            .money(Integer.valueOf(x.getMoney().replaceAll(",","")))
                            .allMoney(Long.valueOf(x.getAllmoney().replaceAll(",","")))
                            .build())
                    .collect(Collectors.toList());
            dlt.setDltDetail(dltDetails);

            return dlt;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
