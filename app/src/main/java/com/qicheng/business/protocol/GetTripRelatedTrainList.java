/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/3/2.
 */
public class GetTripRelatedTrainList extends BaseProcess {

    private static final String url="/basedata/trip_train_list.html";

    private static final Logger logger = new Logger("GetTripRelatedTrainList");

    public String result;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        //获取状态码
        int value = o.optInt("result_code");
        logger.d("Get Trip related train list result:"+result);
        if(value ==0){
            /**
             * 获取列表并缓存
             */
            this.result = o.optString("body");
        }
        setProcessStatus(value);
    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
