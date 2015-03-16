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

/**
 * Created by NO3 on 2015/3/14.
 */
public class UpdateUserInformationProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.UpdateUserInformationProcess");

    private final static String url = "/user/update.html";

    private int updateType;

    private String updateValue;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            switch (updateType) {
                case Const.UserUpdateCode.UPDATE_NICKNAME:
                    o.put("nickname", updateValue);
                    break;
                case Const.UserUpdateCode.UPDATE_BIRTHDAY:
                    o.put("birthday", updateValue);
                    break;
                case Const.UserUpdateCode.UPDATE_PORTRAIT_URL:
                    o.put("portrait_url", updateValue);
                    break;
                case Const.UserUpdateCode.UPDATE_RESIDENCE:
                    o.put("residence", updateValue);
                    break;
                case Const.UserUpdateCode.UPDATE_HOMETOWN:
                    o.put("hometown", updateValue);
                    break;
                case Const.UserUpdateCode.UPDATE_EDUCATION:
                    o.put("education", updateValue);
                    break;
                case Const.UserUpdateCode.UPDATE_INDUSTRY:
                    o.put("industry", updateValue);
                    break;
            }
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入更新用户信息参数异常");
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
            logger.e("个人信息修改失败");
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public void setUpdateValue(String updateValue) {
        this.updateValue = updateValue;
    }
}
