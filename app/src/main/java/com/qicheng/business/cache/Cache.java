package com.qicheng.business.cache;

import com.qicheng.business.module.City;
import com.qicheng.business.module.Train;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.business.protocol.GetTrainListProcess;

import java.util.List;
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

    private TripRelatedCache mTripRelatedCache = new TripRelatedCache();

    private UserCache mUserCache = new UserCache();

    public User getUser() {
        return mUser;
    }

    public void clear() {
    }

    public void onCreate() {
        /*用户对象缓存*/
        mUserCache.onCreate();
        mUser = mUserCache.getCashedUser();
        /*公钥缓存*/
        publicKey = PersistorManager.getInstance().getPublicKey();
        /*行程相关数据缓存*/
        mTripRelatedCache.onCreate();

        GetTrainListProcess process = new GetTrainListProcess();
        process.run();

    }

    public void refreshCacheUser() {
        mUserCache.setCache(mUser);
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

    public List<City> getTripRelatedCityCache() {
        return mTripRelatedCache.getCitys();
    }

    public List<Train> getTripRelatedTrainCache(){
        return mTripRelatedCache.getTrains();
    }

    public List<TrainStation> getTripRelatedStationCache(String cityCode) {
        return mTripRelatedCache.getStations(cityCode);
    }

    public void refreshTripRelatedCityCache(String c){
        mTripRelatedCache.setCityCache(c);
    }

    public void refreshTripRelatedTrainCache(String t){
        mTripRelatedCache.setTrainCache(t);
    }

    public void refreshTripRelatedStationCache(String cityCode, String stations) {
        mTripRelatedCache.setStationCache(cityCode, stations);
    }
}
