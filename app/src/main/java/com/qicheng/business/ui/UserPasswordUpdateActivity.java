/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

public class UserPasswordUpdateActivity extends BaseActivity {
    private static Logger logger = new Logger("com.qicheng.business.ui.UserPasswordUpdateActivity");

    /**
     * 控件成员
     */
    private Button mVerifyCodeButton;
    private Button mSubmitButton;
    private EditText mMobileNumber;
    private EditText mVerifyCode;
    private EditText mUserPwd;
    private LinearLayout mCellNumContainer;
    private CountDownTimer timer;
    private String cellNum;
    private byte resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_update_acrivity);
        resource = getIntent().getExtras().getByte(Const.Intent.UPDATE_PASSWORD_RESOURCE);
        mCellNumContainer = (LinearLayout) findViewById(R.id.cell_num_container);
        if (resource == Const.UPDATE_PWD_FROM_MODIFY) {
            cellNum = Cache.getInstance().getUser().getUserId();
            mCellNumContainer.setVisibility(View.GONE);
        }
        mVerifyCodeButton = (Button) findViewById(R.id.button_get_verify_code);
        //注册按钮
        mSubmitButton = (Button) findViewById(R.id.button_register);
        //获取手机号码输入控件
        mMobileNumber = (EditText) findViewById(R.id.edittext_mobile);
        //获取验证码输入控件
        mVerifyCode = (EditText) findViewById(R.id.editText_verify_code);
        //获取密码输入控件
        mUserPwd = (EditText) findViewById(R.id.edittext_pwd);

        mVerifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {

                if (resource == Const.UPDATE_PWD_FROM_FORGET) {
                    cellNum = mMobileNumber.getText().toString();
                } else {
                    cellNum = Cache.getInstance().getUser().getUserId();
                }
                logger.d(cellNum);
                //判断手机号码是否合法
                if (StringUtil.isEmpty(cellNum) || !StringUtil.isMobileNum(cellNum)) {
                    Alert.Toast(getResources().getString(R.string.illegal_cell_num_msg));
                } else {
                    //重新获取的倒计时开启
                    mVerifyCodeButton.setEnabled(false);

                    timer = new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            mVerifyCodeButton.setText(millisUntilFinished / 1000 + getResources().getString(R.string.verify_code_wait_msg));
                        }

                        public void onFinish() {
                            mVerifyCodeButton.setEnabled(true);
                            mVerifyCodeButton.setText(getResources().getString(R.string.get_verify_code_button));
                        }
                    }.start();
                    /**
                     * 获取验证码
                     */
                    getVerifyCode(cellNum);
                }
            }
        });

        if (resource == Const.UPDATE_PWD_FROM_FORGET) {
            mMobileNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Do nothing
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!StringUtil.isEmpty(s.toString()) && !StringUtil.isEmpty(mVerifyCode.getText().toString()) && !StringUtil.isEmpty(mUserPwd.getText().toString())) {
                        mSubmitButton.setEnabled(true);
                    } else {
                        mSubmitButton.setEnabled(false);
                    }
                }
            });
        }

        mVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString()) && !StringUtil.isEmpty(cellNum) && !StringUtil.isEmpty(mUserPwd.getText().toString())) {
                    mSubmitButton.setEnabled(true);
                } else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });

        mUserPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString()) && !StringUtil.isEmpty(cellNum) && !StringUtil.isEmpty(mVerifyCode.getText().toString())) {
                    mSubmitButton.setEnabled(true);
                } else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resource == Const.UPDATE_PWD_FROM_FORGET) {
                    cellNum = mMobileNumber.getText().toString();
                    if (StringUtil.isEmpty(cellNum) || !StringUtil.isMobileNum(cellNum)) {
                        Alert.Toast(getResources().getString(R.string.illegal_cell_num_msg));
                        return;
                    }
                    if (StringUtil.isEmpty(mUserPwd.getText().toString()) || StringUtil.isEmpty(mVerifyCode.getText().toString())) {
                        Alert.Toast("请填写正确信息");
                    }
                    doModify(mMobileNumber.getText().toString());
                } else if (resource == Const.UPDATE_PWD_FROM_MODIFY) {
                    if (StringUtil.isEmpty(mUserPwd.getText().toString()) || StringUtil.isEmpty(mVerifyCode.getText().toString())) {
                        Alert.Toast("请填写正确信息");
                    }
                    doModify(Cache.getInstance().getUser().getUserId());
                }

            }
        });
    }

    /**
     * 获取验证码
     *
     * @param cellNum
     */
    private void getVerifyCode(String cellNum) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        logger.d("Public key is:------*" + userLogic.getPublicKey() + "*--------");
        userLogic.getVerifyCode(cellNum, Const.ACTION_TYPE_FIND, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((StatusEventArgs) args).getErrCode();
                switch (errCode) {
                    case Success:
                        Alert.Toast(getResources().getString(R.string.verify_code_send_success_msg));
                        break;
                    default:
                        Alert.Toast(getResources().getString(R.string.verify_code_send_failed_msg));
                        break;
                }
            }
        }));

    }

    /**
     * 修改密码
     *
     * @param cellNum
     */
    private void doModify(String cellNum) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.updatePassword(cellNum, mUserPwd.getText().toString(), mVerifyCode.getText().toString(), createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                OperErrorCode errCode = ((StatusEventArgs) args).getErrCode();
                switch (errCode) {
                    case Success:
                        finish();
                        break;
                    case CellNumExist:
                        Alert.Toast(getResources().getString(R.string.cell_num_already_exist_err_msg));
                        break;
                    case VerifyCodeExpire:
                        Alert.Toast(getResources().getString(R.string.verify_code_expire_err_msg));
                        break;
                    case VerifyCodeWrong:
                        Alert.Toast(getResources().getString(R.string.wrong_verify_code_err_msg));
                        break;
                    case NetNotAviable:
                        Alert.showNetAvaiable();
                        break;
                    default:
                        Alert.Toast("修改失败");
                        break;
                }
            }
        }));

        startLoading();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_setting_update_acrivity, menu);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
