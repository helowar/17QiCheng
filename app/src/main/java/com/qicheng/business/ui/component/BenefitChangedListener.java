/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.component;

import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;

/**
 * Created by NO1 on 2015/3/25.
 */
public class BenefitChangedListener {

    private BadgeView benefitBadge;
    private boolean initialized;

    public void initBenefitBadge(){
        if(benefitBadge!=null){
            BenefitLogic logic = (BenefitLogic)LogicFactory.self().get(LogicFactory.Type.Benefit);
            logic.initBenefitView(new EventListener() {
                @Override
                public void onEvent(EventId id, EventArgs args) {
                    UserEventArgs userEventArgs =(UserEventArgs)args;
                    if(userEventArgs.getErrCode()== OperErrorCode.Success){
                        benefitBadge.setBadgeCount(userEventArgs.getResult().getValidBenefitCount());
                    }
                }
            });
        }
    }

    public void updateBenefitBadge(int i){
        if(benefitBadge!=null){
            benefitBadge.setBadgeCount(i);
        }
    }

    public static BenefitChangedListener create(){
        BenefitChangedListener ins = new BenefitChangedListener();
        ins.initialized=false;
        return ins;
    }

    private BenefitChangedListener(){

    }

    public void setBenefitBadge(BadgeView benefitBadge){
        this.benefitBadge = benefitBadge;
        this.initialized = true;
    }

    public boolean isInitialized(){
        return initialized;
    }

}
