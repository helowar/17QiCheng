/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.TrainStation;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 金玉龙 on 2015/3/4.
 * 启程App获取车站列表接口处理类
 */
public class GetStationListProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.GetStationListProcess");

    private String url = "/basedata/station_list.html";

    private String cityCode;

    public String trainResult;

    private List<TrainStation> stationList = null;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("city_code", cityCode);
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入搜索车站参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        try {
            //获取状态码
            int resultCode = o.optInt("result_code");
            setProcessStatus(resultCode);
            if (resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                /**
                 * 获取列表并缓存
                 */
                JSONArray stationJsonArray = o.has("body") ? o.optJSONArray("body") : null;
                if (stationJsonArray != null) {
                    stationList = new ArrayList<TrainStation>();
                    for (int i = 0, length = stationJsonArray.length(); i < length; i++) {
                        JSONObject stationJsonObj = stationJsonArray.getJSONObject(i);
                        TrainStation station = new TrainStation();
                        station.setStationName(stationJsonObj.optString("name"));
                        station.setStationCode(stationJsonObj.optString("code"));
                        stationList.add(station);
                    }
                    Cache.getInstance().refreshTripRelatedStationCache(cityCode, o.getString("body"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }

    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getTrainResult() {
        return trainResult;
    }

    public void setTrainResult(String trainResult) {
        this.trainResult = trainResult;
    }

    public List<TrainStation> getStationList() {
        return stationList;
    }

    public void setStationList(List<TrainStation> stationList) {
        this.stationList = stationList;
    }
}
