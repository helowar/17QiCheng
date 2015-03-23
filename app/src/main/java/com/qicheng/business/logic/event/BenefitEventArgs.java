/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.logic.event;

import com.qicheng.business.module.Benefit;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.List;

/**
 * Created by 金玉龙 on 2015/3/23.
 * 福利时间参数类
 */
public class BenefitEventArgs extends StatusEventArgs {

    private List list;

    public BenefitEventArgs(OperErrorCode errCode) {
        super(errCode);
    }

    public BenefitEventArgs(OperErrorCode errorCode, List list) {
        super(errorCode);
        this.list = list;
    }

    public List getBenefitList() {
        return list;
    }
}
