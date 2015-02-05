package com.qicheng.business.protocol;

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

    private String url="http://192.168.1.107:8080/qps/user/register.html";

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
            o.put("verify_code",mParamUser.getVerifyCode());
            return RSACoder.getInstance().encryptByPublicKey(o.toString(), PersistorManager.getInstance().getPublicKey());
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
            int value = o.optInt("result_code");
            switch(value) {
                case 0:
                    setStatus(ProcessStatus.Status.Success);
                    JSONObject resultBody = o.getJSONObject("body");
                    String token = resultBody.optString("token");
                    String portraitUrl = resultBody.optString("portrait_url");
                    // TODO: 持久化token及portraitUrl，缓存token及portraitImg
                    logger.d("Get users token:"+token);
                    logger.d("Get user portraitUrl"+portraitUrl);
                    break;
                case 1:
                    setStatus(ProcessStatus.Status.IllegalRequest);
                    break;
                case 7:
                    setStatus(ProcessStatus.Status.ErrExistCellNum);
                    break;
                case 9:
                    setStatus(ProcessStatus.Status.ErrWrongVerCode);
                    break;
                case 10:
                    setStatus(ProcessStatus.Status.ErrVerCodeExpire);
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

    public User getParamUser() {
        return mParamUser;
    }

    public void setParamUser(User paramUser) {
        mParamUser = paramUser;
    }
}
