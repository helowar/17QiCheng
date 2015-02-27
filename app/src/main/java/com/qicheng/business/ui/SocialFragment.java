/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qicheng.business.module.Location;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.component.HorizontalScrollListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.util.Const.Intent.PORTRAIT_URL;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_VALUE;
import static com.qicheng.util.Const.Intent.UID;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.ORDER_BY_NEWEST;
import static com.qicheng.util.Const.STATE_PAUSE_ON_FLING;
import static com.qicheng.util.Const.STATE_PAUSE_ON_SCROLL;
import static com.qicheng.util.Const.USER_QUERY_TYPE_NEAR;

/**
 * SocialFragment.java是启程APP的交友Fragment类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月1日
 */
public class SocialFragment extends BaseFragment {

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
     * 附近车友FrameLayout
     */
    private FrameLayout nearPersonFrameLayout = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    /**
     * 附近车友Fragment
     */
    private TravellerPersonFragment nearPersonTravellerFragment = null;

    /**
     * Fragment事物管理对象
     */
    private FragmentTransaction fragmentTransaction = null;

    /**
     * 查询值
     * 当query_type=0、1或2时，该值为车站代码；
     * 当query_type=3、4、5或6时，该值为车次；
     * 当query_type=7时，该值为经纬度，其格式为：经度 + | + 纬度。
     */
    private String queryValue;

    /**
     * 查询用户信息业务逻辑处理对象
     */
    private TravellerPersonLogic logic = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logic = (TravellerPersonLogic) LogicFactory.self().get(LogicFactory.Type.TravellerPerson);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View socialView = inflater.inflate(R.layout.fragment_social, container, false);
        // 获取各种View对象
        recommendPersonsView = (HorizontalScrollListView) socialView.findViewById(R.id.social_recommend_persons_view);
        recommendPersonsLayout = (LinearLayout) socialView.findViewById(R.id.social_recommend_persons_layout);
        nearPersonFrameLayout = (FrameLayout) socialView.findViewById(R.id.social_near_person_Layout);
        // 设置推荐车友滚动停止监听器
        recommendPersonsView.setOnScrollStopListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
        // 设置附近车友区域里的各种View对象
        nearPersonTravellerFragment = new TravellerPersonFragment();
        Bundle nearPerson = new Bundle();
        nearPerson.putByte(TRAVELLER_QUERY_TYPE, USER_QUERY_TYPE_NEAR);
        Location location = Cache.getInstance().getUser().getLocation();
        queryValue = location.getLongitude() + '|' + location.getLatitude();
        nearPerson.putString(TRAVELLER_QUERY_VALUE, queryValue);
        nearPersonTravellerFragment.setArguments(nearPerson);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.social_near_person_Layout, nearPersonTravellerFragment);
        fragmentTransaction.commit();
        nearPersonFrameLayout.setVisibility(View.VISIBLE);
        logic = (TravellerPersonLogic) LogicFactory.self().get(LogicFactory.Type.TravellerPerson);
        // 查询最新推荐用户
        logic.queryRecommendUser(ORDER_BY_NEWEST, null, 8, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserEventArgs result = (UserEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    List<User> userList = result.getUserList();
                    if (userList != null && userList.size() > 0) {
                        User traveller = null;
                        for (int i = 0, size = userList.size(); i < size; i++) {
                            traveller = userList.get(i);
                            recommendPersonList.add(traveller);
                            recommendPersonsLayout.addView(createRecommendPersonView(traveller));
                        }

                    }
                }
            }
        }));
        startLoading();
        return socialView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_PAUSE_ON_SCROLL)) {
                pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
            }
            if (savedInstanceState.containsKey(STATE_PAUSE_ON_FLING)) {
                pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
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
                                User traveller = null;
                                for (int i = 0, size = userList.size(); i < size; i++) {
                                    traveller = userList.get(i);
                                    recommendPersonList.add(0, traveller);
                                    recommendPersonsLayout.addView(createRecommendPersonView(traveller), 0);
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
                                User traveller = null;
                                for (int i = 0, size = userList.size(); i < size; i++) {
                                    traveller = userList.get(i);
                                    recommendPersonList.add(traveller);
                                    recommendPersonsLayout.addView(createRecommendPersonView(traveller));
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

    private View createRecommendPersonView(final User traveller) {
        // 创建推荐车友View
        View recommendPersonView = getActivity().getLayoutInflater().inflate(R.layout.traveller_recommend_person, recommendPersonsLayout, false);
        ImageView imageView = (ImageView) recommendPersonView.findViewById(R.id.traveller_recommend_person_img);
        ImageManager.displayPortrait(traveller.getPortraitURL(), imageView);
        recommendPersonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserInfoActivity(traveller);
            }
        });
        return recommendPersonView;
    }

    private void startUserInfoActivity(User traveller) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra(UID, traveller.getUserId());
        intent.putExtra(PORTRAIT_URL, traveller.getPortraitURL());
        startActivity(intent);
    }
}
