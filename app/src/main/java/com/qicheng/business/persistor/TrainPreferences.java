package com.qicheng.business.persistor;

import android.content.Context;
import android.content.SharedPreferences;

import com.qicheng.util.Const;

import java.util.Set;

/**
 * Created by NO1 on 2015/2/12.
 */
public class TrainPreferences {

    public void set(Set<String> trainSet) {
        getPreferences().edit().putStringSet(Const.SharedPreferenceKey.TrainPreference,trainSet);
    }

    public Set<String> get(){
        return getPreferences().getStringSet(Const.SharedPreferenceKey.TrainPreference,null);
    }

    private SharedPreferences getPreferences() {
        return Const.Application.getSharedPreferences(Const.SharedPreferenceKey.TrainPreference, Context.MODE_PRIVATE);
    }
}
