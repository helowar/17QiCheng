package com.qicheng.business.logic;

import android.graphics.Bitmap;

import com.qicheng.business.logic.event.DynEventAargs;
import com.qicheng.business.logic.event.StationEventAargs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.protocol.AddDynProcess;
import com.qicheng.business.protocol.DeleteDynProcess;
import com.qicheng.business.protocol.GetDynListProcess;
import com.qicheng.business.protocol.GetStationListProcess;
import com.qicheng.business.protocol.InteractProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.protocol.QiniuImageUploadProcess;
import com.qicheng.business.ui.DynPublishActivity;
import com.qicheng.business.ui.component.DynSearch;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.util.Const;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by NO3 on 2015/2/28.
 */
public class DynLogic extends BaseLogic {

    /*工厂方法创建DynLogic*/
    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new DynLogic();
        }
    }

    /**
     * 获取动态列表的Logic方法
     *
     * @param dynSearch
     * @param listener
     */
    public void getDynList(DynSearch dynSearch, final EventListener listener) {
        final GetDynListProcess process = new GetDynListProcess();
        process.setDynSearch(dynSearch);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                DynEventAargs dynEventAargs = new DynEventAargs(process.getDynList(), errCode);
                fireEvent(listener, dynEventAargs);
            }
        });

    }

    /**
     * 动态互动逻辑方法，包含点赞，取消赞，分享
     *
     * @param id
     * @param action
     * @param listener
     */
    public void interact(String id, byte action, final EventListener listener) {
        final InteractProcess process = new InteractProcess();
        process.setId(id);
        process.setAction(action);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                DynEventAargs dynEventAargs = new DynEventAargs(errCode);
                fireEvent(listener, dynEventAargs);
            }
        });
    }

    /**
     * 通过城市代码获取车站的列表
     *
     * @param cityCode
     * @param listener
     */
    public void getStationList(String cityCode, final EventListener listener) {
        final GetStationListProcess process = new GetStationListProcess();
        process.setCityCode(cityCode);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                StationEventAargs stationEventAargs = new StationEventAargs(process.getStationList(), errCode);
                fireEvent(listener, stationEventAargs);
            }
        });
    }

    /**
     * 删除动态的逻辑Logic方法
     *
     * @param dynBody
     * @param listener
     */
    public void addDyn(DynPublishActivity.DynBody dynBody, final EventListener listener) {
        final AddDynProcess process = new AddDynProcess();
        process.setDynBody(dynBody);

        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                DynEventAargs dynEventAargs = new DynEventAargs(errCode);
                fireEvent(listener, dynEventAargs);
            }
        });
    }

    /**
     * 删除动态的逻辑Logic方法
     *
     * @param activityId
     * @param listener
     */
    public void deleteDyn(String activityId, final EventListener listener) {
        final DeleteDynProcess process = new DeleteDynProcess();
        process.setActivityId(activityId);

        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                DynEventAargs dynEventAargs = new DynEventAargs(errCode);
                fireEvent(listener, dynEventAargs);
            }
        });
    }


    /**
     * 保存动态图片文件
     *
     * @param photo
     */
    public void saveDynPicture(Bitmap photo, final EventListener listener) {
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
        } catch (IOException e) {
            errCode = OperErrorCode.FileUpLoadFailed;
        }
//        final ImageUploadProcess process = new ImageUploadProcess();
//        process.run(null, ImageUploadProcess.USAGE_COMMON, myCaptureFile, new ResponseListener() {
//            @Override
//            public void onResponse(String requestId) {
//                // 状态转换：从调用结果状态转为操作结果状态
//                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
//                User resultUser = new User();
//
//                UserEventArgs userEventArgs = new UserEventArgs(resultUser, errCode);
//                if (errCode == OperErrorCode.Success) {
//                    resultUser.setPortraitURL(process.getResultUrl());
//                }
//                fireEvent(listener, userEventArgs);
//            }
//        });
        final QiniuImageUploadProcess process = new QiniuImageUploadProcess();
        process.run(null, QiniuImageUploadProcess.FILE_USAGE_SIMPLE, myCaptureFile, new ResponseListener() {
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
}
