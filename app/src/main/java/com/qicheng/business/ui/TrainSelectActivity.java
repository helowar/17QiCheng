package com.qicheng.business.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qicheng.R;
import com.qicheng.business.module.TrainStation;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.base.BaseFragmentActivity;

public class TrainSelectActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_select);
        getFragmentManager().beginTransaction().add(R.id.train_select_fragment,new TrainPickFragment(),"TrainPickFragment" ).commit();
    }
}
