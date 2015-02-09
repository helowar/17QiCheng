package com.qicheng.business.persistor;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;

import com.qicheng.business.module.User;
import com.qicheng.util.Const;

class UserPreferences {
	
	public void set(User user) {
		SharedPreferences.Editor editor = getPreferences().edit();
		editor.putString("lastuid", user.getUserId());
		editor.putString("lastpass", user.getPassWord());
        editor.putString("lastuname",user.getUserName());
        editor.putString("lastcellnum",user.getCellNum());
        editor.putString("lasttoken",user.getToken());
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
		return user;
	}
	
	public void clearLastestPass() {
		getPreferences().edit().putString("lastpass", "").commit();
	}
	
	private SharedPreferences getPreferences() {
		return Const.Application.getSharedPreferences("user", Context.MODE_PRIVATE);
	}
}



