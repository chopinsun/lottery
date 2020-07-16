package com.suns.lottery.tball.bean;

import java.util.Map;

/**
 * @title: lottery-tball
 * @description:
 * @author: sunxiaobo
 * @create: 2020-07-15 18:44
 **/
public class Kv<K,V> implements Map.Entry<K,V>{
    private K k;
    private V v;

    public Kv(K k, V v) {
        this.k = k;
        this.v = v;
    }

    @Override
    public K getKey() {
        return k;
    }

    @Override
    public V getValue() {
        return v;
    }

    @Override
    public V setValue(V value) {
        return v;
    }

}
