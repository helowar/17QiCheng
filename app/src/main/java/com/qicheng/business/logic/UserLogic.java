package com.qicheng.business.logic;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.business.protocol.LoginProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;

/**
 * Created by NO1 on 2015/1/18.
 */
public class UserLogic extends BaseLogic {

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create(){
            return new UserLogic();
        }
    }

    private static Logger logger = new Logger("com.qicheng.business.logic.UserLogic");

    /**
     * 通过用户名和密码登录
     * @param uid
     * @param password
     */
    public void login(final String uid, final String password, final EventListener listener) {
        logger.d("Login with UserName and Password:" + uid);
        /**
         *友盟统计
         */
//        Stat.onEvent(StatId.Login);
        //清空缓存
        Cache.getInstance().clear();
        //登录所用的数据
        final User user= new User(uid,password);
        //登录后台交互过程
        final LoginProcess process = new LoginProcess();
        process.setParamUser(user);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode= ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("login process response, " + errCode);

                if(errCode==OperErrorCode.Success){
                    Cache.getInstance().setCacheUser(process.getResultUser());
                }
                //发送事件
                fireStatusEvent(listener, OperErrorCode.Success);

            }
        });

    }

}
