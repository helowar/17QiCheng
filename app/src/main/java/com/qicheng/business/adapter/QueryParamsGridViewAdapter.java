/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;

/**
 * QueryParamsGridViewAdapter.java是启程APP的查询参数适配器类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月4日
 */
public class QueryParamsGridViewAdapter extends BaseAdapter {

    /**
     * 查询参数所属的页面对象
     */
    private Activity activity;

    /**
     * 查询参数对应的图标数组对象
     */
    private Integer[] icons = {
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

    /**
     * 构造函数
     *
     * @param activity 查询参数所属的页面对象
     */
    public QueryParamsGridViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int i) {
        return icons[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 获取一个ImageView对象。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        View view = null;
        if (convertView == null) {
            view = activity.getLayoutInflater().inflate(R.layout.query_params, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.query_params_image_view);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, screenWidth / 5));//设置ImageView对象布局
            imageView.setAdjustViewBounds(false);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(6, 6, 6, 0);//设置间距
            imageView.setImageResource(icons[position]);//为ImageView设置图片资源
            TextView textView = (TextView) view.findViewById(R.id.query_params_text_view);
            textView.setText(iconNames[position]);
        } else {
            view = convertView;
        }
        return view;
    }
}
