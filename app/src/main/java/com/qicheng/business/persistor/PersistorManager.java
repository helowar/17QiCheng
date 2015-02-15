package com.qicheng.business.persistor;

import com.qicheng.business.module.User;

import java.util.Set;

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

    public void savePublicKey(String publicKey) {
        new UserPreferences().setPublicKey(publicKey);
    }

    public String getPublicKey() {
        return new UserPreferences().getPublicKey();
    }

    public User getLastestUser() {
        return new UserPreferences().getLastest();
    }

    public void setUser(User user) {
        new UserPreferences().set(user);
    }

    public void setTrainList(Set<String> trains){new TrainPreferences().set(trains);}

    public Set<String> getTrainList(){return new TrainPreferences().get();}
}
