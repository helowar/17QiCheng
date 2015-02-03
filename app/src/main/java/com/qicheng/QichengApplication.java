package com.qicheng;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheFileCount(60)//Set max cache file count in SD card
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        //Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
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
