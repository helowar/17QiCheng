/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

import java.io.Serializable;
import java.util.List;

/**
 * UserDetail.java是启程APP的用户详细信息实体类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class UserDetail implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户IM账户ID
     */
    private String userIMId;

    /**
     * 用户头像URL
     */
    private String portraitUrl;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别 0:女 1:男
     */
    private int gender;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 在线状态 0:离线 1:在线
     */
    private int online;

    /**
     * 动态数量
     */
    private int activityNum;

    /**
     * 居住地
     */
    private String residence;

    /**
     * 家乡
     */
    private String hometown;

    /**
     * 学历，0：初中 1：高中 2：中专 3：大专 4：本科 5：研究生 6：博士 7：博士后
     */
    private String education;

    /**
     * 行业
     */
    private String industry;

    /**
     * 标签数组
     */
    private List<String> tags;

    /**
     * 相册列表
     */
    private List<Photo> photoList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIMId() {
        return userIMId;
    }

    public void setUserIMId(String userIMId) {
        this.userIMId = userIMId;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(int activityNum) {
        this.activityNum = activityNum;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
