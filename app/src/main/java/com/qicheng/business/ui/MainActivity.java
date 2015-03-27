package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.qicheng.R;
import com.qicheng.business.service.LocationService;
import com.qicheng.business.ui.chat.db.UserDao;
import com.qicheng.business.ui.component.BadgeView;
import com.qicheng.business.ui.component.BenefitChangedListener;
import com.qicheng.framework.ui.base.BaseFragmentActivity;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;
import com.slidingmenu.lib.SlidingMenu;

import static com.qicheng.util.Const.Application;
import static com.qicheng.util.Const.QUERY_TYPE_ALL;
import static com.qicheng.util.Const.QUERY_TYPE_TRAIN;

public class MainActivity extends BaseFragmentActivity {

    private static Logger logger = new Logger("com.qicheng.business.ui.MainActivity");

    private RadioButton tripRb;
    private RadioButton actyRb;
    private RadioButton socialRb;
    private RadioButton messageRb;
    private RadioButton benefitRb;

    private RadioGroup bottomBar;

    BadgeView messageBadge;
    BadgeView ticketBadge;

    private MessageFragment messageFragment;
    private TripListFragment tripFragment;
    private BenifitFragment benefitFragment;
    private SocialFragment socialFragment;
    private ActyFragment actyFragment;

    private String userToken;
    private SlidingMenu menu;
    private NewMessageBroadcastReceiver receiver;

    private int index = Const.INDEX_TRIP;
    private UserDao dao = new UserDao(Const.Application);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        userToken = getIntent().getStringExtra("token");
        logger.d("Get the user token:" + userToken);
        initSlidingMenu();
        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);
        receiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        registerReceiver(receiver, intentFilter);
    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 记得把广播给终结掉
            if( !Application.chatActivityAlive){
                abortBroadcast();
                setUnreadMessageCount();
                String msgid = intent.getStringExtra("msgid");
                // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
                final EMMessage message = EMChatManager.getInstance().getMessage(msgid);
                if(messageFragment!=null){
                    messageFragment.refresh();
                }
                notifyNewMessage(message,message.getStringAttribute(Const.Easemob.FROM_USER_NICK,message.getFrom()));
            }

            // 如果是群聊消息，获取到group id
//            if (message.getChatType() == EMMessage.ChatType.GroupChat) {
//                username = message.getTo();
//            }
//            if (!username.equals(toChatUsername)) {
            // 消息不是发给当前会话，return

//            }
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername);
            // 通知adapter有新消息，更新ui
//            adapter.refresh();
//            listView.setSelection(listView.getCount() - 1);

        }
    }

    private void setUnreadMessageCount(){
        int count=0;
        for(String str:EMChatManager.getInstance().getConversationsUnread()){
            count+= EMChatManager.getInstance().getConversation(str).getUnreadMsgCount();
        }
        messageBadge.setText(count+"");
    }

    public void initView() {
        bottomBar = (RadioGroup)findViewById(R.id.tab_menu);
        tripFragment = new TripListFragment();
        getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
        findViewById(R.id.trip_content).setVisibility(View.VISIBLE);
        tripRb = (RadioButton) findViewById(R.id.rbTrip);
        actyRb = (RadioButton) findViewById(R.id.rbSocial);
        socialRb = (RadioButton) findViewById(R.id.rbActy);
        messageRb = (RadioButton) findViewById(R.id.rbMessage);
        benefitRb = (RadioButton) findViewById(R.id.rbBenefit);
        //附加Badge
        messageBadge = new BadgeView(getActivity());
        messageBadge.setMaxCount(99);
        messageBadge.setHideOnNull(true);
        messageBadge.setBadgeMargin(4);
        messageBadge.setTargetView(messageRb);
        messageBadge.setBadgeCount(0);
        ticketBadge = new BadgeView(getActivity());
        ticketBadge.setHideOnNull(true);
        ticketBadge.setBadgeMargin(4);
        ticketBadge.setTargetView(benefitRb);
        ticketBadge.setBadgeCount(8);
        //初始化BenefitChangedListener
        BenefitChangedListener listener = Const.Application.getBenefitChangedListener();
        listener.setBenefitBadge(ticketBadge);
        //设置福利数初始值
        listener.initBenefitBadge();
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
        benefitRb.setOnClickListener(checkedListener);
        fromChatNotification();
    }

    private void fromChatNotification(){
        Intent i = getIntent();
        if(i.getBooleanExtra(Const.Intent.HX_NTF_TO_MAIN,false)){
            tripRb.setChecked(false);
            actyRb.setChecked(false);
            socialRb.setChecked(false);
            messageRb.setChecked(true);
            benefitRb.setChecked(false);
            onCheckedChanged(messageRb.getId());
        }
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
                setTitle(getResources().getString(R.string.title_activity_main));
                break;
            case Const.INDEX_SOCIAL:
                getMenuInflater().inflate(R.menu.menu_social, menu);
                break;
            case Const.INDEX_ACTIVITY:
                getMenuInflater().inflate(R.menu.menu_activity, menu);
                setTitle(getResources().getString(R.string.activity_title));
                break;
            case Const.INDEX_MESSAGE:
                getMenuInflater().inflate(R.menu.menu_conversation_list, menu);
                setTitle("消息");
                break;
            case Const.INDEX_VOUCHER:
                getMenuInflater().inflate(R.menu.menu_benefit, menu);
                setTitle("福利");
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
    protected void onResume() {
        super.onResume();
        setUnreadMessageCount();
        if(messageFragment!=null){
            messageFragment.refresh();
        }
    }

    @Override
    protected void onDestroy() {
        Intent locationService = new Intent(this, LocationService.class);
        stopService(locationService);
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
        }
    }

    private class RadioButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            tripRb.setChecked(false);
            actyRb.setChecked(false);
            socialRb.setChecked(false);
            messageRb.setChecked(false);
            benefitRb.setChecked(false);
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
        findViewById(R.id.benefit_content).setVisibility(View.GONE);
        findViewById(id).setVisibility(View.VISIBLE);
    }

    private void onCheckedChanged(int checkedId) {
        switch (checkedId) {
            case R.id.rbMessage:
                index = Const.INDEX_MESSAGE;
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    getFragmentManager().beginTransaction().add(R.id.message_content, messageFragment).commit();
                } else {
                    invalidateOptionsMenu();
                    messageFragment.refresh();
                }
                // onresume时，取消notification显示
                EMChatManager.getInstance().activityResumed();
                activatedFrame(R.id.message_content);
                break;
            case R.id.rbTrip:
                index = Const.INDEX_TRIP;
                if (tripFragment == null) {
                    tripFragment = new TripListFragment();
                    getFragmentManager().beginTransaction().add(R.id.trip_content, tripFragment).commit();
                } else {
                    invalidateOptionsMenu();
                }
                activatedFrame(R.id.trip_content);
                break;
            case R.id.rbSocial:
                index = Const.INDEX_SOCIAL;
                if (socialFragment == null) {
                    socialFragment = new SocialFragment();
                    socialFragment.setTitle(getResources().getString(R.string.social_fragment_title));
                    socialFragment.setQueryType(QUERY_TYPE_ALL);
                    // 默认查询最新行程关联的用户，即根据最新行程的车次查询相同行程的用户，作为推荐用户。
                    socialFragment.setRecommendQueryType(QUERY_TYPE_TRAIN);
                    socialFragment.setSocialPersonText(getResources().getString(R.string.social_relation_person_text));
                    getFragmentManager().beginTransaction().add(R.id.social_content, socialFragment).commit();
                } else {
                    invalidateOptionsMenu();
                }
                activatedFrame(R.id.social_content);
                break;
            case R.id.rbBenefit:
                index = Const.INDEX_VOUCHER;
                if (benefitFragment == null) {
                    benefitFragment = new BenifitFragment();
                    getFragmentManager().beginTransaction().add(R.id.benefit_content, benefitFragment).commit();
                } else {
                    invalidateOptionsMenu();
                    benefitFragment.updateInitView();
                }
                activatedFrame(R.id.benefit_content);
                break;
            case R.id.rbActy:
                index = Const.INDEX_ACTIVITY;
                if (actyFragment == null) {
                    actyFragment = new ActyFragment();
                    getFragmentManager().beginTransaction().add(R.id.acty_content, actyFragment).commit();
                } else {
                    invalidateOptionsMenu();
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

    public int getIndex() {
        return index;
    }

    public void hideBottomBar(){
        bottomBar.setVisibility(View.GONE);
    }

    public void showBottomBar(){
        bottomBar.setVisibility(View.VISIBLE);
    }


}
