package com.qicheng.framework.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;


import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.ui.component.Loading;
import com.qicheng.framework.event.UIEventListener;

import com.qicheng.util.Const;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {
	
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
		if(!getLoading().isShow()) {
			getLoading().start(this);
		}
	}
	
	public void stopLoading() {
		getLoading().stop();
	}
	
	public Loading getLoading() {
		if(mLoading == null) {
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
//		DebugLogger.write("onCreate " + this);
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
		if(mLoading != null) {
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

}
