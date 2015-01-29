package com.qicheng.business.module;

import java.util.UUID;

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

    public User() {
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
}
