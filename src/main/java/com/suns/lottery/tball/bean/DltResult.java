package com.suns.lottery.tball.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DltResult {
    private Integer id;
    @JSONField(format = "yyyyMMdd")
    @JsonFormat(pattern = "yyyyMMdd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date date_publish;
    private String num;
    private String one;
    private String two;
    private String three;
    private String four;
    private String five;
    private String six;
    private String seven;
    private String money;
    private String article_id;
    private String time_publish;


    public Dlt convert(){
        return Dlt.builder()
                .code(this.num)
                .lotteryDate(this.date_publish)
                .poolmoney(this.money)
                .r1(Integer.valueOf(this.one))
                .r2(Integer.valueOf(this.two))
                .r3(Integer.valueOf(this.three))
                .r4(Integer.valueOf(this.four))
                .r5(Integer.valueOf(this.five))
                .b1(Integer.valueOf(this.six))
                .b2(Integer.valueOf(this.seven))
                .lotteryTimestamp(this.time_publish)
                .seq(Integer.valueOf(this.article_id))
                .build();
    }
}
