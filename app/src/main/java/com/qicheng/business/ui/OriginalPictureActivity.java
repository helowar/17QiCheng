/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.module.Photo;
import com.qicheng.business.ui.component.PhotoView;
import com.qicheng.business.ui.component.PicViewPagerAdapter;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;

public class OriginalPictureActivity extends BaseActivity {

    private int window_width, window_height;// 控件宽度
    private PhotoView image;// 自定义控件

    private ViewTreeObserver viewTreeObserver;
    private ViewPager mViewPager;
    private PicViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String url = getIntent().getStringExtra(Const.Intent.ORIGINAL_PICTURE_URL_KEY);
        int index = getIntent().getExtras().getInt("index");
        List<Photo> photos = (ArrayList<Photo>) getIntent().getExtras().get("photos");
        String url = photos.get(index).getPhotoUrl();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_original_picture);
        /** 获取可見区域高度 **/
//        WindowManager manager = getWindowManager();
//        window_width = manager.getDefaultDisplay().getWidth();
//        window_height = manager.getDefaultDisplay().getHeight();
//        image = (PhotoView) findViewById(R.id.image);
//        ImageManager.displayImageDefault(url, image);
        mViewPager = (ViewPager) findViewById(R.id.id_pager);
        //mAdapter = new PicViewPagerAdapter(getSupportFragmentManager(),photos);
        mViewPager.setAdapter(mAdapter);

    }


}
