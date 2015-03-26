/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.easemob.util.HanziToPinyin;
import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO1 on 2015/3/20.
 */
public class GetContactListProcess extends BaseProcess {

    private static final String url = "/chat/get_friend_list.html";

    private static final Logger logger = new Logger("com.qicheng.business.protocol.GetContactListProcess");

    private List<User> contactList = new ArrayList<User>();

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        try {
            //获取状态码
            int value = o.optInt("result_code");
            setProcessStatus(value);
            if(value ==0){
                JSONArray jsonUserInfoList = o.has("body") ? o.optJSONArray("body") : null;
                if(jsonUserInfoList!=null){
                    for(int i=0;i<jsonUserInfoList.length();i++){
                        JSONObject jsonUser = jsonUserInfoList.getJSONObject(i);
                        User user = new User();
                        user.setUserId(jsonUser.optString("user_id"));
                        user.setUserImId(jsonUser.optString("user_im_id"));
                        user.setNickName(jsonUser.optString("nickname"));
                        user.setPortraitURL(jsonUser.optString("portrait_url"));
                        user.setGender(jsonUser.optInt("gender"));
                        user.setSource(jsonUser.optString("source"));
                        String blackList = jsonUser.optString("blacklist");
                        if(blackList.equals("1")){
                            user.setInBlackList(true);
                        }
                        /*设置user header*/
                        setUserHearder(user);
                        contactList.add(user);
                    }
                }
            }else{
                setStatus(ProcessStatus.Status.InfoNoData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    /**
     * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
     *
     * @param user
     */
    protected void setUserHearder(User user) {
        String headerName = null;
        if (!StringUtil.isEmpty(user.getNickName())) {
            headerName = user.getNickName();
        } else {
            headerName = user.getUserName();
        }
        if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
    }

    public List<User> getContactList(){
        return contactList;
    }
}
