/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic.event;

import com.qicheng.business.module.Dyn;
import com.qicheng.business.module.TrainStation;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO3 on 2015/2/28.
 */
public class DynEventAargs extends StatusEventArgs {
    private ArrayList<Dyn> dynList;

    private ArrayList<TrainStation> stationList;


    public DynEventAargs(OperErrorCode errorCode) {
        super(errorCode);
    }

    public DynEventAargs(ArrayList value) {
        super(OperErrorCode.Success);
        dynList = value;
    }

    public DynEventAargs(ArrayList value, OperErrorCode errCode) {
        super(errCode);
        dynList = value;

    }

    public ArrayList<Dyn> getDynList() {
        return dynList;
    }

    public ArrayList<TrainStation> getStationList() {
        return stationList;
    }
}
