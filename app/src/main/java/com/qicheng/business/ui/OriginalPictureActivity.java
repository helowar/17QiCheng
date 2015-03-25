/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.os.Bundle;
import android.view.Window;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.ui.component.PhotoView;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

public class OriginalPictureActivity extends BaseActivity {

    private int window_width, window_height;// 控件宽度
    private PhotoView image;// 自定义控件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(Const.Intent.ORIGINAL_PICTURE_URL_KEY);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_original_picture);
        image = (PhotoView) findViewById(R.id.image);
        ImageManager.displayImageDefault(url, image);
    }


}
