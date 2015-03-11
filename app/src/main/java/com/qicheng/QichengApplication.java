package com.qicheng;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qicheng.business.cache.Cache;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

import java.io.File;

public class QichengApplication extends Application {

    private BaseActivity mShowingActivity = null;
    private BaseActivity mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Const.Application = this;
        //初始化内存缓存
        Cache.getInstance().onCreate();
//		LogicFactory.self().getUser().appStrat();

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(new File(Const.WorkDir+"img/")))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(100)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        //Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
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
        if (activity == mShowingActivity) {
            mShowingActivity = null;
        }
    }

    public void setActivityOnStart(BaseActivity activity) {
        mShowingActivity = activity;
    }
}
