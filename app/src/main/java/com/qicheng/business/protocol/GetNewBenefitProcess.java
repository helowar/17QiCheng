/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.Benefit;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/3/23.
 */
public class GetNewBenefitProcess extends BaseProcess {

    private static final Logger logger =  new Logger("com.qicheng.business.protocol.GetNewBenefitProcess");

    private static final String url = "/benefit/grab_benefit.html";

    private Benefit mBenefit;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            JSONObject o = new JSONObject(result);
            //获取结果代码
            int value = o.optInt("result_code");
            setProcessStatus(value);
            if(value==0){
                JSONObject jb = o.optJSONObject("body");
                //取出获得的优惠券
                mBenefit = new Benefit();
                mBenefit.setId(jb.optString("id"));
                mBenefit.setName(jb.optString("name"));
                mBenefit.setExpireTime(jb.optString("expire_time"));
                mBenefit.setValue(jb.optDouble("value"));
                mBenefit.setDescription(jb.optString("description"));
                mBenefit.setLogoUrl(jb.optString("logo_url"));
                mBenefit.setBgUrl(jb.optString("bg_url"));
            }
        }catch (JSONException e){
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public Benefit getResult(){
        return mBenefit;
    }
}
