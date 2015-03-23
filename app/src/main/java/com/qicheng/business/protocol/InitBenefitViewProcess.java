/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NO1 on 2015/3/23.
 */
public class InitBenefitViewProcess extends BaseProcess {

    private static final String url = "/benefit/get_benefit_num.html";

    private User mUser;

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
        try{
            JSONObject o = new JSONObject(result);
            //获取结果代码
            int value = o.optInt("result_code");
            setProcessStatus(value);
            if(value==0){
                JSONObject js = o.optJSONObject("body");
                mUser = new User();
                mUser.setValidBenefitCount(js.optInt("benefit_num"));
                mUser.setFriendCount(js.optInt("friend_num"));
            }
        }catch (JSONException e){

        }

    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public User getResult(){
        return mUser;
    }
}
