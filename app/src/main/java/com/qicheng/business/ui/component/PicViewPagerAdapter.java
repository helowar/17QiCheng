/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui.component;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qicheng.business.module.Photo;
import com.qicheng.business.ui.OriginalPictureFragment;

import java.util.List;

/**
 * Created by NO3 on 2015/3/24.
 */
public class PicViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Photo> photos;

    public PicViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public PicViewPagerAdapter(FragmentManager fm, List<Photo> photos) {
        super(fm);
        this.photos = photos;
    }

    @Override
    public Fragment getItem(int i) {
        String url = photos.get(i).getPhotoUrl();
        return OriginalPictureFragment.newInstance(url);
    }

    @Override
    public int getCount() {
        return photos.size();
    }


}
