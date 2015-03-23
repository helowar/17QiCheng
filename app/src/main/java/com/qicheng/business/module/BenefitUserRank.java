/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

/**
 * Created by 金玉龙 on 2015/3/23.
 * 福利排名类
 */
public class BenefitUserRank {
    /**
     * 好友id，如果为空，则表示当前用户自己的福利统计信息。
     */
    private String userId;

    /**
     * 好友IM账户ID
     */
    private String userImId;

    /**
     * 好友头像URL
     */
    private String portraitUrl;

    /**
     * 好友昵称
     */
    private String userName;

    /**
     * 性别 0:女 1:男
     */
    private int gender;

    /**
     * 好友有效福利数量
     */
    private int benefitNum;

    /**
     * 排名
     */
    private int ranking;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImId() {
        return userImId;
    }

    public void setUserImId(String userImId) {
        this.userImId = userImId;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getBenefitNum() {
        return benefitNum;
    }

    public void setBenefitNum(int benefitNum) {
        this.benefitNum = benefitNum;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BenefitUserRank{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", userImId='").append(userImId).append('\'');
        sb.append(", portraitUrl='").append(portraitUrl).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", benefitNum=").append(benefitNum);
        sb.append(", ranking=").append(ranking);
        sb.append('}');
        return sb.toString();
    }
}
