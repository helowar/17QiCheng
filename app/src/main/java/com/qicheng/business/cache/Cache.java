package com.qicheng.business.cache;

import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.business.protocol.GetTrainListProcess;

import java.util.Set;

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

        GetTrainListProcess process = new GetTrainListProcess();
        process.run();

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

    public void setTrainList(Set<String> trains){
        PersistorManager.getInstance().setTrainList(trains);
    }

    public Set<String> getTrainList(){
        return PersistorManager.getInstance().getTrainList();
    }
}
