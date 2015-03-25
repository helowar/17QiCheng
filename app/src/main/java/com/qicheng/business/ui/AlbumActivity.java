/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserPhotoEventArgs;
import com.qicheng.business.module.Photo;
import com.qicheng.business.ui.chat.activity.ShowBigImage;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.util.UIUtil;
import com.qicheng.util.Const;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends BaseActivity {

    private ArrayList<Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getPhotoList((byte) 0, 0l, 100);
    }

    /**
     * 获取用户照片接口
     */
    public void getPhotoList(byte orderBy, Long orderNum, int size) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        String userId = null;
        userLogic.getUserPhotoList(userId, orderBy, orderNum, size, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserPhotoEventArgs result = (UserPhotoEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    photoList = result.getPhotoList();
                    GridView photoGridView = (GridView) findViewById(R.id.photo_list);
                    photoGridView.setAdapter(new AlbumGridViewAdapter(getActivity(), photoList));
                }
            }
        }));
        startLoading();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 相册的GridView的适配器
     */
    class AlbumGridViewAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Photo> list;

        AlbumGridViewAdapter(Context context, ArrayList<Photo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int screenWidth = UIUtil.getScreenWidth(getActivity());
            ImageView imageView;
            View view = null;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.photo_param, null);
                imageView = (ImageView) view.findViewById(R.id.params_image_view);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, screenWidth / 4));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(1, 1, 1, 1);//设置间距
                ImageManager.displayPortrait(list.get(position).getThumbnailUrl(), imageView);//为ImageView设置图片资源
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AlbumItemActivity.class);
                        intent.putExtra("photos", list);
                        intent.putExtra("index", position);
                        intent.putExtra(Const.Intent.ORIGINAL_PICTURE_URL_KEY, list.get(position).getPhotoUrl());
                        startActivity(intent);
                    }
                });

            } else {
                view = convertView;
            }
            return view;
        }

    }


}
