package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/1/19.
 */
public class LoginProcess extends BaseProcess {

    //服务端请求地址
    private final String URL="http://192.168.1.112/user/login.html?t=";

    //参数对象
    private User paramUser;

    public User getResultUser() {
        return resultUser;
    }

    //结果对象
    private User resultUser;

    @Override
    protected String getFakeResult() {
        String fakeResult = "{status:0,userName:"+paramUser.getUserName()+",passWord:"+paramUser.getPassWord()+"}";
        return fakeResult;
    }

    @Override
    protected String getRequestUrl() {
        return URL;
    }

    public void setParamUser(User param){
        paramUser=param;
    }

    @Override
    protected String getInfoParameter() {

        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("userName", paramUser.getUserName());
            o.put("pwd", paramUser.getPassWord());
//            o.put("lng", String.valueOf(mUser.getLocation().getLng()));
//            o.put("lat", String.valueOf(mUser.getLocation().getLat()));
//            o.put("uuid", mPhoneUuid);
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
            int value = o.optInt("status");
            switch(value) {
                case 0:
                    setStatus(ProcessStatus.Status.Success);
                    resultUser = new User();
                    /**
                     * 解析JSON返回值
                     * o.optSting("name");
                     * resultUser = new User();
                     * user.set
                     */
                    break;
                case -1:
                    setStatus(ProcessStatus.Status.ErrPass);
                    break;
                case -2:
                    setStatus(ProcessStatus.Status.ErrUid);
                    break;
                case -10:
                    setStatus(ProcessStatus.Status.ErrException);
                    break;
            }
        } catch(Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }
    }
}
