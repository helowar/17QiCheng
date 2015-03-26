package com.qicheng.business.protocol;

import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NO1 on 2015/2/11.
 */
public class GetTrainInfoProcess extends BaseProcess {

    private static final String url = "/basedata/schedule_list.html";

    private String trainCode;

    private ArrayList<TrainStation> stationList;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("train_name",trainCode);
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
            int value = o.optInt(JSONUtil.STATUS_TAG);
            setProcessStatus(value);
            if(value==0){
                /**
                 * 取出返回值
                 */
                JSONArray body = o.has("body")?o.getJSONArray("body"):null;
                if(body!=null){
                    stationList = new ArrayList<TrainStation>();
                    for(int i = 0;i<body.length();i++){
                        TrainStation station = new TrainStation();
                        station.setStationName(body.getJSONObject(i).getString("station_name"));
                        station.setStationCode(body.getJSONObject(i).getString("station_code"));
                        station.setEnterTime(body.getJSONObject(i).getString("enter_time"));
                        station.setLeaveTime(body.getJSONObject(i).getString("leave_time"));
                        station.setIndex(body.getJSONObject(i).getInt("order_num"));
                        station.setCrossDays(body.getJSONObject(i).getInt("cross_days"));
                        stationList.add(station);
                    }
                    return;
                }
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

    public void setParam(String trainCode) {
        this.trainCode = trainCode;
    }

    public ArrayList<TrainStation> getResult() {
        return stationList;
    }
}
