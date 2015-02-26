package com.qicheng.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.util.Logger;

public class AddTripActivity extends BaseActivity {

    private static final Logger logger = new Logger("com.qicheng.business.ui.AddTripActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        StationSelectFragment fragment = new StationSelectFragment();
        getFragmentManager().beginTransaction().add(R.id.trip_add_fragment,fragment).commit();
    }


}
