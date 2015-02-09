package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/4.
 */
public class RegisterProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.RegisterProcess");

    private String url = "/user/register.html";

    private User mParamUser;


    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("cell_num", mParamUser.getCellNum());
            o.put("pwd", mParamUser.getPassWord());
            o.put("verify_code", mParamUser.getVerifyCode());
            return RSACoder.getInstance().encryptByPublicKey(o.toString(), Cache.getInstance().getPublicKey());
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
            if(value==0){
                JSONObject resultBody = o.getJSONObject("body");
                String token = resultBody.optString("token");
                String portraitUrl = resultBody.optString("portrait_url");
                logger.d("Get users token:" + token);
                logger.d("Get user portraitUrl" + portraitUrl);
                User user = new User();
                user.setPortraitURL(portraitUrl);
                user.setToken(token);
                user.setCellNum(mParamUser.getCellNum());
                user.setPassWord(mParamUser.getPassWord());
                /**
                 * 刷新缓存
                 */
                Cache.getInstance().clear();
                Cache.getInstance().setCacheUser(user);
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

    public User getParamUser() {
        return mParamUser;
    }

    public void setParamUser(User paramUser) {
        mParamUser = paramUser;
    }
}
