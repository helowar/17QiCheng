/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.Location;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONObject;

import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * LocationProcess.java是启程APP的上报用户位置信息接口处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月27日
 */
public class LocationProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.LocationProcess");

    /**
     * 获取上报用户位置接口URL
     */
    private final String url = "/user/report_location.html";

    /**
     * 用户位置信息
     */
    private Location location;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        // 组装传入服务端参数
        try {
            JSONObject o = new JSONObject();
            o.put("longitude", location.getLongitude());
            o.put("latitude", location.getLatitude());
            o.put("city", location.getCity());
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入上报用户位置参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        try {
            // 取回的JSON结果

            // 获取状态码
            int resultCode = o.optInt(STATUS_TAG);
            setProcessStatus(resultCode);
            if(resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                logger.d("上报用户位置信息成功");
            } else {
                logger.e("上报用户位置信息失败");
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理上报用户位置响应结果时异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
