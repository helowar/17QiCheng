package com.qicheng.business.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.util.Logger;

public class MainActivity extends BaseActivity {

    private static Logger logger = new Logger("com.qicheng.business.ui.MainActivity");

    private RadioGroup myTabRg;

    private MessageFragment messageFragment;
    private TripListFragment tripFragment;
    private UserinfoFragment userinfoFragment;
    private SocialFragment socialFragment;
    private ActyFragment actyFragment;

    private ActionBar topBar;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userToken = getIntent().getStringExtra("token");
        logger.d("Get the user token:" + userToken);
        initView();
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
                    case R.id.rbMe:
                        if (userinfoFragment == null) {
                            userinfoFragment = new UserinfoFragment();
                            getFragmentManager().beginTransaction().add(R.id.user_content, userinfoFragment)
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
}
