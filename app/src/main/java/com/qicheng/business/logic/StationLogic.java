/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic;

import com.qicheng.business.logic.event.StationEventAargs;
import com.qicheng.business.protocol.GetStationListProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;

/**
 * StationLogic.java是启程APP的查询车站信息业务逻辑类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月6日
 */
public class StationLogic extends BaseLogic {

    private static Logger logger = new Logger("com.qicheng.business.logic.StationLogic");

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new StationLogic();
        }
    }

    /**
     * 根据城市代码，查询所属该城市的车站信息。
     *
     * @param cityCode 城市代码
     * @param listener 查询结果事件监听器
     */
    public void queryStationByCityCode(String cityCode, final EventListener listener) {
        final GetStationListProcess process = new GetStationListProcess();
        process.setCityCode(cityCode);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("查询车站信息结果码为：" + errCode);
                StationEventAargs stationEventAargs = new StationEventAargs(process.getStationList(), errCode);
                fireEvent(listener, stationEventAargs);
            }
        });
    }
}
