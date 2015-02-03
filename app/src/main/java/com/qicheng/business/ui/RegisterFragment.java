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
import com.qicheng.framework.util.StringUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    /**
     * 控件成员
     */
    private Button mVerifyCodeButton;
    private Button mSubmitButton;
    private EditText mMobileNumber;
    private EditText mVerifyCode;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("注册");
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
        //验证码按钮事件
        mVerifyCodeButton= (Button)fragmentView.findViewById(R.id.button_get_verify_code);
        mVerifyCodeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View button) {
                mVerifyCodeButton.setEnabled(false);
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        mVerifyCodeButton.setText(millisUntilFinished / 1000+getResources().getString(R.string.verify_code_wait_msg));
                    }
                    public void onFinish() {
                        mVerifyCodeButton.setEnabled(true);
                        mVerifyCodeButton.setText(getResources().getString(R.string.get_verify_code_button));
                    }
                }.start();
                // TODO: 调用验证码获取逻辑
            }
        });
        //获取手机号码输入控件
        mMobileNumber = (EditText)fragmentView.findViewById(R.id.edittext_mobile);
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
                if(!(StringUtil.isEmpty(s.toString())&&StringUtil.isEmpty(mVerifyCode.getText().toString()))){
                    mSubmitButton.setEnabled(true);
                }else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });
        //获取验证码输入控件
        mVerifyCode = (EditText)fragmentView.findViewById(R.id.editText_verify_code);
        mVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!(StringUtil.isEmpty(s.toString())&&StringUtil.isEmpty(mMobileNumber.getText().toString()))){
                    mSubmitButton.setEnabled(true);
                }else {
                    mSubmitButton.setEnabled(false);
                }
            }
        });
        //注册按钮事件
        mSubmitButton = (Button)fragmentView.findViewById(R.id.button_register);
        return fragmentView;
    }



}
