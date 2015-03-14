/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.Photo;
import com.qicheng.business.module.UserDetail;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * GetUserDetailProcess.java是启程APP的获取用户详细信息接口处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class GetUserDetailProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.GetUserDetailProcess");

    /**
     * 获取用户详细信息接口URL
     */
    private final String url = "/user/get_detail.html";

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 查询结果：用户详细信息对象
     */
    private UserDetail userDetail;

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
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入获取用户详细信息参数异常");
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
            if(resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                // 获取查询结果：用户详细信息对象
                JSONObject body = JSONUtil.getResultBody(o);
                if (body != null) {
                    userDetail = new UserDetail();
                    userDetail.setUserId(body.optString("user_id"));
                    userDetail.setUserIMId(body.optString("user_im_id"));
                    userDetail.setPortraitUrl(body.optString("portrait_url"));
                    userDetail.setNickname(body.optString("nickname"));
                    userDetail.setGender(body.optInt("gender"));
                    userDetail.setBirthday(body.optString("birthday"));
                    userDetail.setOnline(body.optInt("online"));
                    userDetail.setActivityNum(body.optInt("activity_num"));
                    userDetail.setResidence(body.optString("residence"));
                    userDetail.setHometown(body.optString("hometown"));
                    userDetail.setEducation(body.optString("education"));
                    userDetail.setIndustry(body.optString("industry"));
                    // 获取用户的标签数组
                    JSONArray tagJSONArray = body.has("tags") ? body.optJSONArray("tags") : null;
                    if(tagJSONArray != null && tagJSONArray.length() > 0) {
                        int length = tagJSONArray.length();
                        List<String> tags = new ArrayList<String>(length);
                        for(int i = 0; i < length; i++) {
                            tags.add(tagJSONArray.getString(i));
                        }
                        userDetail.setTags(tags);
                    }
                    // 获取用户的相册数组
                    JSONArray photoJSONArray = body.has("photo_list") ? body.optJSONArray("photo_list") : null;
                    if(photoJSONArray != null && photoJSONArray.length() > 0) {
                        int length = photoJSONArray.length();
                        List<Photo> photoList = new ArrayList<Photo>(length);
                        for(int i = 0; i < length; i++) {
                            JSONObject photoJSONObject = photoJSONArray.getJSONObject(i);
                            Photo photo = new Photo();
                            photo.setThumbnailUrl(photoJSONObject.optString("thumbnail_url"));
                            photo.setPhotoUrl(photoJSONObject.optString("photo_url"));
                            photo.setOrderNum(photoJSONObject.optLong("order_num"));
                            photoList.add(photo);
                        }
                        userDetail.setPhotoList(photoList);
                    }
                }
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理查询获取用户详细信息响应结果时异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }
}
