package com.suns.lottery.tball.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wenxin on 2015/12/29.
 */
@Data
public class PageInfo implements Serializable {

    private int currentPage;
    private int pageSize;
    private long total;
    private int totalPage;
    private boolean count;
    private Boolean reasonable;
    private Boolean pageSizeZero;
    private String countColumn;
    private String orderBy;
    private boolean orderByOnly;

}
