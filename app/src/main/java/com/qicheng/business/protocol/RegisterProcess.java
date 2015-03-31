package com.qicheng.business.protocol;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.common.security.RSACoder;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/4.
 */
public class RegisterProcess extends BaseProcess {

    private static Logger logger = new Logger("com.qicheng.business.protocol.RegisterProcess");

    private String url = "/user/register.html";

    private User mParamUser;


    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            //组装传入服务端参数
            JSONObject o = new JSONObject();
            o.put("cell_num", mParamUser.getCellNum());
            o.put("pwd", mParamUser.getPassWord());
            o.put("verify_code", mParamUser.getVerifyCode());
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
            int value = o.optInt("result_code");
            if(value==0||value==17){
                JSONObject resultBody = o.getJSONObject("body");
                String token = resultBody.optString("token");
                String portraitUrl = resultBody.optString("portrait_url");
                String userImId = resultBody.optString("user_im_id");
                String userId = resultBody.optString("user_id");
                String cellNum = resultBody.optString("cell_num");
                String avatarsToken = resultBody.optString("avatars_token");
                String imagesToken = resultBody.optString("images_token");
                logger.d("Get users token:" + token);
                logger.d("Get user portraitUrl" + portraitUrl);
                User user = Cache.getInstance().getUser();
                user.setPortraitURL(portraitUrl);
                user.setToken(token);
                user.setUserId(userId);
                user.setUserImId(userImId);
                user.setPortraitURL(portraitUrl);
                user.setAvatarsToken(avatarsToken);
                user.setImagesToken(imagesToken);
                //当前使用cellNum登录故如此处理
                user.setUserName(mParamUser.getCellNum());
                user.setCellNum(mParamUser.getCellNum());
                user.setPassWord(mParamUser.getPassWord());
                /**
                 * 刷新缓存
                 */
                Cache.getInstance().refreshCacheUser();
            }
            setProcessStatus(value);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public User getParamUser() {
        return mParamUser;
    }

    public void setParamUser(User paramUser) {
        mParamUser = paramUser;
    }
}
