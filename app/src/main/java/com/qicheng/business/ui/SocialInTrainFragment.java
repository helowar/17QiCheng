/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qicheng.R;
import com.qicheng.business.adapter.QueryParamsGridViewAdapter;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TravellerPersonLogic;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserDetailEventArgs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.City;
import com.qicheng.business.module.Location;
import com.qicheng.business.module.QueryValue;
import com.qicheng.business.module.Train;
import com.qicheng.business.module.User;
import com.qicheng.business.module.UserDetail;
import com.qicheng.business.ui.component.HorizontalScrollListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.util.Const.Intent.FRIEND_SOURCE_KEY;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_VALUE;
import static com.qicheng.util.Const.Intent.USER_DETAIL_KEY;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.ORDER_BY_NEWEST;
import static com.qicheng.util.Const.QUERY_TYPE_ALL;
import static com.qicheng.util.Const.QUERY_TYPE_NEAR;
import static com.qicheng.util.Const.QUERY_TYPE_NOT_ON_CAR;
import static com.qicheng.util.Const.QUERY_TYPE_OFF_CAR;
import static com.qicheng.util.Const.QUERY_TYPE_ON_CAR;
import static com.qicheng.util.Const.QUERY_TYPE_TRAIN;
import static com.qicheng.util.Const.STATE_PAUSE_ON_FLING;
import static com.qicheng.util.Const.STATE_PAUSE_ON_SCROLL;

/**
 * SocialInTrainFragment.java是启程APP的在车里交友Fragment类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月11日
 */
public class SocialInTrainFragment extends BaseFragment {

    /**
     * 页面标题
     */
    private String title = null;

    /**
     * 推荐区域Layout
     */
    private LinearLayout recommendLayout = null;

    /**
     * 推荐用户Layout
     */
    private LinearLayout recommendPersonsLayout = null;

    /**
     * 推荐用户列表
     */
    private List<User> recommendPersonList = new ArrayList<User>();

    /**
     * 查询参数LinearLayout
     */
    private LinearLayout queryParamsLayout = null;

    /**
     * 查询参数GridView是否存在的标志，默认不存在
     */
    private int isVisible = View.GONE;

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
     * 未上车车友Fragment
     */
    private TravellerPersonFragment notOnPersonFragment = null;

    /**
     * 上车车友Fragment
     */
    private TravellerPersonFragment onPersonFragment = null;

    /**
     * 下车车友Fragment
     */
    private TravellerPersonFragment offPersonFragment = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    /**
     * 推荐用户的查询类型
     * <p/>
     * 0：车站 3：车次
     * 如果为空，表示获取全站推荐用户
     */
    private byte recommendQueryType;

    /**
     * 查询用户信息业务逻辑处理对象
     */
    private TravellerPersonLogic logic = null;

    /**
     * 用户业务逻辑处理对象
     */
    private UserLogic userLogic = null;

    /**
     * 城市名称列表
     */
    private String[] cityNames = null;

    /**
     * 城市名称列表
     */
    private String[] cityCodes = null;

    /**
     * 车次列表
     */
    private String[] trains = null;

    /**
     * 车次
     */
    private String train = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        List<City> cityList = Cache.getInstance().getTripRelatedCityCache();
        if (cityList != null && cityList.size() > 0) {
            int size = cityList.size();
            cityNames = new String[size];
            cityCodes = new String[size];
            for (int i = 0; i < size; i++) {
                City city = cityList.get(i);
                cityNames[i] = city.getCityName();
                cityCodes[i] = city.getCityCode();
            }
        }
        List<Train> trainList = Cache.getInstance().getTripRelatedTrainCache();
        if (trainList != null && trainList.size() > 0) {
            int size = trainList.size();
            trains = new String[size];
            for (int i = 0; i < size; i++) {
                trains[i] = trainList.get(i).getTrainCode();
            }
        }
        logic = (TravellerPersonLogic) LogicFactory.self().get(LogicFactory.Type.TravellerPerson);
        userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View socialView = inflater.inflate(R.layout.fragment_social_in_train, container, false);
        // 获取各种View对象
        recommendLayout = (LinearLayout) socialView.findViewById(R.id.social_recommend_layout);
        recommendPersonsLayout = (LinearLayout) socialView.findViewById(R.id.social_recommend_persons_layout);
        HorizontalScrollListView recommendPersonsView = (HorizontalScrollListView) socialView.findViewById(R.id.social_recommend_persons_view);
        GridView queryParamsGridView = (GridView) socialView.findViewById(R.id.query_params_grid_view);
        queryParamsLayout = (LinearLayout) socialView.findViewById(R.id.query_params_layout);
        notOnBtn = (Button) socialView.findViewById(R.id.social_person_not_on_btn);
        onBtn = (Button) socialView.findViewById(R.id.social_person_on_btn);
        offBtn = (Button) socialView.findViewById(R.id.social_person_off_btn);
        notOnFrameLayout = (FrameLayout) socialView.findViewById(R.id.social_person_not_on_Layout);
        onFrameLayout = (FrameLayout) socialView.findViewById(R.id.social_person_on_Layout);
        offFrameLayout = (FrameLayout) socialView.findViewById(R.id.social_person_off_Layout);
        // 设置推荐车友滚动停止监听器
        recommendPersonsView.setOnScrollStopListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
        queryParamsGridView.setAdapter(new QueryParamsGridViewAdapter(getActivity()));
        /*为每个item绑定点击事件监听器*/
        queryParamsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    /*当position的位置为0时是按城市搜索用户*/
                    case 0:
                        searchByCity();
                        break;
                    /*当position的位置为1时是按车次搜索用户*/
                    case 1:
                        searchByTrain();
                        break;
                    /*当position的位置为2时是按最新行程关联搜索用户*/
                    case 2:
                        recommendLayout.setVisibility(View.VISIBLE);
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        // 创建交友Fragment
                        SocialFragment socialFragment = new SocialFragment();
                        socialFragment.setTitle(getResources().getString(R.string.relation_btn_text));
                        socialFragment.setQueryType(QUERY_TYPE_ALL);
                        // 默认查询最新行程关联的用户，即根据最新行程的车次查询相同行程的用户，作为推荐用户。
                        socialFragment.setRecommendQueryType(QUERY_TYPE_TRAIN);
                        socialFragment.setSocialPersonText(getResources().getString(R.string.social_relation_person_text));
                        getFragmentManager().beginTransaction().replace(R.id.social_content, socialFragment).commit();
                        break;
                    /*当position的位置为3时是按附近搜索用户*/
                    case 3:
                        recommendLayout.setVisibility(View.VISIBLE);
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        socialFragment = new SocialFragment();
                        socialFragment.setTitle(getResources().getString(R.string.nearby_btn_text));
                        socialFragment.setQueryType(QUERY_TYPE_NEAR);
                        Location location = Cache.getInstance().getUser().getLocation();
                        String queryValue = location.getLongitude() + '|' + location.getLatitude();
                        socialFragment.setQueryValue(queryValue);
                        // 默认查询整个门户的推荐用户。
                        socialFragment.setRecommendQueryType(QUERY_TYPE_NEAR);
                        socialFragment.setSocialPersonText(getResources().getString(R.string.social_near_person_text));
                        getFragmentManager().beginTransaction().replace(R.id.social_content, socialFragment).commit();
                        break;
                    default:
                        recommendLayout.setVisibility(View.VISIBLE);
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        break;
                }

            }
        });
        // 设置未上车车友、上车车友和下车车友区域里的各种View对象
        Bundle notOn = new Bundle();
        notOn.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_NOT_ON_CAR);
        notOn.putString(TRAVELLER_QUERY_VALUE, train);
        notOnPersonFragment = new TravellerPersonFragment();
        notOnPersonFragment.setArguments(notOn);
        Bundle on = new Bundle();
        on.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_ON_CAR);
        on.putString(TRAVELLER_QUERY_VALUE, train);
        onPersonFragment = new TravellerPersonFragment();
        onPersonFragment.setArguments(on);
        Bundle off = new Bundle();
        off.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_OFF_CAR);
        off.putString(TRAVELLER_QUERY_VALUE, train);
        offPersonFragment = new TravellerPersonFragment();
        offPersonFragment.setArguments(off);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.social_person_not_on_Layout, notOnPersonFragment);
        fragmentTransaction.add(R.id.social_person_on_Layout, onPersonFragment);
        fragmentTransaction.add(R.id.social_person_off_Layout, offPersonFragment);
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
        // 查询推荐用户
        // 默认查询最新行程关联的用户，即根据最新行程的车次查询相同行程的用户，作为推荐用户。
        recommendQueryType = QUERY_TYPE_TRAIN;
        refreshRecommendPerson();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        User user = Cache.getInstance().getUser();
        int genderQueryValue = user.getQueryValue().getGender();
        MenuItem item = null;
        if (genderQueryValue == Const.SEX_ALL) {
            item = menu.findItem(R.id.gender_all);
        } else if (genderQueryValue == Const.SEX_MAN) {
            item = menu.findItem(R.id.male);
        } else if (genderQueryValue == Const.SEX_FEMALE) {
            item = menu.findItem(R.id.female);
        }
        if (item != null) {
            item.setChecked(true);
        }
        item = menu.findItem(R.id.user_query_station);
        if (item != null) {
            item.setVisible(false);
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity.getIndex() == Const.INDEX_SOCIAL) {
            mainActivity.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gender_all:
                item.setChecked(true);
                refreshPerson(Const.SEX_ALL);
                break;
            case R.id.male:
                item.setChecked(true);
                refreshPerson(Const.SEX_MAN);
                break;
            case R.id.female:
                item.setChecked(true);
                refreshPerson(Const.SEX_FEMALE);
                break;
            case R.id.user_query_button:
                switch (isVisible) {
                    case View.GONE:
                        recommendLayout.setVisibility(View.GONE);
                        queryParamsLayout.setVisibility(View.VISIBLE);
                        isVisible = View.VISIBLE;
                        break;
                    case View.VISIBLE:
                        recommendLayout.setVisibility(View.VISIBLE);
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        break;
                }
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
                logic.queryRecommendUser(recommendQueryType, null, ORDER_BY_NEWEST, firstUser.getLastLoginTime(), 4, createUIEventListener(new EventListener() {
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
                logic.queryRecommendUser(recommendQueryType, null, ORDER_BY_EARLIEST, lastUser.getLastLoginTime(), 4, createUIEventListener(new EventListener() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    /**
     * 根据选择的城市代码查询用户。
     */
    private void searchByCity() {
        if (cityNames != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.please_select_city_text));
            builder.setItems(cityNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recommendLayout.setVisibility(View.VISIBLE);
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    // 创建在城市里交友Fragment
                    SocialInCityFragment socialInCityFragment = new SocialInCityFragment();
                    socialInCityFragment.setTitle(cityNames[which]);
                    socialInCityFragment.setCityCode(cityCodes[which]);
                    getFragmentManager().beginTransaction().replace(R.id.social_content, socialInCityFragment).commit();
                }
            });
            builder.show();
        } else {
            Alert.Toast(getResources().getString(R.string.have_no_city_text));
        }
    }

    /**
     * 根据选择的车次查询用户。
     */
    private void searchByTrain() {
        if (trains != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.please_select_train_text));
            builder.setItems(trains, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    title = trains[which];
                    getActivity().setTitle(title);
                    recommendLayout.setVisibility(View.VISIBLE);
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    refreshPerson(trains[which]);
                }
            });
            builder.show();
        } else {
            Alert.Toast(getResources().getString(R.string.have_no_train_text));
        }
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
            notOnPersonFragment.refreshPerson();
            onPersonFragment.refreshPerson();
            offPersonFragment.refreshPerson();
            startLoading();
        }
    }

    /**
     * 刷新整个页面里的用户。
     *
     * @param queryValue 查询值
     */
    private void refreshPerson(String queryValue) {
        refreshRecommendPerson();
        notOnPersonFragment.setQueryType(QUERY_TYPE_NOT_ON_CAR);
        notOnPersonFragment.setQueryValue(queryValue);
        notOnPersonFragment.refreshPerson();
        onPersonFragment.setQueryType(QUERY_TYPE_ON_CAR);
        onPersonFragment.setQueryValue(queryValue);
        onPersonFragment.refreshPerson();
        offPersonFragment.setQueryType(QUERY_TYPE_OFF_CAR);
        offPersonFragment.setQueryValue(queryValue);
        offPersonFragment.refreshPerson();
        startLoading();
    }

    /**
     * 刷新整个页面里的推荐用户。
     */
    private void refreshRecommendPerson() {
        logic.queryRecommendUser(recommendQueryType, null, ORDER_BY_NEWEST, null, 8, createUIEventListener(new EventListener() {
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

    private View createRecommendPersonView(final User traveller) {
        // 创建推荐用户View
        View recommendPersonView = getActivity().getLayoutInflater().inflate(R.layout.traveller_recommend_person, recommendPersonsLayout, false);
        ImageView imageView = (ImageView) recommendPersonView.findViewById(R.id.traveller_recommend_person_img);
        ImageManager.displayPortrait(traveller.getPortraitURL(), imageView);
        recommendPersonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUserInfoActivity(traveller.getUserId());
            }
        });
        return recommendPersonView;
    }

    /**
     * 迁移到用户详细信息页面。
     *
     * @param userId 用户ID
     */
    private void startUserInfoActivity(String userId) {
        userLogic.getUserDetail(userId, Const.ID_TYPE_USER_ID, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserDetailEventArgs result = (UserDetailEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    UserDetail userDetail = result.getUserDetail();
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtra(USER_DETAIL_KEY, userDetail);
                    intent.putExtra(FRIEND_SOURCE_KEY, title);
                    startActivity(intent);
                }
            }
        }));
        startLoading();
    }
}
