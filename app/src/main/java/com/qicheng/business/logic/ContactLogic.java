/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic;

import com.qicheng.business.logic.event.ContactEventArgs;
import com.qicheng.business.protocol.AddContactProcess;
import com.qicheng.business.protocol.GetContactListProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;

/**
 * Created by NO1 on 2015/3/20.
 */
public class ContactLogic extends BaseLogic {

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new ContactLogic();
        }
    }


    private static Logger logger = new Logger("com.qicheng.business.logic.ContactLogic");

    public void addContactUser(String imId,String source){
        final AddContactProcess process = new AddContactProcess(imId,source);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                if(errCode!=OperErrorCode.Success){
                    process.run(this);
                }
            }
        });
    }

    public void getContactList(final EventListener listener){
        final GetContactListProcess process = new GetContactListProcess();
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("GetContactListProcess response, " + errCode);
                ContactEventArgs contactEventArgs = new ContactEventArgs(errCode,process.getContactList());
                //发送事件
                fireEvent(listener, contactEventArgs);
            }
        });
    }
}
