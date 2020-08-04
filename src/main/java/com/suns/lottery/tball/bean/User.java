package com.suns.lottery.tball.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-29 16:21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String uid;
    private String username;
    private String password;
    private String inviteCode;
    private String sourceType;
    private String inviteLink;
    private String createTime;
    private String wechart;
    private String userGroup;
    private String authorityId;
    private String mobile;
    private Integer thridFlag;
    private String avatar;
    private Integer status;
    private Integer vip;
}
