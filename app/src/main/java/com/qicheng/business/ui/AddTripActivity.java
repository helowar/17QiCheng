package com.qicheng.business.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;

public class AddTripActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        getFragmentManager().beginTransaction().add(R.id.trip_add_fragment,new StationSelectFragment()).commit();

    }
}
