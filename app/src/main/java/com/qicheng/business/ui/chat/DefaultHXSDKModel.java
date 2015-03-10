/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.ui.chat;

/**
 * UI Demo HX Model implementation
 */

//import com.easemob.chatuidemo.db.UserDao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * HuanXin default SDK Model implementation
 * @author easemob
 *
 */
public class DefaultHXSDKModel extends HXSDKModel{
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PWD = "pwd";
//    UserDao dao = null;
    protected Context context = null;
    
    public DefaultHXSDKModel(Context ctx){
        context = ctx;
        HXPreferenceUtils.init(context);
    }
    
    @Override
    public void setSettingMsgNotification(boolean paramBoolean) {
        // TODO Auto-generated method stub
        HXPreferenceUtils.getInstance().setSettingMsgNotification(paramBoolean);
    }

    @Override
    public boolean getSettingMsgNotification() {
        // TODO Auto-generated method stub
        return HXPreferenceUtils.getInstance().getSettingMsgNotification();
    }

    @Override
    public void setSettingMsgSound(boolean paramBoolean) {
        // TODO Auto-generated method stub
        HXPreferenceUtils.getInstance().setSettingMsgSound(paramBoolean);
    }

    @Override
    public boolean getSettingMsgSound() {
        // TODO Auto-generated method stub
        return HXPreferenceUtils.getInstance().getSettingMsgSound();
    }

    @Override
    public void setSettingMsgVibrate(boolean paramBoolean) {
        // TODO Auto-generated method stub
        HXPreferenceUtils.getInstance().setSettingMsgVibrate(paramBoolean);
    }

    @Override
    public boolean getSettingMsgVibrate() {
        // TODO Auto-generated method stub
        return HXPreferenceUtils.getInstance().getSettingMsgVibrate();
    }

    @Override
    public void setSettingMsgSpeaker(boolean paramBoolean) {
        // TODO Auto-generated method stub
        HXPreferenceUtils.getInstance().setSettingMsgSpeaker(paramBoolean);
    }

    @Override
    public boolean getSettingMsgSpeaker() {
        // TODO Auto-generated method stub
        return HXPreferenceUtils.getInstance().getSettingMsgSpeaker();
    }

    @Override
    public boolean getUseHXRoster() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean saveHXId(String hxId) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_USERNAME, hxId).commit();
    }

    @Override
    public String getHXId() {
        // TODO Auto-generated method stub
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_USERNAME, null);
    }

    @Override
    public boolean savePassword(String pwd) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(PREF_PWD, pwd).commit();    
    }

    @Override
    public String getPwd() {
        // TODO Auto-generated method stub
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREF_PWD, null);
    }

    @Override
    public String getAppProcessName() {
        // TODO Auto-generated method stub
        return "com.qicheng";
    }
}
