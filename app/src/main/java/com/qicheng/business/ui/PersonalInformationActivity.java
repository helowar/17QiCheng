package com.qicheng.business.ui;

import android.os.Bundle;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;

public class PersonalInformationActivity extends BaseActivity {
 private UserInformationModifyFragment userInformationModifyFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar();
        setContentView(R.layout.activity_personal_information);
        userInformationModifyFragment = new UserInformationModifyFragment();
        getFragmentManager().beginTransaction().add(R.id.userInformation, userInformationModifyFragment).commit();
    }


}
