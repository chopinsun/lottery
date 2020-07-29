package com.suns.lottery.tball.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-29 19:08
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WechartCallBack extends ThirdCallBack {
    private String wechartNo;
    private Boolean success;
    private String sex;
    private String address;

}
