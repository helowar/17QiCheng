/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.easemob.util.HanziToPinyin;
import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;

/**
 * Created by NO1 on 2015/3/20.
 */
public class GetContactListProcess extends BaseProcess {

    private static final String url = "";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.GetContactListProcess");

    @Override
    protected String getRequestUrl() {
        return null;
    }

    @Override
    protected String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(String result) {

    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param user
     */
    protected void setUserHearder(User user) {
        String headerName = null;
        if (!StringUtil.isEmpty(user.getNickName())) {
            headerName = user.getNickName();
        } else {
            headerName = user.getUserName();
        }
        if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }
}
