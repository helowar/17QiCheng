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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserPhotoEventArgs;
import com.qicheng.business.module.Photo;
import com.qicheng.business.module.UserDetail;
import com.qicheng.business.ui.component.HorizontalScrollListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.util.Const.Intent.USER_DETAIL_KEY;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.SEX_MAN;
import static com.qicheng.util.Const.STATE_PAUSE_ON_FLING;
import static com.qicheng.util.Const.STATE_PAUSE_ON_SCROLL;

/**
 * UserInfoActivity.java是启程APP的用户详细信息Activity类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月13日
 */
public class UserInfoActivity extends BaseActivity {

    /**
     * 头像ImageView
     */
    private ImageView portraitImageView = null;

    /**
     * 昵称TextView
     */
    private TextView nicknameTextView = null;

    /**
     * 性别ImageView
     */
    private ImageView genderImageView = null;

    /**
     * 年龄TextView
     */
    private TextView ageTextView = null;

    /**
     * 发起聊天Button
     */
    private Button senderBtn = null;

    /**
     * 标签ViewGroup
     */
    private ViewGroup tagViewGroup = null;

    /**
     * 相册ListView
     */
    private HorizontalScrollListView photoListView = null;

    /**
     * 相册Layout
     */
    private LinearLayout photoLayout = null;

    /**
     * 当前用户所拥有的动态数量Layout
     */
    private RelativeLayout activityNumLayout = null;

    /**
     * 当前用户所拥有的动态数量TextView
     */
    private TextView activityNumTextView = null;

    /**
     * 所在地TextView
     */
    private TextView residenceTextView = null;

    /**
     * 家乡TextView
     */
    private TextView hometownTextView = null;

    /**
     * 学历TextView
     */
    private TextView educationTextView = null;

    /**
     * 从事行业TextView
     */
    private TextView industryTextView = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    /**
     * 相册列表
     */
    private List<Photo> photoList = new ArrayList<Photo>();

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户业务逻辑处理对象
     */
    private UserLogic userLogic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        // 获取各种View对象
        portraitImageView = (ImageView) findViewById(R.id.user_info_portrait_image_view);
        nicknameTextView = (TextView) findViewById(R.id.user_info_nickname_text_view);
        genderImageView = (ImageView) findViewById(R.id.user_info_gender_image_view);
        ageTextView = (TextView) findViewById(R.id.user_info_age_text_view);
        senderBtn = (Button) findViewById(R.id.user_info_sender_btn);
        tagViewGroup = (ViewGroup) findViewById(R.id.user_info_tag_view_group);
        photoListView = (HorizontalScrollListView) findViewById(R.id.user_info_photo_list_view);
        photoLayout = (LinearLayout) findViewById(R.id.user_info_photo_layout);
        activityNumLayout = (RelativeLayout) findViewById(R.id.user_info_activity_layout);
        activityNumTextView = (TextView) findViewById(R.id.user_info_activity_text_view);
        residenceTextView = (TextView) findViewById(R.id.user_info_residence_text_view);
        hometownTextView = (TextView) findViewById(R.id.user_info_hometown_text_view);
        educationTextView = (TextView) findViewById(R.id.user_info_education_text_view);
        industryTextView = (TextView) findViewById(R.id.user_info_industry_text_view);
        // 获取用户详细信息对象
        UserDetail userDetail = (UserDetail) getIntent().getExtras().getSerializable(USER_DETAIL_KEY);
        userId = userDetail.getUserId();
        ImageManager.displayPortrait(userDetail.getPortraitUrl(), portraitImageView);
        nicknameTextView.setText(userDetail.getNickname());
        if (userDetail.getGender() == SEX_MAN) {
            genderImageView.setImageResource(R.drawable.ic_male);
        } else {
            genderImageView.setImageResource(R.drawable.ic_female);
        }
        List<String> tags = userDetail.getTags();
        if (tags != null && tags.size() > 0) {
            for (String tagName : tags) {
                tagViewGroup.addView(createTagView(tagName));
            }
        }
        // 设置相册滚动停止监听器
        photoListView.setOnScrollStopListener(new PhotoOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
        List<Photo> photos = userDetail.getPhotoList();
        if (photos != null && photos.size() > 0) {
            photoList.addAll(photos);
            for (Photo photo : photos) {
                photoLayout.addView(createPhotoView(photo));
            }
        }
        activityNumTextView.setText("(" + userDetail.getActivityNum() + ")");
        residenceTextView.setText(userDetail.getResidence());
        hometownTextView.setText(userDetail.getHometown());
        educationTextView.setText(userDetail.getEducation());
        industryTextView.setText(userDetail.getIndustry());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
        pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class PhotoOnScrollListener extends PauseOnScrollListener implements HorizontalScrollListView.OnScrollStopListener {

        /**
         * 是否第一次拖至最右边标识
         */
        private boolean isFirstScrollToRightEdge = false;

        public PhotoOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super(imageLoader, pauseOnScroll, pauseOnFling);
        }

        @Override
        public void onScrollStoped() {
        }

        @Override
        public void onScrollToLeftEdge() {
        }

        @Override
        public void onScrollToRightEdge() {
            if (!isFirstScrollToRightEdge) {
                isFirstScrollToRightEdge = true;
            } else {
                // 查询之前的照片
                Photo lastPhoto = photoList.get(photoList.size() - 1);
                userLogic.getUserPhotoList(userId, ORDER_BY_EARLIEST, lastPhoto.getOrderNum(), 5, createUIEventListener(new EventListener() {
                    @Override
                    public void onEvent(EventId id, EventArgs args) {
                        stopLoading();
                        UserPhotoEventArgs result = (UserPhotoEventArgs) args;
                        OperErrorCode errCode = result.getErrCode();
                        if (errCode == OperErrorCode.Success) {
                            List<Photo> photos = result.getPhotoList();
                            if (photos != null && photos.size() > 0) {
                                photoList.addAll(photos);
                                for (int i = 0, size = photos.size(); i < size; i++) {
                                    photoLayout.addView(createPhotoView(photos.get(i)));
                                }
                            }
                        }
                    }
                }));
                startLoading();
            }
        }

        @Override
        public void onScrollToMiddle() {
            isFirstScrollToRightEdge = false;
        }
    }

    /**
     * 创建一个用户标签View对象。
     *
     * @param tagName 标签名称
     * @return 用户标签View对象
     */
    private View createTagView(String tagName) {
        // 创建用户标签View
        View userTagView = getLayoutInflater().inflate(R.layout.user_info_tag, null);
        TextView tagTextView = (TextView) userTagView.findViewById(R.id.user_info_tag_text_view);
        tagTextView.setText(tagName);
        return userTagView;
    }

    /**
     * 创建一个用户照片View对象。
     *
     * @param photo 用户照片
     * @return 用户照片View对象
     */
    private View createPhotoView(Photo photo) {
        // 创建用户照片View
        View userPhotoView = getLayoutInflater().inflate(R.layout.user_info_photo, null);
        ImageView photoView = (ImageView) userPhotoView.findViewById(R.id.user_info_photo_image_view);
        ImageManager.displayPortrait(photo.getThumbnailUrl(), photoView);
        return userPhotoView;
    }
}
