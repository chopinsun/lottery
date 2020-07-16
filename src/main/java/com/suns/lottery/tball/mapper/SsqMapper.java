package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.Ssq;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SsqMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Ssq record);
    int batchInsert(List<Ssq> list);

    Ssq selectByPrimaryKey(Integer id);

    List<Ssq> selectAll();

    List<Ssq> selectLastRecode();

    int updateByPrimaryKey(Ssq record);

    int countByRedNum(int num);

    int countByBlueNum(int num);

    List<Map<String,Integer>> countByBlueNum2();

    int exists(@Param("r1") int r1,@Param("r2") int r2,@Param("r3") int r3,@Param("r4") int r4,@Param("r5") int r5,@Param("r6") int r6);

    List<Ssq> history(int n);


    int countByCode(@Param("code") String code);

}