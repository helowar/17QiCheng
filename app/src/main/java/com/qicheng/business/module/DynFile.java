/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

/**
 * Created by NO3 on 2015/3/6.
 */

/**
 * 动态的文件类
 */
public class DynFile  {
    /*
    0:图片 1:视频
    默认0:图片
     */
    private byte fileType;
    /*
    文件相对路径
     */
    private String fileUrl;

    public byte getFileType() {
        return fileType;
    }

    public void setFileType(byte fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DynFile{");
        sb.append("fileType=").append(fileType);
        sb.append(", fileUrl='").append(fileUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}