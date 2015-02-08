package com.qicheng.business.logic.event;

import com.qicheng.business.module.User;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

/**
 * Created by NO1 on 2015/2/6.
 */
public class UserEventArgs extends StatusEventArgs {

    private User result = null;


    public UserEventArgs(User user, OperErrorCode errCode) {
        super(errCode);
        result = user;
    }

    /**
     * 错误返回
     *
     * @param errCode
     */
    public UserEventArgs(OperErrorCode errCode) {
        super(errCode);
    }

    /**
     * 正确返回
     *
     * @param user
     */
    public UserEventArgs(User user) {
        super(OperErrorCode.Success);
        result = user;
    }

    public User getResult() {
        return result;
    }

    @Override
    public OperErrorCode getErrCode() {
        return super.getErrCode();
    }
}
