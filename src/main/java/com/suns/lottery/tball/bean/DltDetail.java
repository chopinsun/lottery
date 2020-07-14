package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-14 17:37
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DltDetail {

    private Integer id;
    private String code;
    private Integer level;
    private String levelName;
    private Integer num ;
    private Integer money ;
    private Long allMoney ;
}
