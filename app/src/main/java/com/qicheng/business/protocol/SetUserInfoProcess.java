package com.qicheng.business.protocol;

import com.google.gson.Gson;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.LabelType;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO1 on 2015/2/9.
 */
public class SetUserInfoProcess extends BaseProcess{

    private static Logger logger = new Logger("com.qicheng.business.protocol.SetUserInfoProcess");

    private String url = "/user/add.html";

    private User mParamUser;

    private ArrayList<LabelType> mLabelTypes = new ArrayList<LabelType>();

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("nickname", mParamUser.getNickName());
            o.put("birthday", mParamUser.getBirthday());
            o.put("gender", mParamUser.getGender());
            o.put("portrait_url",mParamUser.getPortraitURL());
            return o.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        try {
            //获取状态码
            int value = o.optInt("result_code");
            setProcessStatus(value);
            if(getStatus()== ProcessStatus.Status.Success){
                JSONObject userInfo = o.optJSONObject("body");
                String nickName = userInfo.optString("nickname");
                int gender = userInfo.optInt("gender");
                String portraitURL = userInfo.optString("portrait_url");
                String birthday = userInfo.optString("birthday");
                User cachedUser = Cache.getInstance().getUser();
                cachedUser.setNickName(nickName);
                cachedUser.setGender(gender);
                cachedUser.setPortraitURL(portraitURL);
                cachedUser.setBirthday(birthday);
                //刷新User缓存对象
                Cache.getInstance().refreshCacheUser();
                //获取标签列表
                JSONArray arry = (JSONArray) userInfo.opt("tag_types");
                Gson gson = new Gson();
                for (int i = 0; i < arry.length(); i++) {
                    Object type = arry.get(i);
                    LabelType labelType = gson.fromJson(type.toString(), LabelType.class);
                    mLabelTypes.add(labelType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }

    }

    public void setParamUser(User paramUser) {
        mParamUser = paramUser;
    }

    public ArrayList<LabelType> getLabelTypes() {
        return mLabelTypes;
    }

    @Override
    protected String getFakeResult() {
        return null;
    }
}
