/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.component;

/**
 * Created by NO3 on 2015/3/5.
 */

import java.io.Serializable;

/**
 * 动态搜索类
 */
public class DynSearch implements Serializable {

    private int orderBy;
    private long orderNum;
    private int size = 10;
    private Byte queryType;
    private String queryValue;

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Byte getQueryType() {
        return queryType;
    }

    public void setQueryType(Byte queryType) {
        this.queryType = queryType;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DynSearch{");
        sb.append("orderBy=").append(orderBy);
        sb.append(", orderNum=").append(orderNum);
        sb.append(", size=").append(size);
        sb.append(", queryType=").append(queryType);
        sb.append(", queryValue='").append(queryValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

