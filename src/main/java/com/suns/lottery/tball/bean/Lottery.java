package com.suns.lottery.tball.bean;

import java.util.Date;

public class Lottery {
    private Integer id;

    private String code;

    private Integer r1;

    private Integer r2;

    private Integer r3;

    private Integer r4;

    private Integer r5;

    private Integer r6;

    private Integer b1;

    private String b2;

    private String sales;

    private String poolmoney;

    private String content;

    private Integer type1Num;

    private Integer type1Money;

    private Integer type2Num;

    private Integer type2Money;

    private Integer type3Num;

    private Integer type3Money;

    private Date lotteryDate;

    public Lottery(Integer id, String code, Integer r1, Integer r2, Integer r3, Integer r4, Integer r5, Integer r6, Integer b1, String b2, String sales, String poolmoney, String content, Integer type1Num, Integer type1Money, Integer type2Num, Integer type2Money, Integer type3Num, Integer type3Money, Date lotteryDate) {
        this.id = id;
        this.code = code;
        this.r1 = r1;
        this.r2 = r2;
        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
        this.r6 = r6;
        this.b1 = b1;
        this.b2 = b2;
        this.sales = sales;
        this.poolmoney = poolmoney;
        this.content = content;
        this.type1Num = type1Num;
        this.type1Money = type1Money;
        this.type2Num = type2Num;
        this.type2Money = type2Money;
        this.type3Num = type3Num;
        this.type3Money = type3Money;
        this.lotteryDate = lotteryDate;
    }

    public Lottery() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getR1() {
        return r1;
    }

    public void setR1(Integer r1) {
        this.r1 = r1;
    }

    public Integer getR2() {
        return r2;
    }

    public void setR2(Integer r2) {
        this.r2 = r2;
    }

    public Integer getR3() {
        return r3;
    }

    public void setR3(Integer r3) {
        this.r3 = r3;
    }

    public Integer getR4() {
        return r4;
    }

    public void setR4(Integer r4) {
        this.r4 = r4;
    }

    public Integer getR5() {
        return r5;
    }

    public void setR5(Integer r5) {
        this.r5 = r5;
    }

    public Integer getR6() {
        return r6;
    }

    public void setR6(Integer r6) {
        this.r6 = r6;
    }

    public Integer getB1() {
        return b1;
    }

    public void setB1(Integer b1) {
        this.b1 = b1;
    }

    public String getB2() {
        return b2;
    }

    public void setB2(String b2) {
        this.b2 = b2 == null ? null : b2.trim();
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales == null ? null : sales.trim();
    }

    public String getPoolmoney() {
        return poolmoney;
    }

    public void setPoolmoney(String poolmoney) {
        this.poolmoney = poolmoney == null ? null : poolmoney.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getType1Num() {
        return type1Num;
    }

    public void setType1Num(Integer type1Num) {
        this.type1Num = type1Num;
    }

    public Integer getType1Money() {
        return type1Money;
    }

    public void setType1Money(Integer type1Money) {
        this.type1Money = type1Money;
    }

    public Integer getType2Num() {
        return type2Num;
    }

    public void setType2Num(Integer type2Num) {
        this.type2Num = type2Num;
    }

    public Integer getType2Money() {
        return type2Money;
    }

    public void setType2Money(Integer type2Money) {
        this.type2Money = type2Money;
    }

    public Integer getType3Num() {
        return type3Num;
    }

    public void setType3Num(Integer type3Num) {
        this.type3Num = type3Num;
    }

    public Integer getType3Money() {
        return type3Money;
    }

    public void setType3Money(Integer type3Money) {
        this.type3Money = type3Money;
    }

    public Date getLotteryDate() {
        return lotteryDate;
    }

    public void setLotteryDate(Date lotteryDate) {
        this.lotteryDate = lotteryDate;
    }
}