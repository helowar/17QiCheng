package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by NO1 on 2015/2/12.
 */
public class GetTrainListProcess extends BaseProcess {

    private static final Logger logger = new Logger("com.qicheng.business.protocol.GetTrainListProcess");

    private static final String url = "/basedata/train_list.html";

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            //取回的JSON结果
            JSONObject o = new JSONObject(result);
            //获取状态码
            int value = o.optInt("result_code");
            logger.d("Get train list result:"+result);
            if(value ==0){
                /**
                 * 获取列表并缓存
                 */
                JSONArray trains = o.optJSONArray("body");
                Set<String> trainCodeSet = new HashSet<String>();
                for(int i =0;i<trains.length();i++){
                    trainCodeSet.add((trains.getString(i)));
                }
                Cache.getInstance().setTrainList(trainCodeSet);
            }
            setProcessStatus(value);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }

    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
