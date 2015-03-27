/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONObject;

/**
 * Created by NO3 on 2015/3/26.
 */
public class UpdateCellNumProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.UpdateCellNumProcess");
    private String url = "/user/modify_phone.html";
    private String cellNum;
    private String verifyCode;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("cell_num", cellNum);
            o.put("verify_code", verifyCode);
            return RSACoder.getInstance().encryptByPublicKey(o.toString(), Cache.getInstance().getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            logger.d("组装修改手机号码参数失败");
        }
        return null;
    }

    @Override
    protected void onResult(JSONObject result) {
        //获取状态码
        int value = result.optInt("result_code");
        setProcessStatus(value);
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setCellNum(String cellNum) {
        this.cellNum = cellNum;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
