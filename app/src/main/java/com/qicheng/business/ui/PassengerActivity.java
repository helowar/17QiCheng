/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TravellerPersonLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.QueryValue;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.component.HorizontalScrollListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.util.Const.Intent.PORTRAIT_URL;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_VALUE;
import static com.qicheng.util.Const.Intent.UID;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.ORDER_BY_NEWEST;
import static com.qicheng.util.Const.QUERY_TYPE_NOT_ON_CAR;
import static com.qicheng.util.Const.QUERY_TYPE_OFF_CAR;
import static com.qicheng.util.Const.QUERY_TYPE_ON_CAR;
import static com.qicheng.util.Const.STATE_PAUSE_ON_FLING;
import static com.qicheng.util.Const.STATE_PAUSE_ON_SCROLL;

/**
 * TravellerActivity.java是启程APP的展现同车乘客Activity类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月1日
 */
public class PassengerActivity extends BaseActivity {

    /**
     * 未上车车友Fragment标签
     */
    private static final String NOT_ON_TRAVELLER_FRAGMENT_TAG = "not_on_traveller_fragment_tag";

    /**
     * 上车车友Fragment标签
     */
    private static final String ON_TRAVELLER_FRAGMENT_TAG = "on_traveller_fragment_tag";

    /**
     * 下车车友Fragment标签
     */
    private static final String OFF_TRAVELLER_FRAGMENT_TAG = "off_traveller_fragment_tag";

    /**
     * 推荐车友View
     */
    private HorizontalScrollListView recommendPersonsView = null;

    /**
     * 推荐车友Layout
     */
    private LinearLayout recommendPersonsLayout = null;

    /**
     * 推荐车友列表
     */
    private List<User> recommendPersonList = new ArrayList<User>();

    /**
     * 未上车车友按钮
     */
    private Button notOnBtn = null;

    /**
     * 上车车友按钮
     */
    private Button onBtn = null;

    /**
     * 下车车友按钮
     */
    private Button offBtn = null;

    /**
     * 未上车车友FrameLayout
     */
    private FrameLayout notOnFrameLayout = null;

    /**
     * 上车车友FrameLayout
     */
    private FrameLayout onFrameLayout = null;

    /**
     * 下车车友FrameLayout
     */
    private FrameLayout offFrameLayout = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    /**
     * 未上车车友Fragment
     */
    private TravellerPersonFragment notOnTravellerFragment = null;

    /**
     * 上车车友Fragment
     */
    private TravellerPersonFragment onTravellerFragment = null;

    /**
     * 下车车友Fragment
     */
    private TravellerPersonFragment offTravellerFragment = null;

    /**
     * Fragment事物管理对象
     */
    private FragmentTransaction fragmentTransaction = null;

    /**
     * 查询用户信息业务逻辑处理对象
     */
    private TravellerPersonLogic logic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        // 获取上一个Activity传递过来的查询值
        Bundle extras = getIntent().getExtras();
        String queryValue = extras.getString(Const.Intent.TRAVELLER_QUERY_VALUE);
        setTitle(queryValue + " " + getTitle());
        // 获取各种View对象
        recommendPersonsView = (HorizontalScrollListView) findViewById(R.id.passenger_recommend_persons_view);
        recommendPersonsLayout = (LinearLayout) findViewById(R.id.passenger_recommend_persons_layout);
        notOnBtn = (Button) findViewById(R.id.passenger_not_on_btn);
        onBtn = (Button) findViewById(R.id.passenger_on_btn);
        offBtn = (Button) findViewById(R.id.passenger_off_btn);
        notOnFrameLayout = (FrameLayout) findViewById(R.id.passenger_not_on_Layout);
        onFrameLayout = (FrameLayout) findViewById(R.id.passenger_on_Layout);
        offFrameLayout = (FrameLayout) findViewById(R.id.passenger_off_Layout);
        // 设置推荐车友滚动停止监听器
        recommendPersonsView.setOnScrollStopListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
        // 设置未上车车友、上车车友和下车车友区域里的各种View对象
        notOnTravellerFragment = new TravellerPersonFragment();
        Bundle notOn = new Bundle();
        notOn.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_NOT_ON_CAR);
        notOn.putString(TRAVELLER_QUERY_VALUE, queryValue);
        notOnTravellerFragment.setArguments(notOn);
        onTravellerFragment = new TravellerPersonFragment();
        Bundle on = new Bundle();
        on.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_ON_CAR);
        on.putString(TRAVELLER_QUERY_VALUE, queryValue);
        onTravellerFragment.setArguments(on);
        offTravellerFragment = new TravellerPersonFragment();
        Bundle off = new Bundle();
        off.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_OFF_CAR);
        off.putString(TRAVELLER_QUERY_VALUE, queryValue);
        offTravellerFragment.setArguments(off);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.passenger_not_on_Layout, notOnTravellerFragment, NOT_ON_TRAVELLER_FRAGMENT_TAG);
        fragmentTransaction.add(R.id.passenger_on_Layout, onTravellerFragment, ON_TRAVELLER_FRAGMENT_TAG);
        fragmentTransaction.add(R.id.passenger_off_Layout, offTravellerFragment, OFF_TRAVELLER_FRAGMENT_TAG);
        fragmentTransaction.commit();
        notOnFrameLayout.setVisibility(View.VISIBLE);
        onFrameLayout.setVisibility(View.GONE);
        offFrameLayout.setVisibility(View.GONE);
        // 设置未上车车友、上车车友和下车车友按钮监听事件
        notOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notOnBtn.setBackgroundColor(getResources().getColor(R.color.main));
                notOnBtn.setTextColor(getResources().getColor(R.color.white));
                onBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                onBtn.setTextColor(getResources().getColor(R.color.main));
                offBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                offBtn.setTextColor(getResources().getColor(R.color.main));
                notOnFrameLayout.setVisibility(View.VISIBLE);
                onFrameLayout.setVisibility(View.GONE);
                offFrameLayout.setVisibility(View.GONE);
            }
        });
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notOnBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                notOnBtn.setTextColor(getResources().getColor(R.color.main));
                onBtn.setBackgroundColor(getResources().getColor(R.color.main));
                onBtn.setTextColor(getResources().getColor(R.color.white));
                offBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                offBtn.setTextColor(getResources().getColor(R.color.main));
                notOnFrameLayout.setVisibility(View.GONE);
                onFrameLayout.setVisibility(View.VISIBLE);
                offFrameLayout.setVisibility(View.GONE);
            }
        });
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notOnBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                notOnBtn.setTextColor(getResources().getColor(R.color.main));
                onBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                onBtn.setTextColor(getResources().getColor(R.color.main));
                offBtn.setBackgroundColor(getResources().getColor(R.color.main));
                offBtn.setTextColor(getResources().getColor(R.color.white));
                notOnFrameLayout.setVisibility(View.GONE);
                onFrameLayout.setVisibility(View.GONE);
                offFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        logic = (TravellerPersonLogic) LogicFactory.self().get(LogicFactory.Type.TravellerPerson);
        // 查询最新推荐用户
        refreshRecommendPerson();
        startLoading();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_passenger, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        User user = Cache.getInstance().getUser();
        int genderQueryValue = user.getQueryValue().getGender();
        if (genderQueryValue == Const.SEX_MAN) {
            menu.findItem(R.id.male).setChecked(true);
        } else if (genderQueryValue == Const.SEX_FEMALE) {
            menu.findItem(R.id.female).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.male:
                item.setChecked(true);
                refreshPerson(Const.SEX_MAN);
                break;
            case R.id.female:
                item.setChecked(true);
                refreshPerson(Const.SEX_FEMALE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TravellerOnScrollListener extends PauseOnScrollListener implements HorizontalScrollListView.OnScrollStopListener {

        /**
         * 是否第一次拖至最左边标识
         */
        private boolean isFirstScrollToLeftEdge = false;

        /**
         * 是否第一次拖至最右边标识
         */
        private boolean isFirstScrollToRightEdge = false;

        public TravellerOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super(imageLoader, pauseOnScroll, pauseOnFling);
        }

        @Override
        public void onScrollStoped() {
        }

        @Override
        public void onScrollToLeftEdge() {
            if (!isFirstScrollToLeftEdge) {
                isFirstScrollToLeftEdge = true;
            } else {
                // 查询最新推荐用户
                User firstUser = recommendPersonList.get(0);
                logic.queryRecommendUser(ORDER_BY_NEWEST, firstUser.getLastLoginTime(), 4, createUIEventListener(new EventListener() {
                    @Override
                    public void onEvent(EventId id, EventArgs args) {
                        stopLoading();
                        UserEventArgs result = (UserEventArgs) args;
                        OperErrorCode errCode = result.getErrCode();
                        if (errCode == OperErrorCode.Success) {
                            List<User> userList = result.getUserList();
                            if (userList != null && userList.size() > 0) {
                                recommendPersonList.addAll(0, userList);
                                for (int i = userList.size() - 1; i > -1; i--) {
                                    recommendPersonsLayout.addView(createRecommendPersonView(userList.get(i)), 0);
                                }
                            }
                        }
                    }
                }));
                startLoading();
            }
        }

        @Override
        public void onScrollToRightEdge() {
            if (!isFirstScrollToRightEdge) {
                isFirstScrollToRightEdge = true;
            } else {
                // 查询之前推荐用户
                User lastUser = recommendPersonList.get(recommendPersonList.size() - 1);
                logic.queryRecommendUser(ORDER_BY_EARLIEST, lastUser.getLastLoginTime(), 4, createUIEventListener(new EventListener() {
                    @Override
                    public void onEvent(EventId id, EventArgs args) {
                        stopLoading();
                        UserEventArgs result = (UserEventArgs) args;
                        OperErrorCode errCode = result.getErrCode();
                        if (errCode == OperErrorCode.Success) {
                            List<User> userList = result.getUserList();
                            if (userList != null && userList.size() > 0) {
                                recommendPersonList.addAll(userList);
                                for (int i = 0, size = userList.size(); i < size; i++) {
                                    recommendPersonsLayout.addView(createRecommendPersonView(userList.get(i)));
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
            isFirstScrollToLeftEdge = false;
            isFirstScrollToRightEdge = false;
        }
    }

    /**
     * 刷新整个页面里的推荐用户。
     */
    private void refreshRecommendPerson() {
        logic.queryRecommendUser(ORDER_BY_NEWEST, null, 8, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserEventArgs result = (UserEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    List<User> userList = result.getUserList();
                    if (userList != null && userList.size() > 0) {
                        recommendPersonList.clear();
                        recommendPersonList.addAll(userList);
                        recommendPersonsLayout.removeAllViews();
                        for (int i = 0, size = userList.size(); i < size; i++) {
                            recommendPersonsLayout.addView(createRecommendPersonView(userList.get(i)));
                        }
                    }
                }
            }
        }));
    }

    /**
     * 刷新整个页面里的用户。
     *
     * @param gender 性别
     */
    private void refreshPerson(int gender) {
        QueryValue queryValue = Cache.getInstance().getUser().getQueryValue();
        if (gender != queryValue.getGender()) {
            queryValue.setGender(gender);
            Cache.getInstance().refreshCacheUser();
            refreshRecommendPerson();
            FragmentManager manager = getFragmentManager();
            TravellerPersonFragment fragment = null;
            fragment = (TravellerPersonFragment) manager.findFragmentByTag(NOT_ON_TRAVELLER_FRAGMENT_TAG);
            fragment.refreshPerson();
            fragment = (TravellerPersonFragment) manager.findFragmentByTag(ON_TRAVELLER_FRAGMENT_TAG);
            fragment.refreshPerson();
            fragment = (TravellerPersonFragment) manager.findFragmentByTag(OFF_TRAVELLER_FRAGMENT_TAG);
            fragment.refreshPerson();
            startLoading();
        }
    }

    private View createRecommendPersonView(final User traveller) {
        // 创建推荐车友View
        View recommendPersonView;
        ImageView imageView = null;
        recommendPersonView = getActivity().getLayoutInflater().inflate(R.layout.traveller_recommend_person, recommendPersonsLayout, false);
        imageView = (ImageView) recommendPersonView.findViewById(R.id.traveller_recommend_person_img);
        String portraitUrl = traveller.getPortraitURL();
        ImageManager.displayPortrait(portraitUrl, imageView);
        recommendPersonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserInfoActivity(traveller);
            }
        });
        return recommendPersonView;
    }

    private void startUserInfoActivity(User traveller) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(UID, traveller.getUserId());
        intent.putExtra(PORTRAIT_URL, traveller.getPortraitURL());
        startActivity(intent);
    }
}
