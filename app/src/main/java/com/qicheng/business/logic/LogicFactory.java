package com.qicheng.business.logic;

import com.qicheng.framework.logic.BaseLogic;

import java.util.HashMap;
import java.util.Map;

public class LogicFactory {

	private static final LogicFactory ins = new LogicFactory();

	public static final LogicFactory self() {
		return ins;
	}
	
	public static enum Type {
		User,
        TravellerPerson,
        Label,
        Trip,
        Location,
        Dyn,
	}
	
	private Map<Type, BaseLogic.Factory> mFactorys = new HashMap<Type, BaseLogic.Factory>();
	private Map<Type, BaseLogic> mLogics = new HashMap<Type, BaseLogic>();
	
	private LogicFactory() {
//		mFactorys.put(Type.AppointInfo, new AppointInfoLogic.Factory());
//		mFactorys.put(Type.Appoint, new AppointLogic.Factory());
//		mFactorys.put(Type.Chat, new ChatLogic.Factory());
//		mFactorys.put(Type.Ouser, new OuserLogic.Factory());
//		mFactorys.put(Type.Photo, new PhotoLogic.Factory());
//		mFactorys.put(Type.Profile, new ProfileLogic.Factory());
//		mFactorys.put(Type.Radar, new RadarLogic.Factory());
//		mFactorys.put(Type.Shake, new ShakeLogic.Factory());
//		mFactorys.put(Type.Timeline, new TimelineLogic.Factory());
//		mFactorys.put(Type.Upgrade, new UpgradeLogic.Factory());
		mFactorys.put(Type.User, new UserLogic.Factory());
        mFactorys.put(Type.Label, new LabelLogic.Factory());
//		mFactorys.put(Type.Share, new ShareLogic.Factory());
//		mFactorys.put(Type.Emotion, new EmotionLogic.Factory());
        mFactorys.put(Type.TravellerPerson, new TravellerPersonLogic.Factory());
        mFactorys.put(Type.Trip,new TripLogic.Factory());
        mFactorys.put(Type.Location,new LocationLogic.Factory());
        mFactorys.put(Type.Dyn,new DynLogic.Factory());
	}
	
	public BaseLogic get(Type type) {
		if(!mLogics.containsKey(type)) {
			mLogics.put(type, mFactorys.get(type).create());
		}
		return mLogics.get(type);
	}


}
