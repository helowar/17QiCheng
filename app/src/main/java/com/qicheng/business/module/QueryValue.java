/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

/**
 * QueryValue.java是启程APP的查询值实体类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月26日
 */
public class QueryValue {

    /**
     * 性别 0:女 1:男
     */
    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
