package com.qicheng.business.protocol;

import com.qicheng.business.image.ImageManager;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/1/19.
 */
public class LoginProcess extends BaseProcess {

    //服务端请求地址
    private final String URL="http://192.168.1.107:8080/qps/user/login.html";

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
            o.put("user_name", paramUser.getUserName());
            o.put("pwd", paramUser.getPassWord());
            return RSACoder.getInstance().encryptByPublicKey(o.toString(),PersistorManager.getInstance().getPublicKey());
        } catch(Exception e) {
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
            switch(value) {
                case 0:
                    setStatus(ProcessStatus.Status.Success);
                    /**
                     * 取出返回值
                     */
                    JSONObject body = JSONUtil.getResultBody(o);
                    String token = body.optString("token");
                    String nickname = body.optString("nickname");
                    String url = body.optString("portrait_url");
                    /**
                     * 组装返回对象
                     */
                    resultUser.setToken(token);
                    resultUser.setNickName(nickname);
                    resultUser.setPortraitURL(url);
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
