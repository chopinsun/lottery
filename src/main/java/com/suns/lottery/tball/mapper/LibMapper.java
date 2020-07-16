package com.suns.lottery.tball.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LibMapper {
    void saveAll(@Param("balls") List<String> balls,@Param("type") String type);
    List<String> find(@Param("balls") String balls,@Param("type") String type);
    void deleteAllByType(String type);
}
