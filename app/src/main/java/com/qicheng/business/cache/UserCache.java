package com.qicheng.business.cache;

import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;

/**
 * Created by NO1 on 2015/1/20.
 */
public class UserCache {

    private User mUser = null;

    public void onCreate() {
        mUser = PersistorManager.getInstance().getLastestUser();
    }

    public User getCashedUser() {
        return mUser;
    }

    public void setCache(User user) {
        mUser = user;
        PersistorManager.getInstance().setUser(user);
    }
}
