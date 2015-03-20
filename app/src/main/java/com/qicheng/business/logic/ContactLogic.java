/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic;

import com.qicheng.framework.logic.BaseLogic;
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

    }
}
