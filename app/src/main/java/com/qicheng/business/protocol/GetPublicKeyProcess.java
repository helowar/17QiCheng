package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.framework.protocol.BaseProcess;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/3.
 */
public class GetPublicKeyProcess extends BaseProcess {

    private final String url = "/common/get_public_key.html";

    @Override
    public String getRequestUrl() {
        return url;
    }

    @Override
    public String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        //获取状态码
        int value = o.optInt("result_code");
        if(value ==0){
            /**
             * 获取公钥并持久化
             */
            JSONObject key = o.optJSONObject("body");
            String keyString = key.optString("public_key");
            Cache.getInstance().setPublicKey(keyString);
        }
        setProcessStatus(value);
    }

    public void storePublicKey(JSONObject o){
        JSONObject key = o.optJSONObject("body");
        String keyString = key.optString("public_key");
        Cache.getInstance().setPublicKey(keyString);
    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
