package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.Dlt;
import com.suns.lottery.tball.bean.Ssq;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DltMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dlt record);
    int batchInsert(List<Dlt> list);

    Dlt selectByPrimaryKey(Integer id);

    List<Dlt> selectAll();

    int updateByPrimaryKey(Dlt record);

    int countByRedNum(int num);

    int countByBlueNum(Integer num);

    int exists(@Param("r1") int r1, @Param("r2") int r2, @Param("r3") int r3, @Param("r4") int r4, @Param("r5") int r5);

    List<Dlt> history(int n);


    int countByCode(@Param("code") String code);

}