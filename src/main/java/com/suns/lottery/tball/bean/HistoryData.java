package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class HistoryData {

    private Integer state;
    private String message;
    private Integer pageCount;
    private Integer countNum;
    private Integer Tflag;
    private List<LotteryResult> result;
}
