package com.qicheng.business.logic.event;

import com.qicheng.business.module.LabelType;
import com.qicheng.business.module.User;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO1 on 2015/2/6.
 */
public class UserEventArgs extends StatusEventArgs {

    private User result = null;

    /**
     * 用户信息列表
     */
    private List<User> userList = null;
    private ArrayList<LabelType> resultLabelTypes = null;

    public UserEventArgs(User user, OperErrorCode errCode) {
        super(errCode);
        result = user;
    }

    public UserEventArgs(List<User> userList, OperErrorCode errCode) {
        super(errCode);
        this.userList = userList;
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

    public ArrayList<LabelType> getResultLabelTypes() {
        return resultLabelTypes;
    }

    public void setResultLabelTypes(ArrayList<LabelType> resultLabelTypes) {
        this.resultLabelTypes = resultLabelTypes;
    }

    public void setResult(User result) {
        this.result = result;
    }

    public List<User> getUserList() {
        return userList;
    }
}
