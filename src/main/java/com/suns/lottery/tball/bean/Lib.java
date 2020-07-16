package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-16 15:46
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lib {
    private Integer id;
    private String balls;
    private String type;
}
