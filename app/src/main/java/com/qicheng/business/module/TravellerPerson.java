package com.qicheng.business.module;

/**
 * Created by NO2 on 2015/2/2.
 */
public class TravellerPerson {

    /**
     * 车友ID
     */
    private String user_id;

    /**
     * 头像URL
     */
    private String portrait_url;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }
}
