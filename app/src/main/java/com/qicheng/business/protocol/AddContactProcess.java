/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/3/20.
 */
public class AddContactProcess extends BaseProcess {

    private static final String url ="/chat/add_friend.html";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.AddContactProcess");

    private String userImId;
    private String friendSource;

    public  AddContactProcess(String imId,String source){
        userImId = imId;
        friendSource = source;
    }

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("user_im_id",userImId);
            o.put("source",friendSource);
            return o.toString();
        }catch (JSONException e){
            logger.e("AddContactProcess.getInfoParameter error"+e.getMessage());
            return null;
        }
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
