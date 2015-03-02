package com.qicheng.business.logic.event;

import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;

/**
 * Created by NO3 on 2015/2/5.
 * 用户接收分组标签的列表
 */
public class LabelEventArgs extends StatusEventArgs {

    private ArrayList label;


    public LabelEventArgs(ArrayList value) {
        super(OperErrorCode.Success);
        label = value;
    }

    public LabelEventArgs(ArrayList value, OperErrorCode errCode) {
        super(errCode);
        label = value;
    }

    public ArrayList getLabel() {
        return label;
    }
}
