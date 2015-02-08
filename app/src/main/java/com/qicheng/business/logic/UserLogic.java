package com.qicheng.business.logic;

import android.graphics.Bitmap;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.persistor.PersistorManager;
import com.qicheng.business.protocol.GetPublicKeyProcess;
import com.qicheng.business.protocol.ImageUploadProcess;
import com.qicheng.business.protocol.LoginProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.protocol.RegisterProcess;
import com.qicheng.business.protocol.VerifyCodeProcess;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.FileImageUpload;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;
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
        final User user = new User(uid, password);
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
                    Cache.getInstance().setCacheUser(process.getResultUser());
                }
                //发送事件
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
    public void getVerifyCode(final String cellNum, final EventListener listener) {
        logger.d("Get Verify Code with CellNum:" + cellNum);
        final User user = new User();
        user.setCellNum(cellNum);
        //获取验证码过程
        final VerifyCodeProcess process = new VerifyCodeProcess();
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
        return PersistorManager.getInstance().getPublicKey();
    }

    /**
     * 保存用户头像文件
     * @param photo
     * @param listener
     */
    public void saveUserPortrait(Bitmap photo,final EventListener listener){

        final File myCaptureFile = new File( Const.WorkDir+UUID.randomUUID().toString() + ".jpg");
        OperErrorCode errCode = null;
        try{
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            photo.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }
        catch (FileNotFoundException e){
            errCode = OperErrorCode.FileUpLoadFailed;
            fireStatusEvent(listener, errCode);
        }
        catch(IOException e){
            errCode = OperErrorCode.FileUpLoadFailed;
            fireStatusEvent(listener, errCode);
        }
        final ImageUploadProcess process = new ImageUploadProcess();
        process.run(null,ImageUploadProcess.USAGE_PORTRAIT,myCaptureFile,new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode= ProcessStatus.convertFromStatus(process.getStatus());
                User resultUser = new User();

                UserEventArgs userEventArgs = new UserEventArgs(resultUser,errCode);
                if(errCode==OperErrorCode.Success){
                   resultUser.setPortraitURL(process.getResultUrl());
                }
                fireEvent(listener, userEventArgs);
            }
        });
    }



}
