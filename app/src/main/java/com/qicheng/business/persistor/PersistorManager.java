package com.qicheng.business.persistor;

import com.qicheng.business.module.User;

/**
 * Created by NO1 on 2015/1/20.
 */
public class PersistorManager {
    private static PersistorManager ourInstance = new PersistorManager();

    public static PersistorManager getInstance() {
        return ourInstance;
    }

    private PersistorManager() {
    }

    public User getLastestUser() {
        return new UserPreferences().getLastest();
    }

    public void setUser(User user) {
        new UserPreferences().set(user);
    }
}
