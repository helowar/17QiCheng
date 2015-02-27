package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.qicheng.R;
import com.qicheng.business.module.Label;
import com.qicheng.business.service.LocationService;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.util.Logger;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static Logger logger = new Logger("com.qicheng.business.ui.MainActivity");

    private RadioGroup myTabRg;

    private MessageFragment messageFragment;
    private TripListFragment tripFragment;
    private VoucherFragment voucherFragment;
    private SocialFragment socialFragment;
    private ActyFragment actyFragment;

    private String userToken;
    private SlidingMenu menu;

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
    }

    public void initView() {
        tripFragment = new TripListFragment();
        getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
        findViewById(R.id.trip_content).setVisibility(View.VISIBLE);
        myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
        myTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            private void activatedFrame(int id) {
                findViewById(R.id.trip_content).setVisibility(View.GONE);
                findViewById(R.id.social_content).setVisibility(View.GONE);
                findViewById(R.id.acty_content).setVisibility(View.GONE);
                findViewById(R.id.message_content).setVisibility(View.GONE);
                findViewById(R.id.user_content).setVisibility(View.GONE);

                findViewById(id).setVisibility(View.VISIBLE);
            }

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbMessage:
                        if (messageFragment == null) {
                            messageFragment = new MessageFragment();
                            getFragmentManager().beginTransaction().add(R.id.message_content, messageFragment)
                                    .commit();
                        }
                        activatedFrame(R.id.message_content);
                        break;
                    case R.id.rbTrip:
                        if (tripFragment == null) {
                            tripFragment = new TripListFragment();
                            getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
                        }
                        activatedFrame(R.id.trip_content);
                        break;
                    case R.id.rbSocial:
                        if (socialFragment == null) {
                            socialFragment = new SocialFragment();
                            getFragmentManager().beginTransaction().add(R.id.social_content, socialFragment)
                                    .commit();
                        }
                        activatedFrame(R.id.social_content);
                        break;
                    case R.id.rbVoucher:
                        if (voucherFragment == null) {
                            voucherFragment = new VoucherFragment();
                            getFragmentManager().beginTransaction().add(R.id.user_content, voucherFragment)
                                    .commit();
                        }
                        activatedFrame(R.id.user_content);
                        break;
                    case R.id.rbActy:
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
        });
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //Intent intent = new Intent(this,RegisterLabelSelectActivity.class);
            //startActivity(intent);
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
}
