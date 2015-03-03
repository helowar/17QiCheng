/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONObject;

/**
 * Created by NO3 on 2015/3/3.
 */
public class InteractProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.InteractProcess");
    private String url = "/activity/interact.html";
    private String id;
    private int action;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("id", id);
            o.put("action", action);
            return o.toString();
        } catch (Exception e) {
            logger.e("组装动态互动参数异常");
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
            setProcessStatus(value);
        } catch (Exception e) {
            logger.e("动态互动失败");
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
