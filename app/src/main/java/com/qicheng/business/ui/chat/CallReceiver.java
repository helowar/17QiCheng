/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class CallReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//拨打方username
		String from = intent.getStringExtra("from");
		//call type
		String type = intent.getStringExtra("type");
//		if("video".equals(type)){ //视频通话
//		    context.startActivity(new Intent(context, VideoCallActivity.class).
//                    putExtra("username", from).putExtra("isComingCall", true).
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//		}else{ //音频通话
//		    context.startActivity(new Intent(context, VoiceCallActivity.class).
//		            putExtra("username", from).putExtra("isComingCall", true).
//		            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//		}
	}

}
