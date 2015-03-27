package com.qicheng.business.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseFragmentActivity;

public class PersonalInformationActivity extends BaseFragmentActivity {
    private UserInformationFragment userInformationModifyFragment;
    private static final int UPDATE_COMPLETE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar();
        setContentView(R.layout.activity_personal_information);
        userInformationModifyFragment = new UserInformationFragment();
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
            setResult(UPDATE_COMPLETE);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(UPDATE_COMPLETE);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
