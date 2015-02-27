package com.qicheng.business.persistor;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;

import com.qicheng.business.module.User;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

class UserPreferences {
	
	public void set(User user) {
		SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("lastuid", user.getUserId());
        editor.putString("lastpass", user.getPassWord());
        editor.putString("lastuname",user.getUserName());
        editor.putString("lastcellnum",user.getCellNum());
        editor.putString("lasttoken",user.getToken());
        editor.putString("lastportraiturl",user.getPortraitURL());
        editor.putString("lastbirthday",user.getBirthday());
        editor.putInt("lastgender",user.getGender());
        editor.putString("lastnickname",user.getNickName());
        editor.putString("lastLongitude", user.getLocation().getLongitude());
        editor.putString("lastLatitude", user.getLocation().getLatitude());
        editor.putFloat("lastDirection", user.getLocation().getDirection());
		editor.commit();
	}

    public void setPublicKey(String publicKey){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString("publicKey", publicKey).apply();
    }

    public String getPublicKey(){
        SharedPreferences sp = getPreferences();
        return  sp.getString("publicKey","");
    }

	public User getLastest() {
		SharedPreferences sp = getPreferences();
        User user = new User();
		user.setUserId( sp.getString("lastuid", ""));
		user.setPassWord(sp.getString("lastpass", ""));
        user.setUserName(sp.getString("lastuname",""));
        user.setCellNum(sp.getString("lastcellnum",""));
        user.setToken(sp.getString("lasttoken",""));
        user.setBirthday(sp.getString("lastbirthday",""));
        user.setPortraitURL(sp.getString("lastportraiturl",""));
        user.setGender(sp.getInt("lastgender",1));
        user.setNickName(sp.getString("lastnickname",""));
        user.getLocation().setLongitude(sp.getString("lastLongitude", null));
        user.getLocation().setLatitude(sp.getString("lastLatitude", null));
        user.getLocation().setDirection(sp.getFloat("lastDirection", 0));
		return user;
	}
	
	public void clearLastestPass() {
		getPreferences().edit().putString("lastpass", "").commit();
	}
	
	private SharedPreferences getPreferences() {
		return Const.Application.getSharedPreferences(Const.SharedPreferenceKey.UserPreference, Context.MODE_PRIVATE);
	}
}



