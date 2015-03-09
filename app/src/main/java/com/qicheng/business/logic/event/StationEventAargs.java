/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic.event;

import com.qicheng.business.module.TrainStation;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.List;

/**
 * Created by NO3 on 2015/2/28.
 */
public class StationEventAargs extends StatusEventArgs {

    private List<TrainStation> stationList;

    public StationEventAargs(List value, OperErrorCode errCode) {
        super(errCode);
        stationList = value;

    }

    public List<TrainStation> getStationList() {
        return stationList;
    }
}
