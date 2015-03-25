/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserPhotoEventArgs;
import com.qicheng.business.module.Photo;
import com.qicheng.business.ui.component.PicViewPagerAdapter;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.helper.Alert;

import java.util.ArrayList;
import java.util.List;

public class AlbumItemActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private PicViewPagerAdapter mAdapter;
    private List<Photo> photos;

    private List<Photo> newPhotos;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int index = getIntent().getExtras().getInt("index");
        photos = (ArrayList<Photo>) getIntent().getExtras().get("photos");
        setContentView(R.layout.activity_album_item);
        mViewPager = (ViewPager) findViewById(R.id.id_pager);
        mAdapter = new PicViewPagerAdapter(getSupportFragmentManager(), photos);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(index);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int curr = mViewPager.getCurrentItem();
                    int lastReal = mViewPager.getAdapter().getCount() - 1;
                    if (curr == lastReal) {
                        progressBar = (ProgressBar) mViewPager.findViewById(R.id.progress_bar);
                        int index = photos.size();
                        byte orderNum = 1;
                        getPhotoList(null, orderNum, photos.get(index - 1).getOrderNum(), 100);
                        if (photos.size() > index) {
                            mViewPager.setCurrentItem(index, false);
                        }
                    }
                }
            }
        });
    }


    /**
     * 获取用户照片接口
     */
    public void getPhotoList(String userId, byte orderBy, Long orderNum, int size) {
        progressBar.setVisibility(View.VISIBLE);
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.getUserPhotoList(userId, orderBy, orderNum, size, new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserPhotoEventArgs result = (UserPhotoEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    newPhotos = result.getPhotoList();
                    if (newPhotos != null) {
                        photos.addAll(newPhotos);
                    } else {
                        Alert.Toast(getResources().getString(R.string.no_more_picture));
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
