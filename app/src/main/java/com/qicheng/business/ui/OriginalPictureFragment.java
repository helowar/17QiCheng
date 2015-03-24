/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.ui.component.PicViewPagerAdapter;
import com.qicheng.framework.ui.base.BaseFragment;


/**
 * 原图显示的主Fragment
 */
@SuppressLint("ValidFragment")
public class OriginalPictureFragment extends Fragment {
    private int position;
    private String[] url;


    public OriginalPictureFragment(int position, String[] url) {
        this.position = position;
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_original_picture, null);
        ImageView pic = (ImageView) view.findViewById(R.id.image);
        ImageManager.displayImageDefault(url[position], pic);
        return view;
    }
}
