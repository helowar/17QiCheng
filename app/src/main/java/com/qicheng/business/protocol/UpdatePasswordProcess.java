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
 * Created by 金玉龙 on 2015/3/26.
 * 启程App的修改密码的接口实现类
 */
public class UpdatePasswordProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.UpdatePasswordProcess");

    private String url = "/user/find_pwd.html";
    private String cellNum;
    private String newPwd;
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
            o.put("new_pwd", newPwd);
            o.put("verify_code", verifyCode);
            return RSACoder.getInstance().encryptByPublicKey(o.toString(), Cache.getInstance().getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            logger.d("组装修改密码参数失败");
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            JSONObject o = new JSONObject(result);
            //获取状态码
            int value = o.optInt("result_code");
            setProcessStatus(value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.d("更新密码失败");
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setCellNum(String cellNum) {
        this.cellNum = cellNum;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
