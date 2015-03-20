/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

/**
 * Created by NO1 on 2015/3/20.
 */
public class AddContactProcess extends BaseProcess {

    private static final String url ="";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.AddContactProcess");

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

    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
