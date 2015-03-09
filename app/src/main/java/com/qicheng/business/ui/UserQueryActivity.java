/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qicheng.R;
import com.qicheng.business.adapter.QueryParamsGridViewAdapter;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.DynLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.StationLogic;
import com.qicheng.business.logic.event.StationEventAargs;
import com.qicheng.business.module.City;
import com.qicheng.business.module.Location;
import com.qicheng.business.module.QueryValue;
import com.qicheng.business.module.Train;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.User;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.util.Const;

import java.util.List;

import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_VALUE;
import static com.qicheng.util.Const.QUERY_TYPE_ALL;

/**
 * UserQueryActivity.java是启程APP的查询用户Activity类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月1日
 */
public class UserQueryActivity extends BaseActivity {

    /**
     * 用户Fragment标签
     */
    private static final String USER_FRAGMENT_TAG = "user_fragment_tag";

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
     * 用户FrameLayout
     */
    private FrameLayout userFrameLayout = null;

    /**
     * 用户Fragment
     */
    private TravellerPersonFragment userFragment = null;

    /**
     * Fragment事物管理对象
     */
    private FragmentTransaction fragmentTransaction = null;

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
     * 页面标题
     */
    private String title = null;

    /**
     * 当前查询类型是否是城市
     */
    private boolean isCityQueryType = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_query);
        title = getResources().getString(R.string.newest_btn_text);
        queryParamsGridView = (GridView) findViewById(R.id.query_params_grid_view);
        userFrameLayout = (FrameLayout) findViewById(R.id.user_query_users_Layout);
        // 设置用户区域里的各种View对象
        userFragment = new TravellerPersonFragment();
        Bundle queryParams = new Bundle();
        queryParams.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_ALL);
        queryParams.putString(TRAVELLER_QUERY_VALUE, null);
        userFragment.setArguments(queryParams);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.user_query_users_Layout, userFragment, USER_FRAGMENT_TAG);
        fragmentTransaction.commit();
        userFrameLayout.setVisibility(View.VISIBLE);
        queryParamsGridView.setAdapter(new QueryParamsGridViewAdapter(this, this));
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
                    /*当position的位置为2时是按最新搜索用户*/
                    case 2:
                        title = getResources().getString(R.string.newest_btn_text);
                        getActivity().invalidateOptionsMenu();
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        refreshPerson(Const.QUERY_TYPE_ALL, null);
                        isCityQueryType = false;
                        break;
                    /*当position的位置为3时是按附近搜索用户*/
                    case 3:
                        title = getResources().getString(R.string.nearby_btn_text);
                        getActivity().invalidateOptionsMenu();
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        Location location = Cache.getInstance().getUser().getLocation();
                        String queryValue = location.getLongitude() + '|' + location.getLatitude();
                        refreshPerson(Const.QUERY_TYPE_NEAR, queryValue);
                        isCityQueryType = false;
                        break;
                    default:
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        break;
                }

            }
        });
        List<City> cityList = Cache.getInstance().getTripRelatedCityCache();
        int size = cityList.size();
        cityNames = new String[size];
        cityCodes = new String[size];
        for (int i = 0; i < size; i++) {
            City city = cityList.get(i);
            cityNames[i] = city.getCityName();
            cityCodes[i] = city.getCityCode();
        }
        List<Train> trainList = Cache.getInstance().getTripRelatedTrainCache();
        size = trainList.size();
        trains = new String[size];
        for (int i = 0; i < size; i++) {
            trains[i] = trainList.get(i).getTrainCode();
        }
        stationLogic = (StationLogic) LogicFactory.self().get(LogicFactory.Type.Station);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_query, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        User user = Cache.getInstance().getUser();
        int genderQueryValue = user.getQueryValue().getGender();
        if (genderQueryValue == Const.SEX_ALL) {
            menu.findItem(R.id.gender_all).setChecked(true);
        } else if (genderQueryValue == Const.SEX_MAN) {
            menu.findItem(R.id.male).setChecked(true);
        } else if (genderQueryValue == Const.SEX_FEMALE) {
            menu.findItem(R.id.female).setChecked(true);
        }
        menu.findItem(R.id.user_query_title).setTitle(title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.user_query_button:
                queryParamsLayout = (LinearLayout) findViewById(R.id.query_params_layout);
                switch (isVisible) {
                    case View.GONE:
                        queryParamsLayout.setVisibility(View.VISIBLE);
                        isVisible = View.VISIBLE;
                        break;
                    case View.VISIBLE:
                        queryParamsLayout.setVisibility(View.GONE);
                        isVisible = View.GONE;
                        break;
                }
                break;
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
            case R.id.user_query_title:
                if (isCityQueryType) {
                    searchByStation();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 根据选择的城市代码查询用户。
     */
    private void searchByCity() {
        if (cityNames != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.please_select_city_text));
            builder.setItems(cityNames, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    title = cityNames[which];
                    Toast.makeText(getActivity(), getResources().getString(R.string.selected_city_text) + title, Toast.LENGTH_SHORT).show();
                    getActivity().invalidateOptionsMenu();
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    String cityCode = cityCodes[which];
                    isCityQueryType = true;
                    refreshPerson(Const.QUERY_TYPE_CITY, cityCode);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.please_select_train_text));
            builder.setItems(trains, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    title = trains[which];
                    Toast.makeText(getActivity(), getResources().getString(R.string.selected_train_text) + title, Toast.LENGTH_SHORT).show();
                    getActivity().invalidateOptionsMenu();
                    isCityQueryType = false;
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    refreshPerson(Const.QUERY_TYPE_TRAIN, trains[which]);
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.selected_station_text) + title, Toast.LENGTH_SHORT).show();
                    getActivity().invalidateOptionsMenu();
                    isCityQueryType = false;
                    queryParamsLayout.setVisibility(View.GONE);
                    isVisible = View.GONE;
                    refreshPerson(Const.QUERY_TYPE_STATION, stationList.get(which).getStationCode());
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
            FragmentManager manager = getFragmentManager();
            TravellerPersonFragment fragment = null;
            fragment = (TravellerPersonFragment) manager.findFragmentByTag(USER_FRAGMENT_TAG);
            fragment.refreshPerson();
            startLoading();
        }
    }

    /**
     * 刷新整个页面里的用户。
     *
     * @param queryType  查询类型
     * @param queryValue 查询值
     */
    private void refreshPerson(byte queryType, String queryValue) {
        FragmentManager manager = getFragmentManager();
        TravellerPersonFragment fragment = null;
        fragment = (TravellerPersonFragment) manager.findFragmentByTag(USER_FRAGMENT_TAG);
        fragment.setQueryType(queryType);
        fragment.setQueryValue(queryValue);
        fragment.refreshPerson();
        startLoading();
    }
}
