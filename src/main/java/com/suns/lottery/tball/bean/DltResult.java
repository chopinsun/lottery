package com.suns.lottery.tball.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DltResult {

    private String allmoney;
    private String level;
    private String money;
    private Integer num;
    private String piece;
    private String sendCount;
    private String sendPrize;
    private String six;

}
