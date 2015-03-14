/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic.event;

import com.qicheng.business.module.LabelType;
import com.qicheng.business.module.Photo;
import com.qicheng.business.module.User;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * UserPhotoEventArgs.java是启程APP的获取用户照片一览信息事件参数类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class UserPhotoEventArgs extends StatusEventArgs {

    /**
     * 用户照片一览对象
     */
    private List<Photo> photoList;

    public UserPhotoEventArgs(List<Photo> photoList, OperErrorCode errCode) {
        super(errCode);
        this.photoList = photoList;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }
}
