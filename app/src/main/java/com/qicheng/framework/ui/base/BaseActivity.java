package com.qicheng.framework.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;
import com.qicheng.R;
import com.qicheng.business.ui.ChatActivity;
import com.qicheng.business.ui.MainActivity;
import com.qicheng.business.ui.chat.utils.CommonUtils;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.UIEventListener;
import com.qicheng.framework.ui.component.Loading;
import com.qicheng.util.Const;
import com.umeng.analytics.MobclickAgent;


public class BaseActivity extends Activity {
    private static final int notifiId = 11;
    protected NotificationManager notificationManager;


    // helper
    public BaseActivity getActivity() {
        return this;
    }

    // flag
    /**
     * activity是否正在销毁
     * 主要用以判断逻辑层回调后是否继续执行
     */
    private boolean mCreated = false;

    public boolean isCreated() {
        return mCreated;
    }

    // event
    private UIEventListener.Helper mEventListenerHelper = new UIEventListener.Helper();

    public UIEventListener createUIEventListener(EventListener listener) {
        return mEventListenerHelper.createUIEventListener(listener);
    }

    public void addUIEventListener(EventId id, EventListener listener) {
        mEventListenerHelper.add(id, listener);
    }

    public void removeUIEventListener(EventListener listener) {
        mEventListenerHelper.remove(listener);
    }

    // loading
    private Loading mLoading = null;

    public void startLoading() {
        if (!getLoading().isShow()) {
            getLoading().start(this);
        }
    }

    public void stopLoading() {
        getLoading().stop();
    }

    public Loading getLoading() {
        if (mLoading == null) {
            mLoading = new Loading();
        }
        return mLoading;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
//		DebugLogger.write("onAttachFragment " + this);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mCreated = true;
        mEventListenerHelper.setHost(this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    protected void onDestroy() {
        mCreated = false;
        mEventListenerHelper.clear();
//		DebugLogger.write("onDestroy " + this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//		DebugLogger.write("onNewIntent " + this);
    }

    @Override
    protected void onPause() {
//		DebugLogger.write("onPause " + this);
        if (mLoading != null) {
            mLoading.forceStop();
        }
        MobclickAgent.onPause(this);
        Const.Application.setActivityOnStop(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.Application.setCurrentActivity(this);
        Const.Application.setActivityOnStart(this);
        MobclickAgent.onResume(this);
        // onresume时，取消notification显示
        EMChatManager.getInstance().activityResumed();
//		DebugLogger.write("onResume " + this);
    }

//	@Override
//	protected void onResumeFragments() {
//		super.onResume();
////		DebugLogger.write("onResumeFragments " + this);
//	}

    @Override
    protected void onStart() {
        super.onStart();
//		DebugLogger.write("onStart " + this);
    }

    @Override
    protected void onStop() {
//		DebugLogger.write("onStop " + this);
        super.onStop();
    }

    public boolean isActyDestroyed() {
        return super.isFinishing();
    }

    /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     * @param message
     */
    protected void notifyNewMessage(EMMessage message,String nick) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if(!EasyUtils.isAppRunningForeground(this)){
            return;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(false);

        String ticker = CommonUtils.getMessageDigest(message, this);
        String st = getResources().getString(R.string.expression);
        if(message.getType() == EMMessage.Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", st);
        //设置状态栏提示
        mBuilder.setTicker(nick+": " + ticker);
        mBuilder.setContentText("查看新的未读消息");
        mBuilder.setContentTitle(message.getStringAttribute(Const.Easemob.FROM_USER_NICK,"新消息"));

        //必须设置pendingintent，否则在2.3的机器上会有bug
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Const.Intent.HX_NTF_TO_MAIN,true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
//        notificationManager.cancel(notifiId);
    }


}
