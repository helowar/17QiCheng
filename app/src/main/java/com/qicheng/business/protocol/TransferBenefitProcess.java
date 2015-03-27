/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/3/23.
 */
public class TransferBenefitProcess extends BaseProcess {

    private static final String url = "/benefit/interact.html";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.TransferBenefitProcess");

    private String mBenefitId;
    private String mTargetUserId;
    private String mTargetUserImId;

    private static final int ACTION_TRANSFER = 1;
    private static final int BENEFIT_TYPE = 0;

    public TransferBenefitProcess(String benefitId,String targetUserImId){
        this.mBenefitId = benefitId;
        this.mTargetUserImId = targetUserImId;
    }

    public TransferBenefitProcess(String benefitId){
        this.mBenefitId = benefitId;
    }

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("benefit_entity_id",mBenefitId);
            if(!StringUtil.isEmpty(mTargetUserImId)){
                o.put("user_im_id",mTargetUserImId);
            }
            if(!StringUtil.isEmpty(mTargetUserId)){
                o.put("user_id",mTargetUserId);
            }
            o.put("thing_type",BENEFIT_TYPE);
            o.put("action",ACTION_TRANSFER);
            return o.toString();
        }catch (JSONException e){
            logger.e("AddContactProcess.getInfoParameter error"+e.getMessage());
            return null;
        }
    }

    @Override
    protected void onResult(JSONObject o) {
        //获取结果代码
        int value = o.optInt("result_code");
        setProcessStatus(value);
        if(value==0){

        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public String getBenefitId() {
        return mBenefitId;
    }

    public void setBenefitId(String benefitId) {
        mBenefitId = benefitId;
    }

    public String getTargetUserId() {
        return mTargetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        mTargetUserId = targetUserId;
    }
}
