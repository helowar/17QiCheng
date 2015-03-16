/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.module.Label;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

public class OriginalPictureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_original_picture);
        String url = getIntent().getStringExtra(Const.Intent.ORIGINAL_PICTURE_URL_KEY);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        ImageView originalPic = (ImageView) findViewById(R.id.original_pic);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        originalPic.setAdjustViewBounds(true);
        originalPic.setMaxHeight(screenHeight);
        originalPic.setMaxWidth(screenWidth);
        originalPic.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,LinearLayout.LayoutParams.WRAP_CONTENT));
        ImageManager.displayImageDefault(url, originalPic);
    }
}
