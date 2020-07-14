package com.suns.lottery.tball.mapper;

import com.suns.lottery.tball.bean.SsqDetail;

import java.util.List;
import java.util.Map;

public interface SsqDetailMapper {
    List<SsqDetail> findByCode(String code);
    void batchInsert(List<SsqDetail> list);

}