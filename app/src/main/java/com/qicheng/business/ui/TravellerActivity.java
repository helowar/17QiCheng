package com.qicheng.business.ui;

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
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TravellerPersonLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.component.HorizontalScrollListView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;

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
import static com.qicheng.util.Const.QUERY_TYPE_BEGIN;
import static com.qicheng.util.Const.QUERY_TYPE_END;

/**
 * TravellerActivity.java是启程APP的展现同路车友Activity类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月1日
 */
public class TravellerActivity extends BaseActivity {

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
     * 出发车友按钮
     */
    private Button startBtn = null;

    /**
     * 到达车友按钮
     */
    private Button endBtn = null;

    /**
     * 出发车友FrameLayout
     */
    private FrameLayout startFrameLayout = null;

    /**
     * 到达车友FrameLayout
     */
    private FrameLayout endFrameLayout = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    /**
     * 出发车友Fragment
     */
    private TravellerPersonFragment startTravellerFragment = null;

    /**
     * 到达车友Fragment
     */
    private TravellerPersonFragment endTravellerFragment = null;

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
        setContentView(R.layout.activity_traveller);
        // 获取上一个Activity传递过来的查询值
        Bundle extras = getIntent().getExtras();
        String queryValue = extras.getString(TRAVELLER_QUERY_VALUE);
        // 获取各种View对象
        recommendPersonsView = (HorizontalScrollListView) findViewById(R.id.traveller_recommend_persons_view);
        recommendPersonsLayout = (LinearLayout) findViewById(R.id.traveller_recommend_persons_layout);
        startBtn = (Button) findViewById(R.id.traveller_start_btn);
        endBtn = (Button) findViewById(R.id.traveller_end_btn);
        startFrameLayout = (FrameLayout) findViewById(R.id.traveller_start_Layout);
        endFrameLayout = (FrameLayout) findViewById(R.id.traveller_end_Layout);
        // 设置推荐车友滚动停止监听器
        recommendPersonsView.setOnScrollStopListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
        // 设置出发车友和到达车友区域里的各种View对象
        startTravellerFragment = new TravellerPersonFragment();
        Bundle start = new Bundle();
        start.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_BEGIN);
        start.putString(TRAVELLER_QUERY_VALUE, queryValue);
        startTravellerFragment.setArguments(start);
        endTravellerFragment = new TravellerPersonFragment();
        Bundle end = new Bundle();
        end.putByte(TRAVELLER_QUERY_TYPE, QUERY_TYPE_END);
        end.putString(TRAVELLER_QUERY_VALUE, queryValue);
        endTravellerFragment.setArguments(end);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.traveller_start_Layout, startTravellerFragment);
        fragmentTransaction.add(R.id.traveller_end_Layout, endTravellerFragment);
        fragmentTransaction.commit();
        startFrameLayout.setVisibility(View.VISIBLE);
        endFrameLayout.setVisibility(View.GONE);
        // 设置出发车友和到达车友按钮监听事件
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                endBtn.setTextColor(getResources().getColor(R.color.main));
                startBtn.setBackgroundColor(getResources().getColor(R.color.main));
                startBtn.setTextColor(getResources().getColor(R.color.white));
                endFrameLayout.setVisibility(View.GONE);
                startFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                startBtn.setTextColor(getResources().getColor(R.color.main));
                endBtn.setBackgroundColor(getResources().getColor(R.color.main));
                endBtn.setTextColor(getResources().getColor(R.color.white));
                startFrameLayout.setVisibility(View.GONE);
                endFrameLayout.setVisibility(View.VISIBLE);
            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_traveller, menu);
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
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(UID, traveller.getUserId());
        intent.putExtra(PORTRAIT_URL, traveller.getPortraitURL());
        startActivity(intent);
    }
}
