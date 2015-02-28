/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic;

import com.qicheng.business.module.Location;
import com.qicheng.business.protocol.LocationProcess;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.util.Logger;

/**
 * LocationProcess.java是启程APP的上报用户位置信息业务逻辑类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月27日
 */
public class LocationLogic extends BaseLogic {

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new LocationLogic();
        }
    }

    private static Logger logger = new Logger("com.qicheng.business.logic.LocationLogic");

    /**
     * 上报用户位置信息。
     *
     * @param location 用户位置信息
     */
    public void report(Location location) {
        final LocationProcess process = new LocationProcess();
        process.setLocation(location);
        process.run();
    }
}
