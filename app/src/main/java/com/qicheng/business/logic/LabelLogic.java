package com.qicheng.business.logic;

import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.Label;
import com.qicheng.business.protocol.GetLabelListProcess;
import com.qicheng.business.protocol.GetUserLabelProcess;
import com.qicheng.business.protocol.LabelProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;

import java.util.List;

/**
 * Created by NO3 on 2015/2/5.
 * 用户标签逻辑类
 */
public class LabelLogic extends BaseLogic {

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new LabelLogic();
        }
    }


    /**
     * 确定是否添加标签成功
     */
    public void checkUpdate(List<Label> list, final EventListener listener) {
        final LabelProcess labelProcess = new LabelProcess();
        List<Label> labelList = list;
        labelProcess.setSelectLabelList(list);
        labelProcess.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                //操作码转换
                OperErrorCode errCode = ProcessStatus.convertFromStatus(labelProcess.getStatus());
                //发送事件
                fireStatusEvent(listener, errCode);
            }
        });

    }

    public void getUserLabel(final EventListener listener) {
        final GetUserLabelProcess getUserLabelProcess = new GetUserLabelProcess();
        getUserLabelProcess.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(getUserLabelProcess.getStatus());
                LabelEventArgs labelEventArgs = new LabelEventArgs(getUserLabelProcess.getLabels(),errCode);
                fireEvent(listener,labelEventArgs);
            }
        });
    }

    public void getLabelList(final  EventListener listener){
        final GetLabelListProcess getLabelListProcess = new GetLabelListProcess();
        getLabelListProcess.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(getLabelListProcess.getStatus());
                LabelEventArgs labelEventArgs = new LabelEventArgs(getLabelListProcess.getmLabelTypes(),errCode);
                fireEvent(listener,labelEventArgs);
            }
        });
    }
}
