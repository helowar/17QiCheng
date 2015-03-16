/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.util.DateTimeUtil;

import java.util.Calendar;
import java.util.Date;

public class UserSettingFragment extends BaseFragment {
    /* 请求码 */
    private static final int DATE_REQUEST_CODE = 3;

    private View view;
    private LinearLayout linearLayout;

    private TextView birthdayView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_infomation_modify, container, false);
        initView(inflater);
        return view;
    }

    /**
     * 初始化View视图
     *
     * @param inflater
     */
    public void initView(LayoutInflater inflater) {
        linearLayout = (LinearLayout) view.findViewById(R.id.label_scroll_root);
        User user = Cache.getInstance().getUser();
          /*个人头像*/
        initPortraitItem(inflater, R.string.personal_portrait_text, user.getPortraitURL());
        /*个人昵称*/
        initViewItem(inflater, R.string.personal_nickname_text, user.getNickName());
        /*出生日期*/
        initViewItem(inflater, R.string.personal_birthday_text, user.getBirthday());

        /*添加分割段*/
        addSeparation(inflater);

        /*行业*/
        initViewItem(inflater, R.string.personal_industry_text, "计算机");
        /*学历*/
        initViewItem(inflater, R.string.personal_education_text, "本科");
        /*所在地*/
        initViewItem(inflater, R.string.personal_residence_text, "杭州");
        /*家乡*/
        initViewItem(inflater, R.string.personal_home_text, "杭州");
        /*添加分割段*/
        addSeparation(inflater);

         /*手机号码*/
        initViewItem(inflater, R.string.personal_cell_text, user.getCellNum());


    }

    /**
     * 创建menu元素并加入到布局文件中
     *
     * @param inflater
     * @param stringID 字符串id
     * @param text     icon id
     */

    public void initViewItem(LayoutInflater inflater, final int stringID, final String text) {
        View view = inflater.inflate(R.layout.personal_information_text_tabel, null);
        TextView viewText = (TextView) view.findViewById(R.id.table_text);
        viewText.setText(stringID);
        final TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        /*绑定点击事件*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stringID) {
                    case R.string.personal_birthday_text:
                        showDatePickDialog(textView.getText().toString());
                        TextView textView = (TextView) view.findViewById(R.id.text);
                        birthdayView = textView;
                        break;
                    case R.string.my_label:
                        /*跳转到我的标签*/
                        break;
                    case R.string.my_photo:
                       /*跳转到我的相册*/
                        break;
                    case R.string.my_activity:
                        /*跳转到我的动态*/
                        break;
                    case R.string.select_setting:
                         /*跳转到筛选设置*/
                        break;
                    case R.string.account_setting:
                       /*跳转到账户设置*/
                        break;
                    default:
                        break;
                }
            }
        });
        linearLayout.addView(view);
    }

    private void addSeparation(LayoutInflater inflater) {
        View separation = inflater.inflate(R.layout.personal_information_separation, null);
        linearLayout.addView(separation);
    }

    /**
     * 初始化头像修改item
     *
     * @param inflater
     * @param stringID
     * @param url
     */
    private void initPortraitItem(LayoutInflater inflater, final int stringID, String url) {
        View view = inflater.inflate(R.layout.personal_information_portrait_tabel, null);
        TextView viewText = (TextView) view.findViewById(R.id.table_text);
        viewText.setText(stringID);
        ImageView personalPortraitImg = (ImageView) view.findViewById(R.id.portrait);
        ImageManager.displayPortrait(url, personalPortraitImg);
        /*绑定点击事件*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stringID) {
                    case R.string.personal:
                        /*跳转到个人资料页面*/
                        break;
                    case R.string.my_label:
                        /*跳转到我的标签*/
                        break;
                    case R.string.my_photo:
                       /*跳转到我的相册*/
                        break;
                    case R.string.my_activity:
                        /*跳转到我的动态*/
                        break;
                    case R.string.select_setting:
                         /*跳转到筛选设置*/
                        break;
                    case R.string.account_setting:
                       /*跳转到账户设置*/
                        break;
                    default:
                        break;
                }
            }
        });
        linearLayout.addView(view);
    }



    /**
     * 显示生日选择对话框
     */
    private void showDatePickDialog(String date) {
        FragmentManager fm = getActivity().getFragmentManager();
        DatePickFragment dialog = DatePickFragment.newInstance(DateTimeUtil.parseByyyyyMMdd10(date));
        dialog.setDialogTitle("请选择生日");
        dialog.setTargetFragment(this, DATE_REQUEST_CODE);
        dialog.show(fm, "date");
    }


    /**
     * 更新生日
     *
     * @param date
     * @return
     */
    private String updateBirthDate(Date date) {
        StringBuffer dateText = new StringBuffer();
        StringBuffer birthday = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dateText.append(calendar.get(Calendar.YEAR));
        birthday.append(calendar.get(Calendar.YEAR));
        dateText.append("-");
        dateText.append(calendar.get(Calendar.MONTH) + 1);
        if (Calendar.MONTH + 1 < 10) {
            birthday.append("0" + (calendar.get(Calendar.MONTH) + 1));
        } else {
            birthday.append((calendar.get(Calendar.MONTH) + 1));
        }
        dateText.append("-");
        dateText.append(calendar.get(Calendar.DAY_OF_MONTH));
        birthday.append(calendar.get(Calendar.DAY_OF_MONTH));
        return dateText.toString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case DATE_REQUEST_CODE:
                    Date date = (Date) data.getSerializableExtra(DatePickFragment.EXTRA_DATE);
                    String birthday = updateBirthDate(date);
                    birthdayView.setText(birthday);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
