/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic.event;

import com.qicheng.business.module.User;
import com.qicheng.business.module.UserDetail;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

/**
 * UserDetailEventArgs.java是启程APP的获取用户详细信息事件参数类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class UserDetailEventArgs extends StatusEventArgs {

    /**
     * 用户详细信息对象
     */
    private UserDetail userDetail;

    public UserDetailEventArgs(UserDetail userDetail, OperErrorCode errCode) {
        super(errCode);
        this.userDetail = userDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }
}
