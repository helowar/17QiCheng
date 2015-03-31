package com.qicheng.business.logic;

import android.graphics.Bitmap;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.event.UserDetailEventArgs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.logic.event.UserPhotoEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.protocol.AddViewUserProcess;
import com.qicheng.business.protocol.GetPublicKeyProcess;
import com.qicheng.business.protocol.GetUserDetailProcess;
import com.qicheng.business.protocol.GetUserPhotoListProcess;
import com.qicheng.business.protocol.ImageUploadProcess;
import com.qicheng.business.protocol.LoginProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.protocol.RegisterProcess;
import com.qicheng.business.protocol.SetUserInfoProcess;
import com.qicheng.business.protocol.UpdateCellNumProcess;
import com.qicheng.business.protocol.UpdatePasswordProcess;
import com.qicheng.business.protocol.UpdateUserInformationProcess;
import com.qicheng.business.protocol.VerifyCodeProcess;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.UIEventListener;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by NO1 on 2015/1/18.
 */
public class UserLogic extends BaseLogic {

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new UserLogic();
        }
    }


    private static Logger logger = new Logger("com.qicheng.business.logic.UserLogic");

    /**
     * 通过用户名和密码登录
     *
     * @param uName userName含义不固定，具体内容由服务端判断，客户端仅保持此userName以保证可登录
     * @param password
     */
    public void login(final String uName, final String password, final EventListener listener) {
        logger.d("Login with UserName and Password:" + uName);
        /**
         *友盟统计
         */
//        Stat.onEvent(StatId.Login);
        //清空缓存
        Cache.getInstance().clear();
        //登录所用的数据
        final User user = new User(uName, password);
        user.getUserName();
        User cachedUser = Cache.getInstance().getUser();
        cachedUser.setUserName(uName);
        cachedUser.setPassWord(password);
        //登录后台交互过程
        final LoginProcess process = new LoginProcess();
        process.setParamUser(user);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("login process response, " + errCode);
                UserEventArgs userEventArgs = new UserEventArgs(process.getResultUser(), errCode);
                if (errCode == OperErrorCode.Success) {
                    callbackForCacheLogin(process.getResultUser());
//                    user.setUserName(process.getResultUser().getUserName());userName不覆盖，保证自动登录可用
                    loginHX(Cache.getInstance().getUser().getUserImId(), StringUtil.MD5(Cache.getInstance().getUser().getPassWord()));
                    Cache.getInstance().refreshCacheUser();
                }
                //发送事件
                fireEvent(listener, userEventArgs);

            }
        });

    }

    public void loginHX(final String userImId, String password) {
        EMChatManager.getInstance().login(userImId, password, new EMCallBack() {

            @Override
            public void onSuccess() {
                logger.d(userImId + " login huanxin success!");
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
                logger.d(userImId + " login huanxin error,errorCode=" + code + "message:" + message);
            }
        });
    }

    public void loginWithCache(final EventListener listener) {
        User cachedUser = Cache.getInstance().getUser();
        if (cachedUser == null || StringUtil.isEmpty(Cache.getInstance().getPublicKey())) {
            OperErrorCode errorCode = OperErrorCode.NotLogin;
            fireStatusEvent(listener, errorCode);
            return;
        }
        if (StringUtil.isEmpty(cachedUser.getUserId()) || StringUtil.isEmpty(cachedUser.getPassWord())) {
            OperErrorCode errorCode = OperErrorCode.NotLogin;
            fireStatusEvent(listener, errorCode);
            return;
        }
        //登录后台交互过程
        final LoginProcess process = getProcessForCacheLogin();
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("login process response, " + errCode);
                UserEventArgs userEventArgs = new UserEventArgs(process.getResultUser(), errCode);
                if (errCode == OperErrorCode.Success) {
                    callbackForCacheLogin(process.getResultUser());
                    loginHX(Cache.getInstance().getUser().getUserImId(), StringUtil.MD5(Cache.getInstance().getUser().getPassWord()));
                }
                //发送事件
                fireEvent(listener, userEventArgs);

            }
        });
    }

    private LoginProcess getProcessForCacheLogin() {
        User cachedUser = Cache.getInstance().getUser();
        //登录所用的数据
        User user = new User(cachedUser.getUserId(), cachedUser.getPassWord());
        //登录后台交互过程
        LoginProcess process = new LoginProcess();
        process.setParamUser(user);
        return process;
    }

    private void callbackForCacheLogin(User resultUser) {
        User user = Cache.getInstance().getUser();
        user.setToken(resultUser.getToken());
        user.setNickName(resultUser.getNickName());
        user.setPortraitURL(resultUser.getPortraitURL());
        user.setUserImId(resultUser.getUserImId());
        user.setBirthday(resultUser.getBirthday());
        user.setGender(resultUser.getGender());
        user.setUserId(resultUser.getUserId());
        user.setCellNum(resultUser.getCellNum());
        user.setAvatarsToken(resultUser.getAvatarsToken());
        user.setImagesToken(resultUser.getImagesToken());
        Cache.getInstance().refreshCacheUser();
    }

    /**
     * 在注册时初次设置用户基本信息
     */
    public void initUserInfo(final User param, final UIEventListener listener) {
        final SetUserInfoProcess process = new SetUserInfoProcess();
        process.setParamUser(param);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                UserEventArgs userEventArgs = new UserEventArgs(errCode);
                if (errCode == OperErrorCode.Success) {
                    userEventArgs.setResultLabelTypes(process.getLabelTypes());
                }
                fireEvent(listener, userEventArgs);
            }
        });


    }


    /**
     * 获取验证码
     *
     * @param cellNum
     * @param listener
     */
    public void getVerifyCode(final String cellNum, int actionType, final EventListener listener) {
        logger.d("Get Verify Code with CellNum:" + cellNum);
        final User user = new User();
        user.setCellNum(cellNum);
        //获取验证码过程
        final VerifyCodeProcess process = new VerifyCodeProcess();
        process.setActionType(actionType);
        process.setParamUser(user);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("login process response, " + errCode);
                //发送事件
                fireStatusEvent(listener, errCode);

            }
        });
    }

    public void userRegister(final String cellNum, final String password, final String verifyCode, final EventListener listener) {
        logger.d("Register with cellnum:" + cellNum + "--with password:" + password + "--with verifyCode:" + verifyCode);
        /**
         *友盟统计
         */
//        Stat.onEvent(StatId.Login);
        final User user = new User();
        user.setCellNum(cellNum);
        user.setVerifyCode(verifyCode);
        user.setPassWord(password);
        final RegisterProcess process = new RegisterProcess();
        process.setParamUser(user);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("Register process response, " + errCode);
                //发送事件
                fireStatusEvent(listener, errCode);
            }
        });
    }


    /**
     * 获取并持久化公钥
     */
    public void fetchPublicKey(final EventListener listener) {
        final GetPublicKeyProcess process = new GetPublicKeyProcess();
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                if (errCode != OperErrorCode.Success) {
                    /**
                     *获取公钥出错
                     */
                    fireStatusEvent(listener, errCode);

                }
            }
        });
    }

    /**
     * 取出公钥
     *
     * @return
     */
    public String getPublicKey() {
        return Cache.getInstance().getPublicKey();
    }

    /**
     * 保存用户头像文件
     *
     * @param photo
     * @param listener
     */
    public void saveUserPortrait(Bitmap photo, final EventListener listener) {
        File dir = new File(Const.WorkDir);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        final File myCaptureFile = new File(dir + UUID.randomUUID().toString() + ".jpg");
        OperErrorCode errCode = null;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            errCode = OperErrorCode.FileUpLoadFailed;
            fireStatusEvent(listener, errCode);
        } catch (IOException e) {
            errCode = OperErrorCode.FileUpLoadFailed;
            fireStatusEvent(listener, errCode);
        }
        final ImageUploadProcess process = new ImageUploadProcess();
        process.run(null, ImageUploadProcess.USAGE_PORTRAIT, myCaptureFile, new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                User resultUser = new User();

                UserEventArgs userEventArgs = new UserEventArgs(resultUser, errCode);
                if (errCode == OperErrorCode.Success) {
                    resultUser.setPortraitURL(process.getResultUrl());
                }
                fireEvent(listener, userEventArgs);
            }
        });
    }

    /**
     * 更新用户信息接口
     *
     * @param updateType
     * @param updateValue
     */
    public void updateUserInformation(int updateType, String updateValue, final EventListener listener) {
        final UpdateUserInformationProcess process = new UpdateUserInformationProcess();
        process.setUpdateType(updateType);
        process.setUpdateValue(updateValue);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                UserEventArgs userEventArgs = new UserEventArgs(errCode);
                fireEvent(listener, userEventArgs);
            }
        });
    }

    /**
     * 获取用户详细信息。
     *
     * @param id       ID
     * @param type     ID类型 0：用户ID 1：用户IM账户ID
     * @param listener 查询结果事件监听器
     */
    public void getUserDetail(String id, byte type, final EventListener listener) {
        final GetUserDetailProcess process = new GetUserDetailProcess();
        process.setId(id);
        process.setType(type);
        process.run("getUserDetail", new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("获取用户详细信息结果码为：" + errCode);
                UserDetailEventArgs userEventArgs = new UserDetailEventArgs(process.getUserDetail(), errCode);
                // 发送事件
                fireEvent(listener, userEventArgs);
            }
        });
    }

    /**
     * 获取用户照片一览信息。
     *
     * @param userId   用户ID
     * @param orderBy  查询方向 0：往最新方向查询 1：往最早方向查询
     * @param orderNum 照片的序号
     * @param size     查询个数
     * @param listener 查询结果事件监听器
     */
    public void getUserPhotoList(String userId, byte orderBy, long orderNum, int size, final EventListener listener) {
        final GetUserPhotoListProcess process = new GetUserPhotoListProcess();
        process.setInfoParameter(userId, orderBy, orderNum, size);
        process.run("getUserPhotoList", new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("获取用户照片一览信息结果码为：" + errCode);
                UserPhotoEventArgs userEventArgs = new UserPhotoEventArgs(process.getPhotoList(), errCode);
                // 发送事件
                fireEvent(listener, userEventArgs);
            }
        });
    }

    /**
     * 添加浏览用户记录。
     *
     * @param userId 用户ID
     */
    public void addViewUser(String userId) {
        final AddViewUserProcess process = new AddViewUserProcess();
        process.setUserId(userId);
        process.run();
    }

    /**
     * 修改用户密码
     *
     * @param cellNum
     * @param newPwd
     * @param verifyCode
     * @param listener
     */
    public void updatePassword(String cellNum, String newPwd, String verifyCode, final EventListener listener) {
        final UpdatePasswordProcess process = new UpdatePasswordProcess();
        process.setCellNum(cellNum);
        process.setNewPwd(newPwd);
        process.setVerifyCode(verifyCode);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("获取修改密码结果码为：" + errCode);
                UserEventArgs userEventArgs = new UserEventArgs(errCode);
                // 发送事件
                fireEvent(listener, userEventArgs);
            }
        });
    }

    /**
     * 修改手机号码
     *
     * @param cellNum
     * @param verifyCode
     * @param listener
     */
    public void updateCellNum(String cellNum, String verifyCode, final EventListener listener) {
        final UpdateCellNumProcess process = new UpdateCellNumProcess();
        process.setCellNum(cellNum);
        process.setVerifyCode(verifyCode);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("获取修改密码结果码为：" + errCode);
                UserEventArgs userEventArgs = new UserEventArgs(errCode);
                // 发送事件
                fireEvent(listener, userEventArgs);
            }
        });
    }
}
