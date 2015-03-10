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

public class OriginalPictureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_original_picture);
        String url = getIntent().getStringExtra("imgurl");
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
       // originalPic.setImageResource(R.drawable.ic_test_img);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_original_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
