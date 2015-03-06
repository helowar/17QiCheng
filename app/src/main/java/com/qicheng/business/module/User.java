package com.qicheng.business.module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO1 on 2015/1/18.
 */
public class User {

    private String userName;

    private String passWord;

    private String token;

    private String portraitURL;

    private String cellNum;

    private String userId;

    private String verifyCode;

    private String nickName;

    private String birthday;

    private int gender;

    private ArrayList<LabelType> mLabelTypes;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 用户位置信息
     */
    private Location location;

    /**
     * 用户查询值
     */
    private QueryValue queryValue;

    public User() {
        location = new Location();
        queryValue = new QueryValue();
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPortraitURL() {
        return portraitURL;
    }

    public void setPortraitURL(String portraitURL) {
        this.portraitURL = portraitURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getCellNum() {
        return cellNum;
    }

    public void setCellNum(String cellNum) {
        this.cellNum = cellNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public ArrayList<LabelType> getLabelTypes() {
        return mLabelTypes;
    }

    public void setLabelTypes(ArrayList<LabelType> labelTypes) {
        mLabelTypes = labelTypes;
    }

    public Location getLocation() {
        return location;
    }

    public QueryValue getQueryValue() {
        return queryValue;
    }
}
