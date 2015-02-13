package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;
import com.qicheng.util.Const;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/11.
 */
public class GetTrainInfoProcess extends BaseProcess {

    private static final String url = "/basedata/schedule_list.html";

    private String trainCode;

//    private

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
    protected void onResult(String result) {
        try {
            //取回的JSON结果
            JSONObject o = new JSONObject(result);
            //获取状态码
            int value = o.optInt(STATUS_TAG);
            if(value==0){
                /**
                 * 取出返回值
                 */
                JSONObject body = JSONUtil.getResultBody(o);

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

    public void setParam(String trainCode) {
        this.trainCode = trainCode;
    }
}
