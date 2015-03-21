/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

import java.io.Serializable;

/**
 * Created by 金玉龙 on 2015/3/21.
 * 福利实体类
 */
public class Benefit implements Serializable {
    /**
     * 福利实例ID
     */
    private String id;
    /**
     * 福利名称
     */
    private String name;
    /**
     * 福利状态
     * 0:未使用/未领取
     * 1:已使用/已领取
     * 2:已过期
     */
    private byte status;
    /**
     * 后续可操作标识
     * 0:不可操作
     * 1:可转让
     */
    private byte postOpFlag;

    /**
     * 当前来源ID
     * 如果为空，则表示系统；否则为用户ID。
     */
    private String sourceId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 福利Logo图片URL
     */
    private String logoUrl;
    /**
     * 福利背景图片URL
     */
    private String bgUrl;
    /**
     * 福利AndroidAPP下载URL
     */
    private String androidUrl;
    /**
     * 福利iOSAPP下载URL
     */
    private String iOSUrl;
    /**
     * 福利过期时间
     */
    private String expireTime;
    /**
     * 福利金额
     */
    private int value;
    /**
     * 福利说明
     */
    private String description;
    /**
     * 外部供应商福利ID
     */
    private String outsideId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getPostOpFlag() {
        return postOpFlag;
    }

    public void setPostOpFlag(byte postOpFlag) {
        this.postOpFlag = postOpFlag;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public void setBgUrl(String bgUrl) {
        this.bgUrl = bgUrl;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getiOSUrl() {
        return iOSUrl;
    }

    public void setiOSUrl(String iOSUrl) {
        this.iOSUrl = iOSUrl;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutsideId() {
        return outsideId;
    }

    public void setOutsideId(String outsideId) {
        this.outsideId = outsideId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Benefit{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", status=").append(status);
        sb.append(", postOpFlag=").append(postOpFlag);
        sb.append(", sourceId='").append(sourceId).append('\'');
        sb.append(", createTime='").append(createTime).append('\'');
        sb.append(", logoUrl='").append(logoUrl).append('\'');
        sb.append(", bgUrl='").append(bgUrl).append('\'');
        sb.append(", androidUrl='").append(androidUrl).append('\'');
        sb.append(", iOSUrl='").append(iOSUrl).append('\'');
        sb.append(", expireTime='").append(expireTime).append('\'');
        sb.append(", value=").append(value);
        sb.append(", description='").append(description).append('\'');
        sb.append(", outsideId='").append(outsideId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
