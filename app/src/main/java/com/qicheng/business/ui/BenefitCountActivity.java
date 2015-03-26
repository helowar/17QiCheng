/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.module.Benefit;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

import java.util.List;

public class BenefitCountActivity extends BaseActivity {
    private BenefitOfAllFragment benefitOfAllFragment;

    private TextView allBenefit, entityBenefit;
    /**
     * 虚拟福利的Tag
     */
    private static final int TAG_ALL = 0;
    /**
     * 实体福利的Tag
     */
    private static final int TAG_ENTITY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefit_count);
        allBenefit = (TextView) findViewById(R.id.all_benefit_title);
        allBenefit.setTag(TAG_ALL);
        setTagOnclickListener(allBenefit);
        entityBenefit = (TextView) findViewById(R.id.car_entity_title);
        entityBenefit.setTag(TAG_ENTITY);
        setTagOnclickListener(entityBenefit);
        //初始化时键入全部福利
        switchBenefitItem((Integer) allBenefit.getTag(), allBenefit);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_benefit_count, menu);
        ActionBar bar = this.getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 为不同的Tag定义点击事件
     */
    private void setTagOnclickListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchBenefitItem((Integer) v.getTag(), v);
            }
        });
    }


    /**
     * 切换到不同的fragment中
     *
     * @param tag
     * @param view
     */
    public void switchBenefitItem(int tag, View view) {
        clearBackground();
        view.setBackgroundColor(getResources().getColor(R.color.main));
        ((TextView) view).setTextColor(getResources().getColor(R.color.white));
        Bundle bundle = new Bundle();
        switch (tag) {
            case TAG_ALL:
                bundle.putInt(Const.Intent.TYPE_THING, 0);
                bundle.putString(Const.Intent.BENEFIT_TYPE_ID, null);
                break;
            case TAG_ENTITY:
                bundle.putInt(Const.Intent.TYPE_THING, 1);
                bundle.putString(Const.Intent.BENEFIT_TYPE_ID, null);
                break;
            default:
        }
        if (benefitOfAllFragment == null) {
            benefitOfAllFragment = new BenefitOfAllFragment();
            benefitOfAllFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.benefit_of_count, benefitOfAllFragment).commit();
        } else {
            benefitOfAllFragment = new BenefitOfAllFragment();
            benefitOfAllFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.benefit_of_count, benefitOfAllFragment).commit();
        }
    }

    /**
     * 清空背景色
     */
    private void clearBackground() {
        Resources resources = getResources();
        reverseToOriginal(allBenefit, resources);
        reverseToOriginal(entityBenefit, resources);
    }

    /**
     * 恢复tag的原始状态
     */
    private void reverseToOriginal(TextView view, Resources resources) {
        view.setBackgroundColor(resources.getColor(R.color.white));
        view.setTextColor(resources.getColor(R.color.main));
    }


}
