/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.Photo;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.framework.util.JSONUtil.BODY_TAG;
import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * GetUserPhotoListProcess.java是启程APP的获取用户照片一览接口处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class GetUserPhotoListProcess extends BaseProcess {

    private static final Logger logger = new Logger("com.qicheng.business.protocol.GetUserPhotoListProcess");

    /**
     * 获取用户照片一览接口URL
     */
    private static final String url = "/user/get_photo_list.html";

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 查询方向 0：往最新方向查询 1：往最早方向查询
     */
    private byte orderBy;

    /**
     * 当order_by=0时，该值为在客户端里的最新值；
     * 当order_by=1时，该值为在客户端里的最早值；
     * 如果为空，表示获取最新数据。
     */
    private long orderNum;

    /**
     * 查询个数，默认5个
     */
    private int size = 5;

    /**
     * 查询结果：用户照片一览对象
     */
    private List<Photo> photoList;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        // 组装传入服务端参数
        try {
            JSONObject o = new JSONObject();
            o.put("id", userId);
            o.put("order_by", orderBy);
            o.put("order_num", orderNum);
            o.put("size", size);
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入查询用户照片一览参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            // 取回的JSON结果
            JSONObject o = new JSONObject(result);
            // 获取状态码
            int resultCode = o.optInt(STATUS_TAG);
            setProcessStatus(resultCode);
            if (resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                // 获取用户的相册数组
                JSONArray photoJSONArray = o.has(BODY_TAG) ? o.optJSONArray(BODY_TAG) : null;
                if (photoJSONArray != null && photoJSONArray.length() > 0) {
                    int length = photoJSONArray.length();
                    photoList = new ArrayList<Photo>(length);
                    for (int i = 0; i < length; i++) {
                        JSONObject photoJSONObject = photoJSONArray.getJSONObject(i);
                        Photo photo = new Photo();
                        photo.setThumbnailUrl(photoJSONObject.optString("thumbnail_url"));
                        photo.setPhotoUrl(photoJSONObject.optString("photo_url"));
                        photo.setOrderNum(photoJSONObject.optLong("order_num"));
                        photoList.add(photo);
                    }
                }
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理查询用户照片一览响应结果时异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setInfoParameter(String userId, byte orderBy, long orderNum, int size) {
        this.userId = userId;
        this.orderBy = orderBy;
        this.orderNum = orderNum;
        this.size = size;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }
}
