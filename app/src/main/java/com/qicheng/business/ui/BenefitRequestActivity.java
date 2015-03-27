/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseFragmentActivity;

public class BenefitRequestActivity extends BaseFragmentActivity {

    private BenefitOfRankFragment benefitOfRankFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefit_request);
        if (null == benefitOfRankFragment) {
            benefitOfRankFragment = new BenefitOfRankFragment();
            getFragmentManager().beginTransaction().add(R.id.benefit_of_rank, benefitOfRankFragment).commit();

        } else {
            benefitOfRankFragment = new BenefitOfRankFragment();
            getFragmentManager().beginTransaction().replace(R.id.benefit_of_rank, benefitOfRankFragment).commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_benefit_count, menu);
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
