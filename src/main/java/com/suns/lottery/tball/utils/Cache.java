package com.suns.lottery.tball.utils;

import com.suns.lottery.tball.bean.Dlt;
import com.suns.lottery.tball.bean.Ssq;
import com.suns.lottery.tball.mapper.DltMapper;
import com.suns.lottery.tball.mapper.SsqMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-16 16:51
 **/
@Component
@Log4j2
public class Cache {
    @Autowired
    private SsqMapper ssqMapper;
    @Autowired
    private DltMapper dltMapper;

    private final String KEY="lib";

    private final static ConcurrentHashMap<String, Object> ssq = new ConcurrentHashMap<>(6000);
    private final static ConcurrentHashMap<String, Object> dlt = new ConcurrentHashMap<>(6000);
    private long lastSyncTime_ssq;
    private long lastSyncTime_dlt;
    public Cache(){
        lastSyncTime_ssq = System.currentTimeMillis();
        lastSyncTime_dlt = System.currentTimeMillis();
    }
    public void put(Type t,Object v){
        if(t.equals(Type.SSQ)){
            lastSyncTime_ssq = System.currentTimeMillis();
            ssq.put(KEY,v);
        }else if(t.equals(Type.DLT)){
            lastSyncTime_dlt = System.currentTimeMillis();
            dlt.put(KEY,v);
        }else{
            return ;
        }
    }

    public Object get(Type t){
        if(Type.SSQ.equals(t)){
            if(System.currentTimeMillis() - lastSyncTime_ssq> 1000*60*5){
                syncSsq();
            }
            return ssq.get(KEY);
        }else if(Type.DLT.equals(t)){
            if(System.currentTimeMillis() - lastSyncTime_dlt> 1000*60*5){
                syncDlt();
            }
            return dlt.get(KEY);
        }else{
            return null;
        }
    }
    @PostConstruct
    public void init(){
        syncSsq();
        syncDlt();
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void autoSync(){
        syncSsq();
        syncDlt();
    }

    private void syncSsq(){
        List<Ssq> ssqList = ssqMapper.selectAll();
        put(Type.SSQ,ssqList);
    }

    private void syncDlt(){
        List<Dlt> dltList = dltMapper.selectAll();
        put(Type.DLT,dltList);
    }

    public enum Type{
        SSQ,
        DLT;
        Type() {
        }
    }

}
