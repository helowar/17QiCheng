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
import com.umeng.analytics.MobclickAgent;

import java.util.List;


/**
 * 原图显示的主Fragment
 */
@SuppressLint("ValidFragment")
public class OriginalPictureFragment extends Fragment {

    public static OriginalPictureFragment newInstance(String imageUrl) {
        final OriginalPictureFragment f = new OriginalPictureFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_original_picture, null);
        ImageView pic = (ImageView) view.findViewById(R.id.image);
        String url = getArguments().getString("url");
        ImageManager.displayImageDefault(url, pic);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("OriginalPictureFragment");
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("OriginalPictureFragment");
        MobclickAgent.onPause(getActivity());
    }
}
