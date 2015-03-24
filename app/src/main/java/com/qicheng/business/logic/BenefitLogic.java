/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic;

import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.protocol.GetBenefitListProcess;
import com.qicheng.business.protocol.GetBenefitRankListProcess;
import com.qicheng.business.protocol.GetNewBenefitProcess;
import com.qicheng.business.protocol.InitBenefitViewProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.protocol.TransferBenefitProcess;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;

/**
 * Created by 金玉龙 on 2015/3/23.
 * 福利Benefit相关logic类
 */
public class BenefitLogic extends BaseLogic {

    /*工厂方法创建BenefitLogic*/
    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new BenefitLogic();
        }
    }

    /**
     * 获取福利列表
     *
     * @param benefitTypeId
     * @param thingType
     * @param listener
     */
    public void getBenefitList(String benefitTypeId, int thingType, final EventListener listener) {
        final GetBenefitListProcess process = new GetBenefitListProcess();
        process.setBenefitTypeId(benefitTypeId);
        process.setThingType(thingType);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                BenefitEventArgs benefitEventArgs = new BenefitEventArgs(errCode, process.getBenefitList());
                fireEvent(listener, benefitEventArgs);
            }
        });

    }

    /**
     * 获取福利排名列表
     *
     * @param benefitTypeId
     * @param listener
     */
    public void getBenefitRankList(String benefitTypeId, final EventListener listener) {
        final GetBenefitRankListProcess process = new GetBenefitRankListProcess();
        process.setBenefitTypeId(benefitTypeId);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                BenefitEventArgs benefitEventArgs = new BenefitEventArgs(errCode, process.getRankList());
                fireEvent(listener, benefitEventArgs);
            }
        });
    }

    /**
     * 获取福利页面初始化信息
     */
    public void initBenefitView(final EventListener listener){
        EventListener l = listener;
        final InitBenefitViewProcess process = new InitBenefitViewProcess();
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                UserEventArgs userEventArgs = new UserEventArgs(process.getResult(),errCode);
                fireEvent(listener,userEventArgs);
            }
        });
    }

    /**
     * 摇一摇获取新的福利
     * @param listener
     */
    public void getNewBenefit(final EventListener listener){
        final GetNewBenefitProcess process = new GetNewBenefitProcess();
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                BenefitEventArgs benefitEventArgs = new BenefitEventArgs(errCode,process.getResult());
                fireEvent(listener,benefitEventArgs);
            }
        });
    }

    /**
     * 转送福利（对结果不显示）
     * @param benefitId
     * @param targetUserImId
     */
    public void transferBenefit(String benefitId,String targetUserImId){
        final TransferBenefitProcess process = new TransferBenefitProcess(benefitId,targetUserImId);
        process.run();
    }
}
