package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * TravellerPersonProcess.java是启程APP的查询推荐同路人接口处理类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月9日
 */
public class TravellerRecommendPersonProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.TravellerRecommendPersonProcess");

    /**
     * 获取推荐用户一览接口URL
     */
    private final String url = "/user/rmd_user_list.html";

    /**
     * 查询方向 0：往最新方向查询 1：往最早方向查询
     */
    private byte orderBy;

    /**
     * 最后登录时间
     * 当order_by=0时，该值为在客户端里的最新值；
     * 当order_by=1时，该值为在客户端里的最早值；
     * 如果为空，表示获取最新数据。
     * 格式：yyyyMMddHHmmss；
     */
    private String lastLoginTime;

    /**
     * 查询个数，默认8个
     */
    private int size = 8;

    /**
     * 查询结果：推荐用户信息列表
     */
    private List<User> userList = null;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        // 组装传入服务端参数
        try {
            JSONObject o = new JSONObject();
            o.put("order_by", orderBy);
            o.put("last_login_time", lastLoginTime);
            o.put("size", size);
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入查询推荐用户参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            // 取回的JSON结果
            JSONObject o = new JSONObject(result);
            // 获取状态码
            int resultCode = o.optInt(STATUS_TAG);
            setProcessStatus(resultCode);
            if(resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                // 获取查询结果：用户信息数组
                JSONArray userArray = o.has("body") ? o.optJSONArray("body") : null;
                if (userArray != null && userArray.length() > 0) {
                    int length = userArray.length();
                    userList = new ArrayList<User>(length);
                    for (int i = 0; i < length; i++) {
                        o = userArray.getJSONObject(i);
                        User user = new User();
                        user.setUserId(o.optString("user_id"));
                        user.setPortraitURL(o.optString("portrait_url"));
                        user.setLastLoginTime(o.optString("last_login_time"));
                        userList.add(user);
                    }
                }
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理查询推荐用户响应结果时异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setInfoParameter(byte orderBy, String lastLoginTime, int size) {
        this.orderBy = orderBy;
        this.lastLoginTime = lastLoginTime;
        this.size = size;
    }

    public List<User> getUserList() {
        return userList;
    }
}
