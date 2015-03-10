/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by NO3 on 2015/2/28.
 * 动态实体类
 */
public class Dyn implements Serializable {
    /*
    用户id
     */
    private String userId;

    /*
    用户Imid
    */
    private String userImId;

    /*
   用户头像
    */
    private String portraitUrl;
    /*
   用户昵称
    */
    private String nickName;
    /*
   用户匿名
    */
    private String anmName;
    /*
   动态id
    */
    private String activityId;
    /*
   动态内容
    */
    private String content;

    /*
    动态的type
     */

    private int type;

    /*
   分享次数
    */
    private int sharedNum;
    /*
   点赞次数
    */
    private int likedNum;

    /*
    是否分享过
     */
    private int isShared;

    /*
    是否赞过
     */

    private int isLiked;


    /*
   动态创建时间
    */
    private Date createTime;
    /*
   排序号
    */
    private int orderNum;
    /*
   文件类型
    */
    private int fileType;
    /*
   缩略图url
    */
    private String thumbnailUrl;
    /*
   原图url
    */
    private String fileUrl;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAnmName() {
        return anmName;
    }

    public void setAnmName(String anmName) {
        this.anmName = anmName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsShared() {
        return isShared;
    }

    public void setIsShared(int isShared) {
        this.isShared = isShared;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public int getSharedNum() {
        return sharedNum;
    }

    public void setSharedNum(int sharedNum) {
        this.sharedNum = sharedNum;
    }

    public int getLikedNum() {
        return likedNum;
    }

    public void setLikedNum(int likedNum) {
        this.likedNum = likedNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Dyn{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", userImId='").append(userImId).append('\'');
        sb.append(", portraitUrl='").append(portraitUrl).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", anmName='").append(anmName).append('\'');
        sb.append(", activityId='").append(activityId).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", type=").append(type);
        sb.append(", sharedNum=").append(sharedNum);
        sb.append(", likedNum=").append(likedNum);
        sb.append(", isShared=").append(isShared);
        sb.append(", isLiked=").append(isLiked);
        sb.append(", createTime=").append(createTime);
        sb.append(", orderNum=").append(orderNum);
        sb.append(", fileType=").append(fileType);
        sb.append(", thumbnailUrl='").append(thumbnailUrl).append('\'');
        sb.append(", fileUrl='").append(fileUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
