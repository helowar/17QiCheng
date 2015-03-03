/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.cache;

import com.qicheng.business.module.City;
import com.qicheng.business.module.Train;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.framework.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO1 on 2015/3/2.
 */
public class TripRelatedCache {

    /**
     * 用于用户/动态搜索的用户行程关联城市列表
     */
    private List<City> mCitys;
    /**
     * 用于用户/动态搜索的用户行程关联车次列表
     */
    private List<Train> mTrains;

    public void onCreate() {
        String jsonCitys = PersistorManager.getInstance().getTripRelatedCityList();
        String jsonTrain = PersistorManager.getInstance().getTripRelatedTrainList();
        mCitys = new ArrayList<City>();
        mTrains = new ArrayList<Train>();
        if(!StringUtil.isEmpty(jsonCitys)){
           try{
               JSONArray cityArray = new JSONArray(jsonCitys);
               for(int i = 0;i<cityArray.length();i++){
                   JSONObject jc = cityArray.optJSONObject(i);
                   City c = new City();
                   c.setCityCode(jc.optString("code"));
                   c.setCityName(jc.optString("name"));
                   mCitys.add(c);
               }
           }catch (JSONException ex){
               //do nothing
           }
        }
        if(!StringUtil.isEmpty(jsonTrain)){
            try{
                JSONArray trainArray = new JSONArray(jsonTrain);
                for(int i = 0;i<trainArray.length();i++){
                    String jt = trainArray.optString(i);
                    Train t = new Train();
                    t.setTrainCode(jt);
                    mTrains.add(t);
                }
            }catch (JSONException ex){
                //do nothing
            }
        }

    }

    public void setCityCache(String citys){
        PersistorManager.getInstance().setTripRelatedCityList(citys);
        mCitys.clear();
        try{
            JSONArray cityArray = new JSONArray(citys);
            for(int i = 0;i<cityArray.length();i++){
                JSONObject jc = cityArray.optJSONObject(i);
                City c = new City();
                c.setCityCode(jc.optString("code"));
                c.setCityName(jc.optString("name"));
                mCitys.add(c);
            }
        }catch (JSONException ex){
            //do nothing
        }

    }

    public void setTrainCache(String trains){
        PersistorManager.getInstance().setTripRelatedTrainList(trains);
        mTrains.clear();
        try{
            JSONArray trainArray = new JSONArray(trains);
            for(int i = 0;i<trainArray.length();i++){
                String jt = trainArray.optString(i);
                Train t = new Train();
                t.setTrainCode(jt);
                mTrains.add(t);
            }
        }catch (JSONException ex){
            //do nothing
        }
    }

    public List<City> getCitys() {
        return mCitys;
    }

    public List<Train> getTrains() {
        return mTrains;
    }
}
