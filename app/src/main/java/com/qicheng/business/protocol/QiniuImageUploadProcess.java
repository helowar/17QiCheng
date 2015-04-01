/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import android.os.AsyncTask;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;

/**
 * QiniuImageUploadProcess.java是启程APP的上传文件到七牛云存储服务器处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月31日
 */
public class QiniuImageUploadProcess {

    /**
     * 文件用途 0：普通图片文件
     */
    public static final byte FILE_USAGE_SIMPLE = 0;

    /**
     * 文件用途 1:头像图片文件
     */
    public static final byte FILE_USAGE_AVATAR = 1;

    /**
     * 文件用途 2：视频文件
     */
    public static final byte FILE_USAGE_VIDEO = 2;

    /**
     * 文件用途 3：文本文件
     */
    public static final byte FILE_USAGE_TEXT = 3;

    /**
     * 上传失败，原因是上传令牌无效或过期
     */
    public static final int FAILURE_EXPIRE = 401;

    /**
     * 文件用途
     */
    private String requestId;

    /**
     * 文件用途
     */
    private byte fileUsage;

    /**
     * 文件对象
     */
    private File imageFile;

    /**
     * 文件返回URL
     */
    private String resultUrl = null;

    /**
     * 结果响应监听器
     */
    private ResponseListener mListener = null;

    /**
     * 通信结果错误码
     */
    private ProcessStatus.Status mStatus = ProcessStatus.Status.Success;

    /**
     * 七牛上传文件管理器
     */
    private UploadManager uploadManager = new UploadManager();

    public void run(String requestId, byte usage, File image, ResponseListener listener) {
        this.requestId = requestId;
        fileUsage = usage;
        imageFile = image;
        mListener = listener;
        uploadFile();
    }

    public ProcessStatus.Status getStatus() {
        return mStatus;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    /**
     * 上传文件到七牛云存储服务器。
     */
    private void uploadFile() {
        try {
            User cacheUser = Cache.getInstance().getUser();
            String uploadToken = null;
            if (fileUsage == FILE_USAGE_SIMPLE) {
                uploadToken = cacheUser.getImagesToken();
            } else if (fileUsage == FILE_USAGE_AVATAR) {
                uploadToken = cacheUser.getAvatarsToken();
            } else {
                mStatus = ProcessStatus.Status.ErrUnkown;
                mListener.onResponse(requestId);
                return;
            }
            String fileName = imageFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String userIdMD5 = StringUtil.MD5(cacheUser.getUserId());
            final String fileKey = userIdMD5 + DateTimeUtil.format(new Date()) + extension;
            uploadManager.put(imageFile, fileKey, uploadToken, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        mStatus = ProcessStatus.Status.Success;
                        resultUrl = '/' + fileKey;
                        mListener.onResponse(requestId);
                    } else {
                        if (info.statusCode == FAILURE_EXPIRE) {
                            mStatus = ProcessStatus.Status.ErrLoginTimeOut;
                            new AsyncComm(requestId, mListener).execute();
                        } else {
                            mStatus = ProcessStatus.Status.ErrUnkown;
                            mListener.onResponse(requestId);
                        }
                    }
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AsyncComm extends AsyncTask<Void, Void, Void> {

        private String mRequestId = "";
        private ResponseListener mListener = null;

        public AsyncComm(String requestId, ResponseListener listener) {
            mRequestId = requestId;
            mListener = listener;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // 重登录系统，获取新的上传文件令牌
            Const.Application.reLoginAndRepeat();
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            mListener.onResponse(mRequestId);
        }
    }
}
