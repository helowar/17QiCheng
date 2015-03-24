/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.module.Benefit;
import com.qicheng.util.Const;

public class BenefitDetailActivity extends Activity {
    private Benefit benefit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefit_detail);
        Bundle bundle = getIntent().getExtras();
        benefit = (Benefit) bundle.get(Const.Intent.BENEFIT_DETAIL);
        ImageView benefitIcon = (ImageView) findViewById(R.id.benefit_icon);
        ImageManager.displayImageDefault(benefit.getLogoUrl(), benefitIcon);
        TextView title = (TextView) findViewById(R.id.benefit_title);
        title.setText(benefit.getName());
        TextView expireTime = (TextView) findViewById(R.id.benefit_deadline);
        expireTime.setText(benefit.getExpireTime());
        TextView description = (TextView) findViewById(R.id.benefit_content);
        description.setText(benefit.getDescription());
        TextView value = (TextView) findViewById(R.id.benefit_value);
        value.setText(benefit.getValue() + "");
        ImageView shareImg = (ImageView) findViewById(R.id.share_img);
        if (benefit.getPostOpFlag() == 0) {
            shareImg.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_benefit_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
