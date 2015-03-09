/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.persistor;

import android.content.Context;
import android.content.SharedPreferences;

import com.qicheng.util.Const;

import java.util.Set;

/**
 * 提供不转码的用户行程相关城市/车次 json字符串缓存
 */
public class TripRelatedPreferences {

    private static final String city_list_key = "citys";

    private static final String train_list_key = "trains";

    private SharedPreferences getPreferences() {
        return Const.Application.getSharedPreferences(Const.SharedPreferenceKey.TripRelatedPreference, Context.MODE_PRIVATE);
    }

    public void setCityList(String cityList) {
        getPreferences().edit().putString(city_list_key, cityList).commit();
    }

    public void setTrainList(String trainList) {
        getPreferences().edit().putString(train_list_key, trainList).commit();
    }

    public void setStationList(String cityCode, String stationList) {
        getPreferences().edit().putString(cityCode, stationList).commit();
    }

    public String getCityList() {
        return getPreferences().getString(city_list_key, "");
    }

    public String getTrainList() {
        return getPreferences().getString(train_list_key, "");
    }

    public String getStationList(String cityCode) {
        return getPreferences().getString(cityCode, "");
    }
}
