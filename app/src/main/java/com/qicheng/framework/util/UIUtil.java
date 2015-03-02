/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.framework.util;

/**
 * Created by NO1 on 2015/2/28.
 */
public class UIUtil {

    /*
     * converts dip to px
     */
    public static int dip2Px(float dip,float density) {
        return (int) (dip * density + 0.5f);
    }
}
