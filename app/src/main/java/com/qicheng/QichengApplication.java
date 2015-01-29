package com.qicheng;

import android.app.Application;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

public class QichengApplication extends Application {
	
	private BaseActivity mShowingActivity = null;
	private BaseActivity mCurrentActivity = null;

	@Override
	public void onCreate() {
		super.onCreate();
		
		Const.Application = this;
        Cache.getInstance().onCreate();
//		LogicFactory.self().getUser().appStrat();
	}
	
	public BaseActivity getCurrentActivity() {
		return mCurrentActivity;
	}
	
	public void setCurrentActivity(BaseActivity value) {
		mCurrentActivity = value;
	}
	
	public boolean isAllActivityHide() {
		return mShowingActivity == null;
	}
	
	public void setActivityOnStop(BaseActivity activity) {
		// 一般是先新activity onstart，再老activity onstop
		if(activity == mShowingActivity) {
			mShowingActivity = null;
		}
	}
	
	public void setActivityOnStart(BaseActivity activity) {
		mShowingActivity = activity;
	}
}
