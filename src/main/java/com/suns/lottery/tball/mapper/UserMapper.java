package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.User;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-29 16:50
 **/
public interface UserMapper {

    User findByUserName(String userName);
    User findByUid(String uid);
    void updateByUid(User user);
    void addUser(User user);
}
