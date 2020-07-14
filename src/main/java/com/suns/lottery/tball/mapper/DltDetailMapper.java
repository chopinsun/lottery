package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.DltDetail;

import java.util.List;

public interface DltDetailMapper {
    List<DltDetail> findByCode(String code);
    void batchInsert(List<DltDetail> list);

}