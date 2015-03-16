/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

import java.io.Serializable;

/**
 * UserDetail.java是启程APP的相册信息实体类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class Photo implements Serializable {

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 原始照片URL
     */
    private String photoUrl;

    /**
     * 自然数字顺序号,APP端按序号获取动态文件
     */
    private long orderNum;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }
}
