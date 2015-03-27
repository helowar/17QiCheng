package com.qicheng;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.easemob.EMCallBack;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.protocol.GetPublicKeyProcess;
import com.qicheng.business.protocol.LoginProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.ui.chat.QichengHXSDKHelper;
import com.qicheng.business.ui.component.BenefitChangedListener;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.net.HttpComm;
import com.qicheng.framework.net.HttpResultCallback;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

public class QichengApplication extends Application {

    public static Context applicationContext;
    private static QichengApplication instance;

    private Activity mShowingActivity = null;
    private Activity mCurrentActivity = null;
    public boolean chatActivityAlive = false;

    public static QichengHXSDKHelper hxSDKHelper = new QichengHXSDKHelper();

    private BenefitChangedListener mBenefitChangedListener;


    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.openActivityDurationTrack(false);
        Const.Application = this;
        //初始化内存缓存
        Cache.getInstance().onCreate();
        applicationContext = this;
        instance = this;
        mBenefitChangedListener = BenefitChangedListener.create();
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
        hxSDKHelper.onInit(this);
    }

    public BenefitChangedListener getBenefitChangedListener() {
        return mBenefitChangedListener;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity value) {
        mCurrentActivity = value;
    }

    public boolean isAllActivityHide() {
        return mShowingActivity == null;
    }

    public void setActivityOnStop(Activity activity) {
        // 一般是先新activity onstart，再老activity onstop
        if (activity == mShowingActivity) {
            mShowingActivity = null;
        }
    }

    public void setActivityOnStart(Activity activity) {
        mShowingActivity = activity;
    }

    public static QichengApplication getInstance() {
        return instance;
    }

    /**
     * 获取内存中好友user list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }

    /**
     * 设置好友user list到内存中
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     *
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(emCallBack);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public JSONObject reLoginAndRepeat(final String url,final String parameter) throws JSONException{
        fetchPublicKey();
        reLogin();
        return rePost(url,parameter);
    }

    private void reLogin(){
        final UserLogic logic =(UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        final LoginProcess process = logic.getProcessForCacheLogin();
        HttpComm comm  = new HttpComm(false);
        comm.post(Const.BASE_URL+process.getRequestUrl(), process.getInfoParameter(), new HttpResultCallback() {

            @Override
            public void onResponse(HttpDownloaderResult success, String url,
                                   String message) {

                if (success == HttpDownloaderResult.eSuccessful) {
                    try{
                        JSONObject o = new JSONObject(message);
                        logic.callbackForCacheLogin(process.getUserFromResult(o));
                    }catch (JSONException e){
                    }
                } else {
                }
            }

            @Override
            public void onProgress(String url, float rate) {
            }
        });
    }

    private void fetchPublicKey(){
        final GetPublicKeyProcess process = new GetPublicKeyProcess();
        HttpComm comm  = new HttpComm(false);
        comm.post(Const.BASE_URL+process.getRequestUrl(), process.getInfoParameter(), new HttpResultCallback() {

            @Override
            public void onResponse(HttpDownloaderResult success, String url,
                                   String message) {

                if (success == HttpDownloaderResult.eSuccessful) {
                    try{
                        JSONObject o = new JSONObject(message);
                        process.storePublicKey(o);
                    }catch (JSONException e){
                    }
                } else {
                }
            }

            @Override
            public void onProgress(String url, float rate) {
            }
        });
    }

    private JSONObject rePost(final String url,final String parameter)throws JSONException{
       HttpComm comm  = new HttpComm(false);
       return new JSONObject( comm.post(url, parameter));
    }
}
