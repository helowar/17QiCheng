/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */
package com.qicheng.business.ui.chat;

/**
 * HX SDK app model which will manage the user data and preferences
 * @author easemob
 *
 */
public abstract class HXSDKModel {
    public abstract void setSettingMsgNotification(boolean paramBoolean);
    public abstract boolean getSettingMsgNotification();

    public abstract void setSettingMsgSound(boolean paramBoolean);
    public abstract boolean getSettingMsgSound();

    public abstract void setSettingMsgVibrate(boolean paramBoolean);
    public abstract boolean getSettingMsgVibrate();

    public abstract void setSettingMsgSpeaker(boolean paramBoolean);
    public abstract boolean getSettingMsgSpeaker();
   
    public abstract boolean saveHXId(String hxId);
    public abstract String getHXId();
    
    public abstract boolean savePassword(String pwd);
    public abstract String getPwd();
    
    /**
     * 返回application所在的process name,默认是包名
     * @return
     */
    public abstract String getAppProcessName();
    /**
     * 是否总是接收好友邀请
     * @return
     */
    public boolean getAcceptInvitationAlways(){
        return true;
    }
    
    /**
     * 是否需要环信好友关系，默认是false
     * @return
     */
    public boolean getUseHXRoster(){
        return false;
    }
    
    /**
     * 是否需要已读回执
     * @return
     */
    public boolean getRequireReadAck(){
        return true;
    }
    
    /**
     * 是否需要已送达回执
     * @return
     */
    public boolean getRequireDeliveryAck(){
        return false;
    }
    
    /**
     * 是否运行在sandbox测试环境. 默认是关掉的
     * 设置sandbox 测试环境
     * 建议开发者开发时设置此模式
     */
    public boolean isSandboxMode(){
        return false;
    }
    
    /**
     * 是否设置debug模式
     * @return
     */
    public boolean isDebugMode(){
        return false;
    }
}
