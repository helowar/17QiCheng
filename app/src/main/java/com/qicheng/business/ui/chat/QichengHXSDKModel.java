/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.chat;

import android.content.Context;

import com.qicheng.business.module.User;
import com.qicheng.business.ui.chat.db.DbOpenHelper;
import com.qicheng.business.ui.chat.db.UserDao;

import java.util.List;
import java.util.Map;

/**
 * Created by NO1 on 2015/3/10.
 */
public class QichengHXSDKModel extends DefaultHXSDKModel {

    public QichengHXSDKModel(Context ctx) {
        super(ctx);
    }

    // demo will use HuanXin roster
    public boolean getUseHXRoster() {
        // TODO Auto-generated method stub
        return true;
    }

    // demo will switch on debug mode
    public boolean isDebugMode(){
        return true;
    }

    public boolean saveContactList(List<User> contactList) {
        // TODO Auto-generated method stub
        UserDao dao = new UserDao(context);
        dao.saveContactList(contactList);
        return true;
    }

    public Map<String, User> getContactList() {
        // TODO Auto-generated method stub
        UserDao dao = new UserDao(context);
        return dao.getContactList();
    }

    public void closeDB() {
        // TODO Auto-generated method stub
        DbOpenHelper.getInstance(context).closeDB();
    }

    @Override
    public String getAppProcessName() {
        // TODO Auto-generated method stub
        return "com.qicheng";
    }
}
