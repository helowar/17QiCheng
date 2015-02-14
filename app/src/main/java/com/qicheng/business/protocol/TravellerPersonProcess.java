package com.qicheng.business.protocol;

import com.qicheng.business.module.User;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * TravellerPersonProcess.java是启程APP的查询同路人接口处理类
 *
 * @author 花树峰
 * @version 1.0 2015年2月5日
 */
public class TravellerPersonProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.TravellerPersonProcess");

    /**
     * 获取普通用户一览接口URL
     */
    private final String url = "/user/user_list.html";

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
     * 查询类型 0：车站 1：出发 2：到达 3：车次 4：未上车 5：上车 6：下车
     */
    private byte queryType;

    /**
     * 查询值
     * 当query_type=0、1或2时，该值为车站代码；
     * 当query_type=3、4、5或6时，该值为车次。
     */
    private String queryValue;

    /**
     * 查询结果：用户信息列表
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
            o.put("query_type", queryType);
            o.put("query_value", queryValue);
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入查询普通用户参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            // 取回的JSON结果
            JSONObject o = new JSONObject(result);
            // 获取状态码
            int resultCode = o.optInt(JSONUtil.STATUS_TAG);
            switch (resultCode) {
                case Const.ResponseResultCode.RESULT_SUCCESS:
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
                    setStatus(ProcessStatus.Status.Success);
                    break;
                case Const.ResponseResultCode.RESULT_ILLEGAL_CALL:
                    setStatus(ProcessStatus.Status.IllegalRequest);
                    break;
                case Const.ResponseResultCode.RESULT_EXCEPTION:
                    setStatus(ProcessStatus.Status.ErrException);
                    break;
                default:
                    setStatus(ProcessStatus.Status.ErrUnkown);
                    break;
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理查询普通用户响应结果时异常");
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setInfoParameter(byte queryType, String queryValue, byte orderBy, String lastLoginTime, int size) {
        this.queryType = queryType;
        this.queryValue = queryValue;
        this.orderBy = orderBy;
        this.lastLoginTime = lastLoginTime;
        this.size = size;
    }

    public List<User> getUserList() {
        return userList;
    }
}
