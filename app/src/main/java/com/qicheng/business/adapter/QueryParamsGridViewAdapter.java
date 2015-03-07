/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.qicheng.R;

/**
 * QueryParamsGridViewAdapter.java是启程APP的查询参数适配器类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月4日
 */
public class QueryParamsGridViewAdapter extends BaseAdapter {

    /**
     * 查询参数所属的上下文对象
     */
    private Context context;

    /**
     * 查询参数所属的页面对象
     */
    private Activity activity;

    /**
     * 查询参数对应的图标数组对象
     */
    private Integer[] imgs = {
            R.drawable.ic_city, R.drawable.ic_channel,
            R.drawable.ic_place, R.drawable.ic_meet
    };

    /**
     * 构造函数
     *
     * @param context  查询参数所属的上下文对象
     * @param activity 查询参数所属的页面对象
     */
    public QueryParamsGridViewAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int i) {
        return imgs[i];
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
        // 取得窗口属性
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 窗口的宽度
        int screenWidth = dm.widthPixels;
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(context);
            // 设置ImageView对象布局
            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 4, screenWidth / 4));
            // 设置边界对齐
            imageView.setAdjustViewBounds(false);
            // 设置刻度的类型
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 设置间距
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }
        // 为ImageView设置图片资源
        imageView.setImageResource(imgs[position]);
        return imageView;
    }
}
