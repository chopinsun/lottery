package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-13 19:46
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prizegrade {

    private Integer type;
    private String typenum;
    private String typemoney;

}
