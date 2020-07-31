package com.jcloud.recordcenter.client.vo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.Builder;
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


    public static PageInfo convert(Page page){
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(page.getPageNum());
        pageInfo.setTotalPage(page.getPages());
        pageInfo.setPageSize(page.getPageSize());
        pageInfo.setTotal(page.getTotal());
        pageInfo.setCount(page.isCount());
        pageInfo.setOrderBy(page.getOrderBy());
        pageInfo.setReasonable(page.getReasonable());
        return pageInfo;
    }
}
