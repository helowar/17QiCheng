package com.qicheng.business.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RadioGroup;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private RadioGroup myTabRg;

    private MessageFragment messageFragment;
    private TripListFragment tripFragment;
    private UserinfoFragment userinfoFragment;
    private SocialFragment socialFragment;
    private ActyFragment actyFragment;

    private ActionBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void initView() {
        tripFragment = new TripListFragment();
        getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
        findViewById(R.id.trip_content).setVisibility(View.VISIBLE);
        myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
        myTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            private void activatedFrame(int id){
                findViewById(R.id.trip_content).setVisibility(View.GONE);
                findViewById(R.id.social_content).setVisibility(View.GONE);
                findViewById(R.id.acty_content).setVisibility(View.GONE);
                findViewById(R.id.message_content).setVisibility(View.GONE);
                findViewById(R.id.user_content).setVisibility(View.GONE);

                findViewById(id).setVisibility(View.VISIBLE);
            }

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.rbMessage:
                        if(messageFragment==null){
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
                        if(socialFragment==null){
                            socialFragment = new SocialFragment();
                            getFragmentManager().beginTransaction().add(R.id.social_content, socialFragment)
                                    .commit();
                        }
                        activatedFrame(R.id.social_content);
                        break;
                    case R.id.rbMe:
                        if(userinfoFragment==null){
                            userinfoFragment = new UserinfoFragment();
                            getFragmentManager().beginTransaction().add(R.id.user_content, userinfoFragment)
                                    .commit();
                        }
                        activatedFrame(R.id.user_content);
                        break;
                    case R.id.rbActy:
                        if(actyFragment==null){
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
