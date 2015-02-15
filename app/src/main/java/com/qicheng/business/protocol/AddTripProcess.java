package com.qicheng.business.protocol;

import com.qicheng.business.module.Trip;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NO1 on 2015/2/12.
 */
public class AddTripProcess extends BaseProcess {

    private static final Logger logger = new Logger("com.qicheng.business.protocol.AddTripProcess");

    private static final String url = "/trip/add.html";

    private Trip param;

    private Trip result;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("train_name",param.getTrainCode());
            o.put("begin_station_code",param.getStartStationCode());
            o.put("begin_station_name",param.getStartStationName());
            o.put("end_station_code",param.getEndStationCode());
            o.put("end_station_name",param.getEndStationName());
            o.put("begin_time",param.getStartTime());
            o.put("end_time",param.getStopTime());
            o.put("stay_days",param.getStayDays());
            o.put("carpool_type",param.getCarSharing());
            o.put("travel_type",param.getTravelTogether());
            return o.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            //取回的JSON结果
            JSONObject o = new JSONObject(result);
            //获取状态码
            int value = o.optInt("result_code");
            setProcessStatus(value);
            logger.d("Add Trip result:"+result);
            if(value ==0){
                /**
                 * 获取添加行程中的用户
                 */
                JSONObject jsonArrayUserList = o.has("body") ? o.optJSONObject("body") : null;
                if(jsonArrayUserList!=null){
                    this.result = new Trip();
                    this.result.setStartUserList(getUserList(jsonArrayUserList.has("begin_travellers") ? jsonArrayUserList.optJSONArray("begin_travellers") : null));
                    this.result.setStopUserList(getUserList(jsonArrayUserList.has("end_travellers") ? jsonArrayUserList.optJSONArray("end_travellers") : null));
                    this.result.setTrainUserList(getUserList(jsonArrayUserList.has("train_travellers") ? jsonArrayUserList.optJSONArray("train_travellers") : null));
                }
            }else{
                setStatus(ProcessStatus.Status.InfoNoData);
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

    public void setParam(Trip param) {
        this.param = param;
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

    public Trip getResult() {
        return result;
    }
}
