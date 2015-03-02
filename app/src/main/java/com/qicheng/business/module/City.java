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
 * Created by NO1 on 2015/3/2.
 */
public class City implements Serializable{

    private String mCityCode;

    private String mCityName;

    private List<TrainStation> mStationList;

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityCode(String cityCode) {
        mCityCode = cityCode;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public List<TrainStation> getStationList() {
        return mStationList;
    }

    public void setStationList(List<TrainStation> stationList) {
        mStationList = stationList;
    }
}
