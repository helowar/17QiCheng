/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.util.Const;

public class UserSettingFragment extends BaseFragment {
    /* 请求码 */

    private View view;
    private LinearLayout linearLayout;


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

        /*修改手机号码*/
        initViewItem(inflater, R.string.update_cell_num);
        /*修改密码*/
        initViewItem(inflater, R.string.update_password);

        /*添加分割段*/
        addSeparation(inflater);

         /*手机号码*/
        initViewItem(inflater, R.string.logout);


    }

    /**
     * 创建menu元素并加入到布局文件中
     *
     * @param inflater
     * @param stringID 字符串id
     */

    public void initViewItem(LayoutInflater inflater, final int stringID) {
        View view = inflater.inflate(R.layout.personal_information_text_tabel, null);
        TextView viewText = (TextView) view.findViewById(R.id.table_text);
        viewText.setText(stringID);
        final TextView textView = (TextView) view.findViewById(R.id.text);
        /*绑定点击事件*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (stringID) {
                    case R.string.update_cell_num:
                        intent = new Intent(getActivity(), UserCellnumupdateActivity.class);
                        startActivity(intent);
                        break;
                    case R.string.update_password:
                       /*跳转到更新密码*/
                        intent = new Intent(getActivity(), UserPasswordUpdateActivity.class);
                        intent.putExtra(Const.Intent.UPDATE_PASSWORD_RESOURCE, Const.UPDATE_PWD_FROM_MODIFY);
                        startActivity(intent);
                        break;
                    case R.string.logout:
                        /*跳转到我的动态*/
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


}
