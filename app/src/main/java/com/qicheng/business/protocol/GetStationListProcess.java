/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.TrainStation;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO3 on 2015/3/4.
 */
public class GetStationListProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.GetStationListProcess");

    private String url = "/basedata/station_list.html";

    private String cityCode;

    public String trainResult;

    private ArrayList<TrainStation> stationList = new ArrayList<TrainStation>();

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
    protected void onResult(String result) {
        try {
            //取回的JSON结果
            JSONObject o = new JSONObject(result);
            //获取状态码
            int resultCode = o.optInt("result_code");
            setProcessStatus(resultCode);
            logger.d("Get city related station list result:" + result);
            if (resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                /**
                 * 获取列表并缓存
                 */

                JSONArray jsonArrayStationList = o.has("body") ? o.optJSONArray("body") : null;
                if (jsonArrayStationList != null) {
                    for (int i = 0; i < jsonArrayStationList.length(); i++) {
                        JSONObject stationJsonObj = (JSONObject) jsonArrayStationList.get(i);
                        TrainStation station = new TrainStation();
                        station.setStationName(stationJsonObj.optString("name"));
                        station.setStationCode(stationJsonObj.optString("code"));
                        stationList.add(station);
                    }
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

    public ArrayList<TrainStation> getStationList() {
        return stationList;
    }

    public void setStationList(ArrayList<TrainStation> stationList) {
        this.stationList = stationList;
    }
}
