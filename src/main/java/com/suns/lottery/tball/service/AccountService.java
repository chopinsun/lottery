package com.suns.lottery.tball.service;

import com.suns.lottery.tball.bean.User;
import com.suns.lottery.tball.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-29 16:49
 **/
@Component
public class AccountService {
    @Autowired
    private UserMapper userMapper;


    public User checkPassword(String username,String password){
        User user = userMapper.findByUserName(username);
        if(user!=null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public void addUser(User user){
        userMapper.addUser(user);
    }

    public User findUser(String uid){
        return userMapper.findByUid(uid);
    }


    public void updateUser(User user){
        userMapper.updateByUid(user);
    }

}
