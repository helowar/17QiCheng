package com.qicheng.framework.event;

public enum OperErrorCode {

    /**
     * 成功
     */
    Success,

    /**
     * 网络不可用
     */
    NetNotAviable,

    /**
     * 定位服务不可用
     */
    LocationNotAviable,

    /**
     * 用户名不存在
     */
    UidNoExist,

    /**
     * 用户名已存在
     */
    UidExist,

    /**
     * 用户名无效
     */
    UidInvalid,

    /**
     * 密码错误
     */
    PasswordError,

    /**
     * 要删除的照片为头像
     */
    PhotoIsPortait,

    /**
     * 他在我的黑名单中
     */
    InBlack,

    /**
     * 不需要升级
     */
    NoNeedUpgrade,

    /**
     * 未知错误
     */
    Unknown,

    /**
     * 初始化状态
     */
    None,

    /**
     * 非法请求
     */
    IllegalRequest,

    /**
     * 验证码过期
     */
    VerifyCodeExpire,

    VerifyCodeWrong,

    CellNumExist,

    FileUpLoadFailed,

    /**
     * 未找到数据
     */
    NoDataFound,

    NotLogin,

}
