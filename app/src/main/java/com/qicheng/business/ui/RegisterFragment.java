package com.qicheng.business.ui;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment {

    private static Logger logger = new Logger("com.qicheng.business.ui.RegisterFragment");

    /**
     * 控件成员
     */
    private Button mVerifyCodeButton;
    private Button mSubmitButton;
    private EditText mMobileNumber;
    private EditText mVerifyCode;
    private EditText mUserPwd;
    private CountDownTimer timer;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.title_activity_register));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //跳转至登录
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
        ActionBar bar = getActivity().getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_register, container, false);
        //验证码按钮
        mVerifyCodeButton= (Button)fragmentView.findViewById(R.id.button_get_verify_code);
        //注册按钮
        mSubmitButton = (Button)fragmentView.findViewById(R.id.button_register);
        //获取手机号码输入控件
        mMobileNumber = (EditText)fragmentView.findViewById(R.id.edittext_mobile);
        //获取验证码输入控件
        mVerifyCode = (EditText)fragmentView.findViewById(R.id.editText_verify_code);
        //获取密码输入控件
        mUserPwd = (EditText)fragmentView.findViewById(R.id.edittext_pwd);

        mVerifyCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View button) {
                String cellNum = mMobileNumber.getText().toString();
                //判断手机号码是否合法
                if(StringUtil.isEmpty(cellNum)||!StringUtil.isMobileNum(cellNum)){
                       Alert.Toast(getResources().getString(R.string.illegal_cell_num_msg));
                }else {
                    /**
                     * 获取验证码
                     */
                    getVerifyCode();
                }
            }
        });

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
                if(!StringUtil.isEmpty(s.toString())&&!StringUtil.isEmpty(mVerifyCode.getText().toString())&&!StringUtil.isEmpty(mUserPwd.getText().toString())){
                    mSubmitButton.setEnabled(true);
                }else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });

        mVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!StringUtil.isEmpty(s.toString())&&!StringUtil.isEmpty(mMobileNumber.getText().toString())&&!StringUtil.isEmpty(mUserPwd.getText().toString())){
                    mSubmitButton.setEnabled(true);
                }else {
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
                if (!StringUtil.isEmpty(s.toString()) && !StringUtil.isEmpty(mMobileNumber.getText().toString()) && !StringUtil.isEmpty(mVerifyCode.getText().toString())) {
                    mSubmitButton.setEnabled(true);
                } else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String cellNum = mMobileNumber.getText().toString();
                if(StringUtil.isEmpty(cellNum)||!StringUtil.isMobileNum(cellNum)){
                    Alert.Toast(getResources().getString(R.string.illegal_cell_num_msg));
                    return;
                }
                if(StringUtil.isEmpty(mUserPwd.getText().toString())||StringUtil.isEmpty(mVerifyCode.getText().toString())){
                    Alert.Toast("请填写正确注册信息");
                }
                // TODO:调用注册逻辑
                doRegister();
            }
        });
        return fragmentView;
    }


    private void getVerifyCode(){
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        logger.d("Public key is:------*"+userLogic.getPublicKey()+"*--------");
        userLogic.getVerifyCode(mMobileNumber.getText().toString(), createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((StatusEventArgs)args).getErrCode();
                switch(errCode) {
                    case Success:
                        Alert.Toast(getResources().getString(R.string.verify_code_send_success_msg));
                        //重新获取的倒计时开启
                        mVerifyCodeButton.setEnabled(false);

                        timer = new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                mVerifyCodeButton.setText(millisUntilFinished / 1000+getResources().getString(R.string.verify_code_wait_msg));
                            }
                            public void onFinish() {
                                mVerifyCodeButton.setEnabled(true);
                                mVerifyCodeButton.setText(getResources().getString(R.string.get_verify_code_button));
                            }
                        }.start();
                        break;
                    default:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.verify_code_send_failed_msg));
                        break;
                }
            }
        }));

    }

    /**
     * 执行登录过程
     */
    private void doRegister(){
        UserLogic userLogic = (UserLogic)LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.userRegister(mMobileNumber.getText().toString(),mUserPwd.getText().toString(), mVerifyCode.getText().toString(),createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                OperErrorCode errCode = ((StatusEventArgs)args).getErrCode();
                switch(errCode) {
                    case Success:
                        startActivity(new Intent(getActivity(), MainActivity.class ));
                        getActivity().finish();
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
                        Alert.Toast("注册失败");
                        break;
                }
            }
        }));

        startLoading();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(timer!=null){
            timer.cancel();
        }
    }
}
