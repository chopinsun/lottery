package com.suns.lottery.tball.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-01-14 17:59
 **/
@Configuration
@MapperScan(value = { "cn.itweknow.sbmybatis.mapper" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class Database {
}
