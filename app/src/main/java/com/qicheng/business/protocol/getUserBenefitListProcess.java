/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * Created by 金玉龙 on 2015/3/21.
 * 启程APP的获取用户福利列表的接口处理类
 */
public class getUserBenefitListProcess extends BaseProcess {
    private String url = "/benefit/user_benefit_list.html";

    /**
     * 福利类型ID
     */
    private String benefitTypeId;

    /**
     * 物品福利类型 0：虚拟物品福利 1：实物物品福利
     */
    private int thingType;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("thing_type", thingType);
            o.put("benefit_type_id", benefitTypeId);
            return o.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            JSONObject o = new JSONObject(result);
            int value = o.optInt(STATUS_TAG);
            setProcessStatus(value);
            if (value == Const.ResponseResultCode.RESULT_SUCCESS) {
                JSONArray benefitArray = o.has("body") ? o.optJSONArray("body") : null;
                if(benefitArray!=null&&benefitArray.length()>0){

                }
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setBenefitTypeId(String benefitTypeId) {
        this.benefitTypeId = benefitTypeId;
    }

    public void setThingType(int thingType) {
        this.thingType = thingType;
    }
}
