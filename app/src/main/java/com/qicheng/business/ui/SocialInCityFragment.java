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
import com.qicheng.business.logic.StationLogic;
import com.qicheng.business.logic.TravellerPersonLogic;
import com.qicheng.business.logic.event.StationEventAargs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.City;
import com.qicheng.business.module.Location;
import com.qicheng.business.module.QueryValue;
import com.qicheng.business.module.Train;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.User;
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

import static com.qicheng.util.Const.Intent.PORTRAIT_URL;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_VALUE;
import static com.qicheng.util.Const.Intent.UID;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.ORDER_BY_NEWEST;
import static com.qicheng.util.Const.QUERY_TYPE_ALL;
import static com.qicheng.util.Const.QUERY_TYPE_BEGIN;
import static com.qicheng.util.Const.QUERY_TYPE_CITY;
import static com.qicheng.util.Const.QUERY_TYPE_COME_CITY;
import static com.qicheng.util.Const.QUERY_TYPE_END;
import static com.qicheng.util.Const.QUERY_TYPE_LEAVE_CITY;
import static com.qicheng.util.Const.QUERY_TYPE_NEAR;
import static com.qicheng.util.Const.QUERY_TYPE_STATION;
import static com.qicheng.util.Const.QUERY_TYPE_TRAIN;
import static com.qicheng.util.Const.STATE_PAUSE_ON_FLING;
import static com.qicheng.util.Const.STATE_PAUSE_ON_SCROLL;

/**
 * SocialInCityFragment.java是启程APP的在城市里交友Fragment类。
 *
 * @author 花树峰
 * @version 1.0 2015年3月11日
 */
public class SocialInCityFragment extends BaseFragment {

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
     * 推荐用户View
     */
    private HorizontalScrollListView recommendPersonsView = null;

    /**
     * 推荐用户列表
     */
    private List<User> recommendPersonList = new ArrayList<User>();

    /**
     * 查询参数LinearLayout
     */
    private LinearLayout queryParamsLayout = null;

    /**
     * 查询参数GridView
     */
    private GridView queryParamsGridView = null;

    /**
     * 查询参数GridView是否存在的标志，默认不存在
     */
    private int isVisible = View.GONE;

    /**
     * 来到用户按钮
     */
    private Button comeBtn = null;

    /**
     * 离开用户按钮
     */
    private Button leaveBtn = null;

    /**
     * 来到用户FrameLayout
     */
    private FrameLayout comeFrameLayout = null;

    /**
     * 离开用户FrameLayout
     */
    private FrameLayout leaveFrameLayout = null;

    /**
     * 来到用户Fragment
     */
    private TravellerPersonFragment comePersonFragment = null;

    /**
     * 离开用户Fragment
     */
    private TravellerPersonFragment leavePersonFragment = null;

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
     * 推荐用户的查询值
     * <p/>
     * 当query_type=0时，该值为车站代码；
     * 当query_type=3时，该值为车次。
     */
    private String recommendQueryValue;

    /**
     * 查询用户信息业务逻辑处理对象
     */
    private TravellerPersonLogic logic = null;

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
     * 车站列表
     */
    private List<TrainStation> stationList = null;

    /**
     * 查询车站信息业务逻辑处理对象
     */
    private StationLogic stationLogic = null;

    /**
     * 城市代码
     */
    private String cityCode = null;

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
        stationLogic = (StationLogic) LogicFactory.self().get(LogicFactory.Type.Station);
        logic = (TravellerPersonLogic) LogicFactory.self().get(LogicFactory.Type.TravellerPerson);
        // 获取当前城市里的车站列表
        stationList = Cache.getInstance().getTripRelatedStationCache(cityCode);
        if (stationList == null) {
            getStationList(cityCode);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View socialView = inflater.inflate(R.layout.fragment_social_in_city, container, false);
        // 获取各种View对象
        recommendLayout = (LinearLayout) socialView.findViewById(R.id.social_recommend_layout);
        recommendPersonsLayout = (LinearLayout) socialView.findViewById(R.id.social_recommend_persons_layout);
        recommendPersonsView = (HorizontalScrollListView) socialView.findViewById(R.id.social_recommend_persons_view);
        queryParamsGridView = (GridView) socialView.findViewById(R.id.query_params_grid_view);
        queryParamsLayout = (LinearLayout) socialView.findViewById(R.id.query_params_layout);
        comeBtn = (Button) socialView.findViewById(R.id.social_person_come_btn);
        leaveBtn = (Button) socialView.findViewById(R.id.social_person_leave_btn);
        comeFrameLayout = (FrameLayout) socialView.findViewById(R.id.social_person_come_Layout);
        leaveFrameLayout = (FrameLayout) socialView.findViewById(R.id.social_person_leave_Layout);
        // 设置推荐用户滚动停止监听器
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
        // 设置来到用户和离开用户按钮监听事件
        comeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                leaveBtn.setTextColor(getResources().getColor(R.color.main));
                comeBtn.setBackgroundColor(getResources().getColor(R.color.main));
                comeBtn.setTextColor(getResources().getColor(R.color.white));
                leaveFrameLayout.setVisibility(View.GONE);
                comeFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        leaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comeBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                comeBtn.setTextColor(getResources().getColor(R.color.main));
                leaveBtn.setBackgroundColor(getResources().getColor(R.color.main));
                leaveBtn.setTextColor(getResources().getColor(R.color.white));
                comeFrameLayout.setVisibility(View.GONE);
                leaveFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        // 设置来到用户和离开用户区域里的各种View对象
        Bundle comePerson = new Bundle();
        comePerson.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_COME_CITY);
        comePerson.putString(TRAVELLER_QUERY_VALUE, cityCode);
        comePersonFragment = new TravellerPersonFragment();
        comePersonFragment.setArguments(comePerson);
        Bundle leavePerson = new Bundle();
        leavePerson.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_LEAVE_CITY);
        leavePerson.putString(TRAVELLER_QUERY_VALUE, cityCode);
        leavePersonFragment = new TravellerPersonFragment();
        leavePersonFragment.setArguments(leavePerson);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.social_person_come_Layout, comePersonFragment);
        fragmentTransaction.add(R.id.social_person_leave_Layout, leavePersonFragment);
        fragmentTransaction.commit();
        comeFrameLayout.setVisibility(View.VISIBLE);
        leaveFrameLayout.setVisibility(View.GONE);
        // 查询推荐用户
        // 默认查询整个门户的推荐用户。
        recommendQueryType = QUERY_TYPE_CITY;
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
            item.setVisible(true);
            item.setTitle(getResources().getString(R.string.station_btn_text));
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity.getIndex() == Const.INDEX_SOCIAL) {
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
            case R.id.user_query_station:
                searchByStation();
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
                logic.queryRecommendUser(recommendQueryType, recommendQueryValue, ORDER_BY_NEWEST, firstUser.getLastLoginTime(), 4, createUIEventListener(new EventListener() {
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
                logic.queryRecommendUser(recommendQueryType, recommendQueryValue, ORDER_BY_EARLIEST, lastUser.getLastLoginTime(), 4, createUIEventListener(new EventListener() {
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

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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
                    title = cityNames[which];
                    getActivity().setTitle(title);
                    recommendLayout.setVisibility(View.VISIBLE);
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    comeBtn.setText(R.string.social_person_come_btn_text);
                    leaveBtn.setText(R.string.social_person_leave_btn_text);
                    // 查询整个门户的推荐用户。
                    recommendQueryType = QUERY_TYPE_CITY;
                    recommendQueryValue = null;
                    String cityCode = cityCodes[which];
                    refreshPerson(QUERY_TYPE_COME_CITY, QUERY_TYPE_LEAVE_CITY, cityCode);
                    // 获取当前城市里的车站列表
                    stationList = Cache.getInstance().getTripRelatedStationCache(cityCode);
                    if (stationList == null) {
                        getStationList(cityCode);
                    }
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
                    recommendLayout.setVisibility(View.VISIBLE);
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    // 创建在车里交友Fragment
                    SocialInTrainFragment socialInTrainFragment = new SocialInTrainFragment();
                    socialInTrainFragment.setTitle(trains[which]);
                    socialInTrainFragment.setTrain(trains[which]);
                    getFragmentManager().beginTransaction().replace(R.id.social_content, socialInTrainFragment).commit();
                }
            });
            builder.show();
        } else {
            Alert.Toast(getResources().getString(R.string.have_no_train_text));
        }
    }

    /**
     * 根据选择的车站查询用户。
     */
    public void searchByStation() {
        if (stationList != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.please_select_station_text));
            int size = stationList.size();
            final String[] stationNames = new String[size];
            for (int i = 0; i < size; i++) {
                stationNames[i] = stationList.get(i).getStationName();
            }
            builder.setItems(stationNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    title = stationNames[which];
                    getActivity().setTitle(title);
                    recommendLayout.setVisibility(View.VISIBLE);
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    comeBtn.setText(R.string.traveller_start_btn_text);
                    leaveBtn.setText(R.string.traveller_end_btn_text);
                    String stationCode = stationList.get(which).getStationCode();
                    // 查询当前车站里的用户，作为推荐用户。
                    recommendQueryType = QUERY_TYPE_STATION;
                    recommendQueryValue = stationCode;
                    refreshPerson(QUERY_TYPE_BEGIN, QUERY_TYPE_END, stationCode);
                }
            });
            builder.show();
        } else {
            Alert.Toast(getResources().getString(R.string.have_no_station_text));
        }
    }

    /**
     * 根据城市代码，获取该城市里的车站列表。
     *
     * @param cityCode 城市代码
     */
    private void getStationList(String cityCode) {
        stationLogic.queryStationByCityCode(cityCode, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                StationEventAargs stationEventAargs = (StationEventAargs) args;
                OperErrorCode errCode = stationEventAargs.getErrCode();
                switch (errCode) {
                    case Success:
                        stationList = stationEventAargs.getStationList();
                        break;
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
            comePersonFragment.refreshPerson();
            leavePersonFragment.refreshPerson();
            startLoading();
        }
    }

    /**
     * 刷新整个页面里的用户。
     *
     * @param comeQueryType  来到查询类型
     * @param leaveQueryType 离开查询类型
     * @param queryValue     查询值
     */
    private void refreshPerson(byte comeQueryType, byte leaveQueryType, String queryValue) {
        refreshRecommendPerson();
        comePersonFragment.setQueryType(comeQueryType);
        comePersonFragment.setQueryValue(queryValue);
        comePersonFragment.refreshPerson();
        leavePersonFragment.setQueryType(leaveQueryType);
        leavePersonFragment.setQueryValue(queryValue);
        leavePersonFragment.refreshPerson();
        startLoading();
    }

    /**
     * 刷新整个页面里的推荐用户。
     */
    private void refreshRecommendPerson() {
        logic.queryRecommendUser(recommendQueryType, recommendQueryValue, ORDER_BY_NEWEST, null, 8, createUIEventListener(new EventListener() {
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
