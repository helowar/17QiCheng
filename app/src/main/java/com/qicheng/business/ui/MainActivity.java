package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qicheng.R;
import com.qicheng.business.service.LocationService;
import com.qicheng.business.ui.component.BadgeView;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;
import com.slidingmenu.lib.SlidingMenu;

public class MainActivity extends BaseActivity {

    private static Logger logger = new Logger("com.qicheng.business.ui.MainActivity");

    private RadioButton tripRb;
    private RadioButton actyRb;
    private RadioButton socialRb;
    private RadioButton messageRb;
    private RadioButton ticketRb;

    BadgeView messageBadge;
    BadgeView ticketBadge;

    private MessageFragment messageFragment;
    private TripListFragment tripFragment;
    private VoucherFragment voucherFragment;
    private SocialFragment socialFragment;
    private ActyFragment actyFragment;

    private String userToken;
    private SlidingMenu menu;

    private int index = Const.INDEX_TRIP;

    public static MainActivity instanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        userToken = getIntent().getStringExtra("token");
        logger.d("Get the user token:" + userToken);
        // initView();
        initSlidingMenu();
        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);
        instanceState = this;

    }

    public void initView() {
        tripFragment = new TripListFragment();
        getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
        findViewById(R.id.trip_content).setVisibility(View.VISIBLE);
        tripRb = (RadioButton) findViewById(R.id.rbTrip);
        actyRb = (RadioButton) findViewById(R.id.rbSocial);
        socialRb = (RadioButton) findViewById(R.id.rbActy);
        messageRb = (RadioButton) findViewById(R.id.rbMessage);
        ticketRb = (RadioButton) findViewById(R.id.rbTicket);
        //附加Badge
        messageBadge = new BadgeView(getActivity());
        messageBadge.setMaxCount(99);
        messageBadge.setHideOnNull(true);
        messageBadge.setBadgeMargin(4);
        messageBadge.setTargetView(messageRb);
        messageBadge.setBadgeCount(100);
        ticketBadge = new BadgeView(getActivity());
        ticketBadge.setHideOnNull(true);
        ticketBadge.setBadgeMargin(4);
        ticketBadge.setTargetView(ticketRb);
        ticketBadge.setBadgeCount(8);
        BadgeView tripBadge = new BadgeView(getActivity());
        tripBadge.setHideOnNull(true);
        tripBadge.setBadgeMargin(4);
        tripBadge.setTargetView(tripRb);
        tripBadge.setText("0");
        BadgeView actyBadge = new BadgeView(getActivity());
        actyBadge.setHideOnNull(true);
        actyBadge.setBadgeMargin(4);
        actyBadge.setTargetView(actyRb);
        actyBadge.setText("0");
        BadgeView socialBadge = new BadgeView(getActivity());
        socialBadge.setHideOnNull(true);
        socialBadge.setBadgeMargin(4);
        socialBadge.setTargetView(socialRb);
        socialBadge.setText("0");
        //添加点击监听器
        View.OnClickListener checkedListener = new RadioButtonOnClickListener();
        tripRb.setOnClickListener(checkedListener);
        actyRb.setOnClickListener(checkedListener);
        socialRb.setOnClickListener(checkedListener);
        messageRb.setOnClickListener(checkedListener);
        ticketRb.setOnClickListener(checkedListener);

    }


    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu() {
        // 设置主界面视图
        setContentView(R.layout.activity_main);
        // getFragmentManager().beginTransaction().replace(R.id.content_frame, new TripListFragment()).commit();
        initView();
        // 设置滑动菜单的属性值
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 设置滑动菜单的视图界面
        menu.setMenu(R.layout.menu_frame);
        getFragmentManager().beginTransaction().replace(R.id.menu_frame, new TopMenuFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*判断当前是哪个fragment并设置actionbar*/
        switch (index) {
            case Const.INDEX_TRIP:
                getMenuInflater().inflate(R.menu.menu_main, menu);
                getActivity().setTitle(getResources().getString(R.string.title_activity_main));
                break;
            case Const.INDEX_SOCIAL:
                getActivity().setTitle("交友");
                break;
            case Const.INDEX_ACTIVITY:
                getMenuInflater().inflate(R.menu.menu_activity, menu);
                getActivity().setTitle(getResources().getString(R.string.activity_title));
                break;
            case Const.INDEX_MESSAGE:
                getActivity().setTitle("消息");
                break;
            case Const.INDEX_VOUCHER:
                getActivity().setTitle("代金券");
                break;
        }
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            menu.toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Intent locationService = new Intent(this, LocationService.class);
        stopService(locationService);
        super.onDestroy();
    }

    private class RadioButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tripRb.setChecked(false);
            actyRb.setChecked(false);
            socialRb.setChecked(false);
            messageRb.setChecked(false);
            ticketRb.setChecked(false);
            RadioButton target = (RadioButton) v;
            target.setChecked(true);
            onCheckedChanged(target.getId());
        }
    }

    private void activatedFrame(int id) {
        findViewById(R.id.trip_content).setVisibility(View.GONE);
        findViewById(R.id.social_content).setVisibility(View.GONE);
        findViewById(R.id.acty_content).setVisibility(View.GONE);
        findViewById(R.id.message_content).setVisibility(View.GONE);
        findViewById(R.id.user_content).setVisibility(View.GONE);
        findViewById(id).setVisibility(View.VISIBLE);
    }

    private void onCheckedChanged(int checkedId) {
        switch (checkedId) {
            case R.id.rbMessage:
                index = Const.INDEX_MESSAGE;
                getActivity().invalidateOptionsMenu();
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    getFragmentManager().beginTransaction().add(R.id.message_content, messageFragment)
                            .commit();
                }
                activatedFrame(R.id.message_content);
                break;
            case R.id.rbTrip:
                index = Const.INDEX_TRIP;
                getActivity().invalidateOptionsMenu();
                if (tripFragment == null) {
                    tripFragment = new TripListFragment();
                    getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
                }
                activatedFrame(R.id.trip_content);
                break;
            case R.id.rbSocial:
                index = Const.INDEX_SOCIAL;
                getActivity().invalidateOptionsMenu();
                if (socialFragment == null) {
                    socialFragment = new SocialFragment();
                    getFragmentManager().beginTransaction().add(R.id.social_content, socialFragment)
                            .commit();
                }
                activatedFrame(R.id.social_content);
                break;
            case R.id.rbTicket:
                index = Const.INDEX_VOUCHER;
                getActivity().invalidateOptionsMenu();
                if (voucherFragment == null) {
                    voucherFragment = new VoucherFragment();
                    getFragmentManager().beginTransaction().add(R.id.user_content, voucherFragment)
                            .commit();
                }
                activatedFrame(R.id.user_content);
                break;
            case R.id.rbActy:
                index = Const.INDEX_ACTIVITY;
                getActivity().invalidateOptionsMenu();
                if (actyFragment == null) {
                    actyFragment = new ActyFragment();
                    getFragmentManager().beginTransaction().add(R.id.acty_content, actyFragment)
                            .commit();
                }
                activatedFrame(R.id.acty_content);
                break;
            default:
                break;
        }
    }

    public void incrementMessageCount(int increment) {
        messageBadge.incrementBadgeCount(increment);
    }

    public void decrementMessageCount(int decrement) {
        messageBadge.decrementBadgeCount(decrement);
    }

    public void incrementTicketCount(int increment) {
        ticketBadge.incrementBadgeCount(increment);
    }

    public void decrementTicketCount(int decrement) {
        ticketBadge.decrementBadgeCount(decrement);
    }

}
