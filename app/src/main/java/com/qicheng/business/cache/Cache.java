package com.qicheng.business.cache;

import com.qicheng.business.module.User;

/**
 * Created by NO1 on 2015/1/18.
 */
public class Cache {

    private static Cache ins = new Cache();

    public static Cache getInstance(){return ins;}

    private User mUser;

    private UserCache mUserCache= new UserCache();

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public void clear(){
        setUser(null);

    }

    public void onCreate(){
        mUserCache.onCreate();
    }

    public void setCacheUser(User user){
        mUser=user;
        mUserCache.setCache(user);
    }
}
