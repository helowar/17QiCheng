/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
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
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
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
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.util.Const.Intent.DYN_QUERY_NAME;
import static com.qicheng.util.Const.Intent.DYN_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.DYN_QUERY_VALUE;
import static com.qicheng.util.Const.Intent.FRIEND_SOURCE_KEY;
import static com.qicheng.util.Const.Intent.HX_USER_ID;
import static com.qicheng.util.Const.Intent.HX_USER_NICK_NAME;
import static com.qicheng.util.Const.Intent.HX_USER_TO_CHAT_AVATAR;
import static com.qicheng.util.Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY;
import static com.qicheng.util.Const.Intent.ORIGINAL_PICTURE_URL_KEY;
import static com.qicheng.util.Const.Intent.USER_DETAIL_KEY;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.QUERY_TYPE_USER;
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
     * 标签ViewGroup
     */
    private ViewGroup tagViewGroup = null;

    /**
     * 相册Layout
     */
    private LinearLayout photoLayout = null;

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
        ImageView portraitImageView = (ImageView) findViewById(R.id.user_info_portrait_image_view);
        TextView nicknameTextView = (TextView) findViewById(R.id.user_info_nickname_text_view);
        ImageView genderImageView = (ImageView) findViewById(R.id.user_info_gender_image_view);
        TextView ageTextView = (TextView) findViewById(R.id.user_info_age_text_view);
        Button senderBtn = (Button) findViewById(R.id.user_info_sender_btn);
        tagViewGroup = (ViewGroup) findViewById(R.id.user_info_tag_view_group);
        HorizontalScrollListView photoListView = (HorizontalScrollListView) findViewById(R.id.user_info_photo_list_view);
        photoLayout = (LinearLayout) findViewById(R.id.user_info_photo_layout);
        RelativeLayout activityNumLayout = (RelativeLayout) findViewById(R.id.user_info_activity_layout);
        TextView activityNumTextView = (TextView) findViewById(R.id.user_info_activity_text_view);
        TextView residenceTextView = (TextView) findViewById(R.id.user_info_residence_text_view);
        TextView hometownTextView = (TextView) findViewById(R.id.user_info_hometown_text_view);
        TextView educationTextView = (TextView) findViewById(R.id.user_info_education_text_view);
        TextView industryTextView = (TextView) findViewById(R.id.user_info_industry_text_view);
        Bundle extras = getIntent().getExtras();
        // 获取用户详细信息对象
        final UserDetail userDetail = (UserDetail) extras.getSerializable(USER_DETAIL_KEY);
        final String friendSource = extras.getString(FRIEND_SOURCE_KEY);
        final boolean isFromChatActivity = extras.getBoolean(IS_FROM_CHAT_ACTIVITY_KEY, false);
        userId = userDetail.getUserId();
        ImageManager.displayPortrait(userDetail.getPortraitUrl(), portraitImageView);
        nicknameTextView.setText(userDetail.getNickname());
        if (userDetail.getGender() == SEX_MAN) {
            genderImageView.setImageResource(R.drawable.ic_male);
        } else {
            genderImageView.setImageResource(R.drawable.ic_female);
        }
        setAge(ageTextView, userDetail.getBirthday());
        if (userDetail.getUserIMId().equals(Cache.getInstance().getUser().getUserImId())) {
            senderBtn.setVisibility(View.GONE);
        } else {
            senderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromChatActivity) {
                        finish();
                        return;
                    }
                    Intent i = new Intent(getActivity(), ChatActivity.class);
                    i.putExtra(HX_USER_ID, userDetail.getUserIMId());
                    i.putExtra(HX_USER_NICK_NAME, userDetail.getNickname());
                    i.putExtra(HX_USER_TO_CHAT_AVATAR, userDetail.getPortraitUrl());
                    i.putExtra(FRIEND_SOURCE_KEY, friendSource);
                    startActivity(i);
                }
            });
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
        activityNumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ToDynActivity.class);
                intent.putExtra(DYN_QUERY_TYPE, QUERY_TYPE_USER);
                intent.putExtra(DYN_QUERY_VALUE, userId);
                intent.putExtra(DYN_QUERY_NAME, userDetail.getNickname() + getResources().getString(R.string.someone_activity_text));
                startActivity(intent);
            }
        });
        activityNumTextView.setText("(" + userDetail.getActivityNum() + ")");
        residenceTextView.setText(userDetail.getResidence());
        hometownTextView.setText(userDetail.getHometown());
        educationTextView.setText(userDetail.getEducation());
        industryTextView.setText(userDetail.getIndustry());
        userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
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
        View userTagView = getLayoutInflater().inflate(R.layout.user_info_tag, tagViewGroup, false);
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
    private View createPhotoView(final Photo photo) {
        // 创建用户照片View
        View userPhotoView = getLayoutInflater().inflate(R.layout.user_info_photo, photoLayout, false);
        ImageView photoView = (ImageView) userPhotoView.findViewById(R.id.user_info_photo_image_view);
        ImageManager.displayPortrait(photo.getThumbnailUrl(), photoView);
        userPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OriginalPictureActivity.class);
                intent.putExtra(ORIGINAL_PICTURE_URL_KEY, photo.getPhotoUrl());
                startActivity(intent);
            }
        });
        return userPhotoView;
    }

    /**
     * 设置年龄TextView的文本值。
     *
     * @param ageTextView 年龄TextView对象
     * @param birthday    生日字符串（yyyy-MM-dd）
     */
    private void setAge(TextView ageTextView, String birthday) {
        if (StringUtil.isEmpty(birthday)) {
            ageTextView.setText(R.string.secret_text);
        } else {
            String age = DateTimeUtil.getAge(birthday);
            if (StringUtil.isEmpty(age)) {
                ageTextView.setText(R.string.secret_text);
            } else {
                ageTextView.setText(age + getResources().getString(R.string.age_text));
            }
        }
    }
}
