package com.qicheng.business.protocol;

import com.qicheng.framework.event.OperErrorCode;

public class ProcessStatus{
    public static enum Status {

        Success,

        /** email不存在 */
        ErrUid,

        /** 密码错误 */
        ErrPass,

        /** email已经存在 */
        ErrUidExist,

        /** email不合法 */
        ErrUidInvalid,

        /** 要删除的图片是头像，不能删除 */
        ErrPhotoIsPortrait,

        ErrNetDisable,

        ErrException,
        ErrUnkown,
    }

    public static OperErrorCode convertFromStatus(Status status) {
        switch(status) {
            case Success:
                return OperErrorCode.Success;
            case ErrUid:
                return OperErrorCode.UidNoExist;
            case ErrPass:
                return OperErrorCode.PasswordError;
            case ErrPhotoIsPortrait:
                return OperErrorCode.PhotoIsPortait;
            case ErrUidExist:
                return OperErrorCode.UidExist;
            case ErrUidInvalid:
                return OperErrorCode.UidInvalid;
            case ErrNetDisable:
                return OperErrorCode.NetNotAviable;
            default:
                return OperErrorCode.Unknown;
        }
    }
 }

