package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/3.
 */
public class VerifyCodeProcess extends BaseProcess{

    private final String url = "http://192.168.1.107:8080/qps/user/verify_code_get.html";

    private User mParamUser;

    private User mResultUser;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    public void setParamUser(User mParamUser) {
        this.mParamUser = mParamUser;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("action_type", "0");
            o.put("cell_num", mParamUser.getCellNum());
            return o.toString();
        } catch(JSONException e) {
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
            switch(value) {
                case 0:
                    setStatus(ProcessStatus.Status.Success);
                    break;
                case 1:
                    setStatus(ProcessStatus.Status.IllegalRequest);
                    break;
                default:
                    setStatus(ProcessStatus.Status.ErrUnkown);
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
