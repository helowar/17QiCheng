package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/3.
 */
public class VerifyCodeProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.VerifyCodeProcess");

    private final String url = "/user/verify_code_get.html";

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
            return RSACoder.getInstance().encryptByPublicKey(o.toString(), Cache.getInstance().getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        //获取状态码
        int value = o.optInt("result_code");
        setProcessStatus(value);
    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
