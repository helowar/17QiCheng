package com.qicheng.business.logic;

import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.LabelTypeList;
import com.qicheng.business.protocol.LabelProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;

import java.util.ArrayList;

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

    ArrayList<LabelTypeList> labelTypeLists;

    /**
     * 直接获取用户标签
     */
    public void getLabelList(final EventListener listener) {
        final LabelProcess process = new LabelProcess();
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                //操作码转换
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                //接收标签列表和返回值
                LabelEventArgs args = new LabelEventArgs(labelTypeLists, errCode);

                if (errCode == OperErrorCode.Success) {
                    /**
                     *
                     */
                } else {
                    process.getFakeResult();
                    labelTypeLists = process.getList();
                    args.setLabelTypeLists(labelTypeLists);
                }
                //发送事件
                fireEvent(listener, args);

            }
        });

    }

}
