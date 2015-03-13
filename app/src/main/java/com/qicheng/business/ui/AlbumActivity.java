/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class AlbumActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
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
        private Integer[] imgs = {
                R.drawable.ic_city, R.drawable.ic_channel,
                R.drawable.ic_meet, R.drawable.ic_place
        };

        /**
         * 查询参数对应的图标名称数组对象
         */
        private Integer[] iconNames = {
                R.string.city_btn_text, R.string.train_btn_text,
                R.string.relation_btn_text, R.string.nearby_btn_text
        };

        AlbumGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return imgs.length;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            DisplayMetrics dm = new DisplayMetrics();
            //取得窗口属性
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            //窗口的宽度
            int screenWidth = dm.widthPixels;
            View view = null;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.query_params, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.query_params_image_view);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, screenWidth / 5));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(6, 6, 6, 0);//设置间距
                imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
                TextView textView = (TextView) view.findViewById(R.id.query_params_text_view);
                textView.setText(iconNames[position]);
            } else {
                view = convertView;
            }
            return view;
        }
    }

}
