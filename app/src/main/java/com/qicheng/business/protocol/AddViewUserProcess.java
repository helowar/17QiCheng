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

import org.json.JSONObject;

import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * AddViewUserProcess.java是启程APP的添加浏览用户记录接口处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月20日
 */
public class AddViewUserProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.AddViewUserProcess");

    /**
     * 添加浏览用户记录接口URL
     */
    private final String url = "/user/add_viewed.html";

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 添加结果
     */
    private boolean result = false;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        // 组装传入服务端参数
        try {
            JSONObject o = new JSONObject();
            o.put("user_id", userId);
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入添加浏览用户记录参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            // 取回的JSON结果
            JSONObject o = new JSONObject(result);
            // 获取状态码
            int resultCode = o.optInt(STATUS_TAG);
            setProcessStatus(resultCode);
            if (resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                this.result = true;
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理添加浏览用户记录响应结果时异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isResult() {
        return result;
    }
}
