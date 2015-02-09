package com.qicheng.business.ui;

import android.os.Bundle;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private RegisterFragment mRegisterFragment;
    private UserInfoInputFragment mUserInfoInputFragment;

    public RegisterFragment getRegisterFragment() {
        return mRegisterFragment;
    }

    public UserInfoInputFragment getUserInfoInputFragment() {
        return mUserInfoInputFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar();
        setContentView(R.layout.activity_register);
        mRegisterFragment = new RegisterFragment();
        UserInfoInputFragment userInfoSettingFragment = new UserInfoInputFragment();
//        getFragmentManager().beginTransaction().add(R.id.form_register, mRegisterFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.form_register, userInfoSettingFragment).commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_register, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
