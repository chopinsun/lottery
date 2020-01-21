package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.Lottery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface LotteryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Lottery record);
    int batchInsert(List<Lottery> list);

    Lottery selectByPrimaryKey(Integer id);

    List<Lottery> selectAll();

    int updateByPrimaryKey(Lottery record);

    int countByRedNum(int num);

    List<Integer> countByBlueNum(@Param("sort") String sort);

    List<Map<String,Integer>> countByBlueNum2();

    int exists(@Param("r1") int r1,@Param("r2") int r2,@Param("r3") int r3,@Param("r4") int r4,@Param("r5") int r5,@Param("r6") int r6);

    List<Lottery> history(int n);

}