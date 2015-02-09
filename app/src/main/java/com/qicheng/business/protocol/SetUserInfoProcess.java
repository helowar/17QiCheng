package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

/**
 * Created by NO1 on 2015/2/9.
 */
public class SetUserInfoProcess extends BaseProcess{

    private static Logger logger = new Logger("com.qicheng.business.protocol.SetUserInfoProcess");

    private String url = "http://192.168.1.107:8080/qps/user/modify.html";

    private User mParamUser;

//    private List<>

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

    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
