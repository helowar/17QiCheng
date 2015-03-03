/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.module;

import java.io.Serializable;

/**
 * Created by NO1 on 2015/3/2.
 */
public class Train implements Serializable {

    private String mTrainCode;

    public String getTrainCode() {
        return mTrainCode;
    }

    public void setTrainCode(String trainCode) {
        mTrainCode = trainCode;
    }
}
