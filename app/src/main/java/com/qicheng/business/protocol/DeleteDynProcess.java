/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO3 on 2015/3/10.
 */
public class DeleteDynProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.DeleteDynProcess");
    private String url = "/activity/delete.html";

    private String activityId;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject object = new JSONObject();
            object.put("id", activityId);
            return object.toString();
        } catch (Exception e) {
            logger.e("组装删除动态参数异常");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            int value = object.optInt("result_code");
            setProcessStatus(value);
        } catch (JSONException e) {
            logger.e("删除动态参数异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
