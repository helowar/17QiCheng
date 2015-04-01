package com.qicheng.business.protocol;

import com.qicheng.business.module.Trip;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NO1 on 2015/1/26.
 */
public class TripListProcess extends BaseProcess{

    private static final String url = "/trip/list.html";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.TripListProcess");

    private ArrayList<Trip> mTripList = null;

    private int param;


    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("order_num",param);
            o.put("size", Const.TripListFetchCount);
            return o.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onResult(JSONObject o) {
        try {
            //获取状态码
            int value = o.optInt("result_code");
            setProcessStatus(value);
            if(getStatus()== ProcessStatus.Status.Success){
                /**
                 * 获取行程列表
                 */
                JSONArray jsonArrayTripList = o.has("body") ? o.optJSONArray("body") : null;
                if(jsonArrayTripList!=null){
                    mTripList = new ArrayList<Trip>();
                    for(int i = 0;i<jsonArrayTripList.length();i++){
                        JSONObject jsonTrip = jsonArrayTripList.getJSONObject(i);
                        Trip trip = new Trip();
                        trip.setTrainCode(jsonTrip.optString("train_name"));
                        trip.setStartStationName(jsonTrip.optString("begin_station_name"));
                        trip.setStartStationCode(jsonTrip.optString("begin_station_code"));
                        trip.setEndStationName(jsonTrip.optString("end_station_name"));
                        trip.setEndStationCode(jsonTrip.optString("end_station_code"));
                        trip.setStartTime(jsonTrip.optString("begin_time"));
                        trip.setStopTime(jsonTrip.optString("end_time"));
                        trip.setOrderNum(jsonTrip.optInt("order_num"));
                        trip.setStartUserList(getUserList(jsonTrip.has("begin_travellers") ? jsonTrip.optJSONArray("begin_travellers") : null));
                        trip.setStopUserList(getUserList(jsonTrip.has("end_travellers") ? jsonTrip.optJSONArray("end_travellers") : null));
                        trip.setTrainUserList(getUserList(jsonTrip.has("train_travellers") ? jsonTrip.optJSONArray("train_travellers") : null));
                        mTripList.add(trip);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }
    }

    private ArrayList<String> getUserList(JSONArray jsonList){
        ArrayList<String> userList;
        if(jsonList ==null){
            userList=null;
        }else{
            userList = new ArrayList<String>();
            try{
                for(int j = 0;j<jsonList.length();j++){
                    userList.add(jsonList.getString(j));
                }
            }catch (JSONException e){
                setStatus(ProcessStatus.Status.ErrUnkown);
            }
        }
        return  userList;
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public ArrayList<Trip> getResult(){
        return this.mTripList;
    }
}
