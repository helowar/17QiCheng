/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.framework.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by NO1 on 2015/2/28.
 */
public class UIUtil {

    /*
     * converts dip to px
     */
    public static int dip2Px(float dip, float density) {
        return (int) (dip * density + 0.5f);
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param activity
     * @return int 宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }
}
