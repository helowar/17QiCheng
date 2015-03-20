/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qicheng.R;

public class BenefitRequestActivity extends Activity {

    private BenefitOfAllFragment benefitOfAllFragment;

    private TextView allBenefit, carBenefit, hotelBenefit, touristBenefit;

    private static final int TAG_ALL = 0;
    private static final int TAG_CAR = 1;
    private static final int TAG_HOTEL = 2;
    private static final int TAG_TOURIST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefit_request);
        allBenefit = (TextView) findViewById(R.id.all_benefit_title);
        allBenefit.setTag(TAG_ALL);
        setTagOnclickListener(allBenefit);
        carBenefit = (TextView) findViewById(R.id.car_benefit_title);
        carBenefit.setTag(TAG_CAR);
        setTagOnclickListener(carBenefit);
        hotelBenefit = (TextView) findViewById(R.id.hotel_benefit_title);
        hotelBenefit.setTag(TAG_HOTEL);
        setTagOnclickListener(hotelBenefit);
        touristBenefit = (TextView) findViewById(R.id.tourist_benefit_title);
        touristBenefit.setTag(TAG_TOURIST);
        setTagOnclickListener(touristBenefit);
        //初始化时键入全部福利
        switchBenefitItem((Integer) allBenefit.getTag(), allBenefit);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_benefit_count, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
        switch (tag) {
            case TAG_ALL:
                benefitOfAllFragment = new BenefitOfAllFragment();
                getFragmentManager().beginTransaction().add(R.id.benefit_of_count, benefitOfAllFragment).commit();
                break;
            case TAG_CAR:
                benefitOfAllFragment = new BenefitOfAllFragment();
                getFragmentManager().beginTransaction().add(R.id.benefit_of_count, benefitOfAllFragment).commit();
                break;
            case TAG_HOTEL:
                benefitOfAllFragment = new BenefitOfAllFragment();
                getFragmentManager().beginTransaction().add(R.id.benefit_of_count, benefitOfAllFragment).commit();
                break;
            case TAG_TOURIST:
                benefitOfAllFragment = new BenefitOfAllFragment();
                getFragmentManager().beginTransaction().add(R.id.benefit_of_count, benefitOfAllFragment).commit();
                break;
            default:
        }
    }

    /**
     * 清空背景色
     */
    private void clearBackground() {
        Resources resources = getResources();
        reverseToOriginal(allBenefit, resources);
        reverseToOriginal(carBenefit, resources);
        reverseToOriginal(hotelBenefit, resources);
        reverseToOriginal(touristBenefit, resources);
    }

    /**
     * 恢复tag的原始状态
     */
    private void reverseToOriginal(TextView view, Resources resources) {
        view.setBackgroundColor(resources.getColor(R.color.white));
        view.setTextColor(resources.getColor(R.color.main));
    }

}
