/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

public class UserInformationModifyActivity extends BaseActivity {
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
    /*
    文本编辑框
     */
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_modify);
        Bundle bundle = getIntent().getExtras();
        updateType = bundle.getInt("update_type");
        updateValue = bundle.getString("update_value");
        updateTitle = bundle.getString("update_title");
        setTitle(updateTitle);
        editText = (EditText) findViewById(R.id.update_data);
        editText.setHint(updateValue);
        editText.setText(updateValue);
        editText.setFocusable(true);
        editText.requestFocus();
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInformation();
            }
        });
    }


    /**
     * 更新用户基本信息
     */
    private void updateUserInformation() {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        updateValue = editText.getText().toString();
        userLogic.updateUserInformation(updateType, updateValue, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs userEventArgs = (UserEventArgs) args;
                OperErrorCode errCode = userEventArgs.getErrCode();
                switch (errCode) {
                    case Success:
                        Intent intent = new Intent();
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT, editText.getText().toString());
                        setResult(updateType, intent);
                        finish();
                        break;
                }
            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_information_modify, menu);
        ActionBar bar = this.getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().finish();
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
