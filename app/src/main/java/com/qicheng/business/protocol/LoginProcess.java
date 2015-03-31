package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/1/19.
 */
public class LoginProcess extends BaseProcess {

    //服务端请求地址
    private final String URL = "/user/login.html";

    //参数对象
    private User paramUser;

    public User getResultUser() {
        return resultUser;
    }

    //结果对象
    private User resultUser;

    @Override
    protected String getFakeResult() {
        String fakeResult = "{status:0,userName:" + paramUser.getUserName() + ",passWord:" + paramUser.getPassWord() + "}";
        return fakeResult;
    }

    @Override
    public String getRequestUrl() {
        return URL;
    }

    public void setParamUser(User param) {
        paramUser = param;
    }

    @Override
    public String getInfoParameter() {

        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("user_name", paramUser.getUserName());
            o.put("pwd", paramUser.getPassWord());
            return RSACoder.getInstance().encryptByPublicKey(o.toString(), Cache.getInstance().getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(JSONObject o) {
        try {
            //获取状态码
            int value = o.optInt(JSONUtil.STATUS_TAG);
            if(value==0){
                /**
                 * 取出返回值
                 */
                JSONObject body = JSONUtil.getResultBody(o);
                String token = body.optString("token");
                String nickname = body.optString("nickname");
                String url = body.optString("portrait_url");
                String imId = body.optString("user_im_id");
                String gender = body.optString("gender");
                String birthday = body.optString("birthday");
                String userName = body.optString("user_name");
                String userId = body.optString("user_id");
                String cellNum = body.optString("cell_num");
                String avatarsToken = body.optString("avatars_token");
                String imagesToken = body.optString("images_token");
                /**
                 * 组装返回对象
                 */
                resultUser = new User();
                resultUser.setToken(token);
                resultUser.setNickName(nickname);
                resultUser.setPortraitURL(url);
                resultUser.setUserImId(imId);
                resultUser.setGender(Integer.parseInt(gender));
                resultUser.setBirthday(birthday);
                resultUser.setUserId(userId);
                resultUser.setUserName(userName);
                resultUser.setCellNum(cellNum);
                resultUser.setImagesToken(imagesToken);
                resultUser.setAvatarsToken(avatarsToken);
            }
            setProcessStatus(value);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }
    }

    public User getUserFromResult(JSONObject o){
        /**
         * 取出返回值
         */
        JSONObject body = JSONUtil.getResultBody(o);
        String token = body.optString("token");
        String nickname = body.optString("nickname");
        String url = body.optString("portrait_url");
        String imId = body.optString("user_im_id");
        String gender = body.optString("gender");
        String birthday = body.optString("birthday");
        String userName = body.optString("user_name");
        String userId = body.optString("user_id");
        String cellNum = body.optString("cell_num");
        String avatarsToken = body.optString("avatars_token");
        String imagesToken = body.optString("images_token");
        /**
         * 组装返回对象
         */
        User resultUser = new User();
        resultUser.setToken(token);
        resultUser.setNickName(nickname);
        resultUser.setPortraitURL(url);
        resultUser.setUserImId(imId);
        resultUser.setGender(Integer.parseInt(gender));
        resultUser.setBirthday(birthday);
        resultUser.setUserId(userId);
        resultUser.setUserName(userName);
        resultUser.setCellNum(cellNum);
        resultUser.setImagesToken(imagesToken);
        resultUser.setAvatarsToken(avatarsToken);
        return resultUser;
    }

}
