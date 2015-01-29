package com.qicheng.framework.event;

/**
 * 有状态事件参数
 * Created by Mark on 2015/1/9.
 */
public class StatusEventArgs extends EventArgs{

    private OperErrorCode mErrCode = OperErrorCode.None;

    public StatusEventArgs(OperErrorCode errCode) {
        mErrCode = errCode;
    }
    public OperErrorCode getErrCode() {
        return mErrCode;
    }
}
