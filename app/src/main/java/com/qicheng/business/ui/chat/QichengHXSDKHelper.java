/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.chat;

import android.content.Intent;
import android.content.IntentFilter;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.ChatActivity;
import com.qicheng.business.ui.MainActivity;
import com.qicheng.business.ui.chat.utils.CommonUtils;
import com.qicheng.business.ui.chat.utils.Constant;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

import java.util.Map;

/**
 * Created by NO1 on 2015/3/9.
 */
public class QichengHXSDKHelper extends HXSDKHelper {

    /**
     * contact list in cache
     */
    private Map<String, User> contactList;

    @Override
    protected void initHXOptions(){
        super.initHXOptions();
        // you can also get EMChatOptions to set related SDK options
        // EMChatOptions options = EMChatManager.getInstance().getChatOptions();
    }

    @Override
    protected OnMessageNotifyListener getMessageNotifyListener(){
        // 取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
        return new OnMessageNotifyListener() {

            @Override
            public String onNewMessageNotify(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = CommonUtils.getMessageDigest(message, appContext);
                if(message.getType() == EMMessage.Type.TXT)
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                String fromer = message.getStringAttribute(Const.Easemob.FROM_USER_NICK,message.getFrom());
                return fromer + ": " + ticker;
            }

            @Override
            public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
                return fromUsersNum + "位启友，发来了" + messageNum + "条消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage message) {
                //修改标题,这里使用默认
                return "新消息";
            }

            @Override
            public int onSetSmallIcon(EMMessage message) {
                //设置小图标
                return 0;
            }
        };
    }

    @Override
    protected OnNotificationClickListener getNotificationClickListener(){
        return new OnNotificationClickListener() {

            @Override
            public Intent onNotificationClick(EMMessage message) {
                Intent intent = new Intent(appContext, MainActivity.class);
                intent.putExtra(Const.Intent.HX_NTF_TO_MAIN,true);
//                EMMessage.ChatType chatType = message.getChatType();
//                if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
//                    intent.putExtra(Const.Intent.HX_USER_ID, message.getFrom());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                    try {
//                        intent.putExtra(Const.Intent.HX_USER_NICK_NAME, message.getStringAttribute(Const.Easemob.FROM_USER_NICK));
//                        intent.putExtra(Const.Intent.HX_USER_TO_CHAT_AVATAR, message.getStringAttribute(Const.Easemob.FROM_USER_AVATAR));
//                    }catch (Exception e){
//
//                    }
//                } else { // 群聊信息
//                    // message.getTo()为群聊id
//                    intent.putExtra("groupId", message.getTo());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                }
                return intent;
            }
        };
    }

    @Override
    protected void onConnectionConflict(){
//        Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("conflict", true);
//        appContext.startActivity(intent);
    }
    @Override
    protected void onCurrentAccountRemoved(){
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }


    @Override
    protected void initListener(){
        super.initListener();
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        appContext.registerReceiver(new CallReceiver(), callFilter);
    }

    @Override
    protected HXSDKModel createModel() {
        return new QichengHXSDKModel(appContext);
    }

    /**
     * get demo HX SDK Model
     */
    public QichengHXSDKModel getModel(){
        return (QichengHXSDKModel) hxModel;
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
//        if (getHXId() != null && contactList == null) {
//            contactList = ((DemoHXSDKModel) getModel()).getContactList();
//        }

        return contactList;
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }

    @Override
    public void logout(final EMCallBack callback){
        super.logout(new EMCallBack(){

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                setContactList(null);
                endCall();
                getModel().closeDB();
                if(callback != null){
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }

        });
    }

    void endCall(){
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
