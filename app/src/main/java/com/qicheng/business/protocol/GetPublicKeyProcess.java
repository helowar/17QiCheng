package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.framework.protocol.BaseProcess;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/3.
 */
public class GetPublicKeyProcess extends BaseProcess {

    private final String url = "http://192.168.1.107:8080/qps/common/get_public_key.html";

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
            switch (value) {
                case 0:
                    setStatus(ProcessStatus.Status.Success);
                    /**
                     * 获取公钥并持久化
                     */
                    JSONObject key = o.optJSONObject("body");
                    String keyString = key.getString("public_key");
                    Cache.getInstance().setPublicKey(keyString);
                    break;
                case 1:
                    setStatus(ProcessStatus.Status.IllegalRequest);
                    break;
                default:
                    setStatus(ProcessStatus.Status.ErrUnkown);
                    break;
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
}
