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
 * Created by NO1 on 2015/3/23.
 */
public class TransferBenefitProcess extends BaseProcess {

    private static final String url = "/benefit/interact.html";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.TransferBenefitProcess");

    private String mBenefitId;
    private String mTargetUserId;

    private static final int ACTION_TRANSFER = 1;

    public TransferBenefitProcess(String benefitId,String targetUserId){
        this.mBenefitId = benefitId;
        this.mTargetUserId = targetUserId;
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
            o.put("user_id",mTargetUserId);
            o.put("action",ACTION_TRANSFER);
            return o.toString();
        }catch (JSONException e){
            logger.e("AddContactProcess.getInfoParameter error"+e.getMessage());
            return null;
        }
    }

    @Override
    protected void onResult(String result) {



    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
