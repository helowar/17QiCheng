/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.LocationLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TravellerPersonLogic;
import com.qicheng.business.module.User;

/**
 * LocationService.java是启程APP的获取地理位置服务类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月26日
 */
public class LocationService extends Service {

    /**
     * 百度定位SDK相关对象
     */
    private LocationClient locationClient = null;

    /**
     * 查询用户信息业务逻辑处理对象
     */
    private LocationLogic logic = null;

    @Override
    public void onCreate() {
        super.onCreate();
        logic = (LocationLogic) LogicFactory.self().get(LogicFactory.Type.Location);
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setProdName("17QiCheng");
        // 每间隔30分钟取一次地理位置信息
        option.setScanSpan(1800000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                User user = Cache.getInstance().getUser();
                // 经度
                user.getLocation().setLongitude(String.valueOf(bdLocation.getLongitude()));
                // 纬度
                user.getLocation().setLatitude(String.valueOf(bdLocation.getLatitude()));
                user.getLocation().setDirection(bdLocation.getDirection());
                user.getLocation().setCity(bdLocation.getCity());
                Cache.getInstance().refreshCacheUser();
                // 上报用户位置信息到服务端
                logic.report(user.getLocation());
            }
        });
        locationClient.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(locationClient.isStarted()) {
            locationClient.requestLocation();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void onDestroy() {
        locationClient.stop();
        super.onDestroy();
    }
}
