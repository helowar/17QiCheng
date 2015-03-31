/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.StringUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;

/**
 * QiniuFileUpload.java是启程APP的上传文件到七牛云存储服务器处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月31日
 */
public class QiniuFileUpload {

    /**
     * 上传成功
     */
    public static final int SUCCESS = 1;

    /**
     * 上传失败，原因是上传令牌无效或过期
     */
    public static final int FAILURE_EXPIRE = 401;

    /**
     * 上传文件结果对象
     */
    private UploadResult result = new UploadResult();

    /**
     * 上传文件到七牛云存储服务器。
     *
     * @param file        文件对象
     * @param uploadToken 上传令牌
     */
    public UploadResult uploadFile(File file, String uploadToken) {
        try {
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            User user = Cache.getInstance().getUser();
            String userIdMD5 = StringUtil.MD5(user.getUserId());
            final String fileKey = userIdMD5 + DateTimeUtil.format(new Date()) + extension;
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(file, fileKey, uploadToken, new UpCompletionHandler() {
                @Override
                public void complete(String key, ResponseInfo info, JSONObject response) {
                    if (info.isOK()) {
                        result.setResult(SUCCESS);
                        result.setUrl(fileKey);
                    } else {
                        result.setResult(info.statusCode);
                    }
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public class UploadResult {

        private int result;

        private String url;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
