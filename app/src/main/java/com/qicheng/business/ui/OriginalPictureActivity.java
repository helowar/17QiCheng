/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.ui.component.DragImageView;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.util.BitmapUtils;
import com.qicheng.framework.util.UIUtil;
import com.qicheng.util.Const;

import java.io.InputStream;

public class OriginalPictureActivity extends BaseActivity {

    private int window_width, window_height;// 控件宽度
    private DragImageView dragImageView;// 自定义控件
    private int state_height;// 状态栏的高度

    private ViewTreeObserver viewTreeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(Const.Intent.ORIGINAL_PICTURE_URL_KEY);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_original_picture);
        /** 获取可見区域高度 **/
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        dragImageView = (DragImageView) findViewById(R.id.div_main);
        dragImageView.setAdjustViewBounds(true);
        dragImageView.setMaxHeight(window_width);
        dragImageView.setLayoutParams(new LinearLayout.LayoutParams(window_width,LinearLayout.LayoutParams.WRAP_CONTENT));
        ImageManager.displayImageDefault(url, dragImageView);
        dragImageView.setmActivity(this);//注入Activity.
        /** 测量状态栏高度 **/
        viewTreeObserver = dragImageView.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (state_height == 0) {
                            // 获取状况栏高度
                            Rect frame = new Rect();
                            getWindow().getDecorView()
                                    .getWindowVisibleDisplayFrame(frame);
                            state_height = frame.top;
                            dragImageView.setScreen_H(window_height-state_height);
                            dragImageView.setScreen_W(window_width);
                        }

                    }
                });
    }


}
