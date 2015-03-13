package com.qicheng.business.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal_information, menu);
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
}
