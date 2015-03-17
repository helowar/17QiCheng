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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;

public class SelectEducationActivity extends BaseActivity {
    /*
更新的类型
 */
    private int updateType;
    /*
    更新的actionbar的title
     */
    private String updateTitle;
    /*
    要更新的具体数据
     */
    private String updateValue;
    private LinearLayout selectEducation;
    private List<Education> educations;
    private String[] eduNames = {"初中", "高中", "中专", "大专", "本科", "研究生", "博士", "博士后"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_education);
        //获取传过来的信息
        Bundle bundle = getIntent().getExtras();
        updateType = bundle.getInt(Const.Intent.UPDATE_USER_INFORMATION_TYPE);
        updateValue = bundle.getString(Const.Intent.UPDATE_USER_INFORMATION_VALUE);
        updateTitle = bundle.getString(Const.Intent.UPDATE_USER_INFORMATION_TITLE);
        setTitle(updateTitle);
        selectEducation = (LinearLayout) findViewById(R.id.select_education);
        initEducationList();
        for (Education edu : educations) {
            if (edu.getEduName().equals(updateValue)) {
                edu.setSelected(true);
            }
            initItem(edu, getLayoutInflater());
        }

    }

    /**
     * 初始化学历的Item
     *
     * @param education
     * @param inflater
     */
    private void initItem(final Education education, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.education_item, null);
        /*判定当前已选的*/
        if (education.isSelected) {
            view.findViewById(R.id.is_select).setBackgroundColor(getResources().getColor(R.color.main));
        }
        view.setTag(education);
        TextView eduText = (TextView) view.findViewById(R.id.edu_text);
        eduText.setText(education.getEduName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击是清空状态
                for (int i = 0, size = educations.size(); i < size; i++) {
                    Education edu = educations.get(i);
                    if (edu.isSelected) {
                        selectEducation.getChildAt(i).findViewById(R.id.is_select).setBackgroundColor(getResources().getColor(R.color.unavailable));
                    }
                }
                v.findViewById(R.id.is_select).setBackgroundColor(getResources().getColor(R.color.main));
                //更新学历
                updateUserInformation(education);
            }
        });
        selectEducation.addView(view);
    }


    /**
     * 更新用户基本信息
     */
    private void updateUserInformation(final Education edu) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.updateUserInformation(updateType, edu.getType().toString(), createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs userEventArgs = (UserEventArgs) args;
                OperErrorCode errCode = userEventArgs.getErrCode();
                switch (errCode) {
                    case Success:
                        Intent intent = new Intent();
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT, edu.getEduName());
                        setResult(Const.UserUpdateCode.UPDATE_EDUCATION, intent);
                        finish();
                        break;
                }
            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_education, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化学历的列表
     */
    private void initEducationList() {
        educations = new ArrayList<Education>();
        for (int i = 0, length = eduNames.length; i < length; i++) {
            Education education = new Education();
            education.setEduName(eduNames[i]);
            education.setType(i);
            educations.add(education);
        }
    }

    /*
    学历相关信息实体类
     */
    private class Education {
        private String eduName;
        private Integer type;
        private boolean isSelected;

        public String getEduName() {
            return eduName;
        }

        public void setEduName(String eduName) {
            this.eduName = eduName;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }
}
