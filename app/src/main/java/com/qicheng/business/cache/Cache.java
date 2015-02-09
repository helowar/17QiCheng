package com.qicheng.business.cache;

import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;

/**
 * Created by NO1 on 2015/1/18.
 */
public class Cache {

    private static Cache ins = new Cache();


    public static Cache getInstance() {
        return ins;
    }

    private User mUser;
    private String publicKey;

    private UserCache mUserCache = new UserCache();

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public void clear() {
        setUser(null);

    }

    public void onCreate() {
        /*用户对象缓存*/
        mUserCache.onCreate();
        /*公钥缓存*/
        publicKey = PersistorManager.getInstance().getPublicKey();
    }

    public void setCacheUser(User user) {
        mUser = user;
        mUserCache.setCache(user);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        PersistorManager.getInstance().savePublicKey(publicKey);
    }
}
