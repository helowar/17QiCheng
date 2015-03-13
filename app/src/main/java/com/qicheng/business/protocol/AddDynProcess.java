/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.DynFile;
import com.qicheng.business.ui.DynPublishActivity;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by NO3 on 2015/3/5.
 */
public class AddDynProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.AddDynProcess");
    private String url = "/activity/add.html";

    private DynPublishActivity.DynBody dynBody;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    /**
     * 组装添加动态的内容
     *
     * @return
     */
    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("content", dynBody.getContent());
            o.put("type", dynBody.getType());
            o.put("is_anms", dynBody.getIsAnms());
            if (dynBody.getQueryType() != null) {
                o.put("query_type", dynBody.getQueryType());
                o.put("query_value", dynBody.getQueryValue());
            }
            List<DynFile> files = dynBody.getFiles();
            if (dynBody.getFiles() != null) {
                JSONArray fileArray = new JSONArray();
                JSONObject fileObj = new JSONObject();
                fileObj.put("file_type", files.get(0).getFileType());
                fileObj.put("file_url", files.get(0).getFileUrl());
                fileArray.put(fileObj);
                o.put("files", fileArray);
            }
            logger.e("组装传入添加动态参数成功" + o.toString());
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入添加动态参数异常");
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
            if (value == Const.ResponseResultCode.RESULT_NOT_PERMIT) {
                setStatus(ProcessStatus.Status.ResultNotPermit);
            }
            setProcessStatus(value);
        } catch (Exception e) {
            logger.e("动态添加失败");
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setDynBody(DynPublishActivity.DynBody dynBody) {
        this.dynBody = dynBody;
    }
}
