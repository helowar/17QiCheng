package com.qicheng.business.logic.event;

import com.qicheng.business.module.LabelType;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO3 on 2015/2/5.
 * 用户接收分组标签的列表
 */
public class LabelEventArgs extends StatusEventArgs {

    private List<LabelType> labelType;

    public LabelEventArgs(List<LabelType> value) {
        super(OperErrorCode.Success);
        labelType = value;
    }

    public LabelEventArgs(ArrayList<LabelType> value, OperErrorCode errCode) {
        super(errCode);
        labelType = value;
    }

    public List<LabelType> getLabelType() {
        return labelType;
    }
}
