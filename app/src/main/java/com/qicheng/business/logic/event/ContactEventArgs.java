/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic.event;

import com.qicheng.business.module.User;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.List;

/**
 * Created by NO1 on 2015/3/20.
 */
public class ContactEventArgs extends StatusEventArgs {

    private List<User> contactList;

    public ContactEventArgs(OperErrorCode errCode,List<User> list){
        super(errCode);
        this.contactList = list;
    }

    @Override
    public OperErrorCode getErrCode() {
        return super.getErrCode();
    }

    public List<User> getContactList() {
        return contactList;
    }
}
