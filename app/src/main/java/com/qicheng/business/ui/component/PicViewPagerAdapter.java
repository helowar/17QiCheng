/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qicheng.business.ui.OriginalPictureFragment;

/**
 * Created by NO3 on 2015/3/24.
 */
public class PicViewPagerAdapter extends FragmentPagerAdapter {

    private String[] url;

    public PicViewPagerAdapter(FragmentManager fm, String[] url) {
        super(fm);
        this.url = url;
    }

    @Override
    public Fragment getItem(int i) {
        OriginalPictureFragment fragment = new OriginalPictureFragment(i, url);
        return fragment;
    }

    @Override
    public int getCount() {
        return url.length;
    }


}
