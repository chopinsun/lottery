package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.Lottery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface LotteryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery record);
    int batchInsert(List<Lottery> list);

    Lottery selectByPrimaryKey(Integer id);

    List<Lottery> selectAll();

    int updateByPrimaryKey(Lottery record);

    int countByNum(int num);
}