/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.persistor;

import com.qicheng.business.module.User;

import java.util.Set;

/**
 * Created by NO1 on 2015/1/20.
 */
public class PersistorManager {

    private static PersistorManager ourInstance = new PersistorManager();

    private UserPreferences userPreferences;

    private TrainPreferences trainPreferences;

    private TripRelatedPreferences tripRelatedPreferences;

    public static PersistorManager getInstance() {
        return ourInstance;
    }

    private PersistorManager() {
        userPreferences = new UserPreferences();
        trainPreferences = new TrainPreferences();
        tripRelatedPreferences = new TripRelatedPreferences();
    }

    public void savePublicKey(String publicKey) {
        userPreferences.setPublicKey(publicKey);
    }

    public String getPublicKey() {
        return userPreferences.getPublicKey();
    }

    public User getLastestUser() {
        return userPreferences.getLastest();
    }

    public void setUser(User user) {
        userPreferences.set(user);
    }

    public void setTrainList(Set<String> trains) {
        trainPreferences.set(trains);
    }

    public Set<String> getTrainList() {
        return trainPreferences.get();
    }

    public void setTripRelatedCityList(String cityList) {
        tripRelatedPreferences.setCityList(cityList);
    }

    public void setTripRelatedTrainList(String trainList) {
        tripRelatedPreferences.setTrainList(trainList);
    }

    public void setTripRelatedStationList(String cityCode, String stationList) {
        tripRelatedPreferences.setStationList(cityCode, stationList);
    }

    public String getTripRelatedCityList() {
        return tripRelatedPreferences.getCityList();
    }

    public String getTripRelatedTrainList() {
        return tripRelatedPreferences.getTrainList();
    }

    public String getTripRelatedStationList(String cityCode) {
        return tripRelatedPreferences.getStationList(cityCode);
    }
}
