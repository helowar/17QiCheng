package com.qicheng.business.logic.event;

import com.qicheng.business.module.LabelTypeList;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;

/**
 * Created by NO3 on 2015/2/5.
 */
public class LabelEventArgs extends StatusEventArgs {

    private ArrayList<LabelTypeList> labelTypeLists = new ArrayList<LabelTypeList>();

    public LabelEventArgs( ArrayList<LabelTypeList> value) {
        super(OperErrorCode.Success);
        labelTypeLists = value;
    }
    public LabelEventArgs(ArrayList<LabelTypeList> value, OperErrorCode errCode) {
        super(errCode);
        labelTypeLists = value;
    }

    public ArrayList<LabelTypeList> getLabelTypeLists() {
        return labelTypeLists;
    }

    public void setLabelTypeLists(ArrayList<LabelTypeList> labelTypeLists) {
        this.labelTypeLists = labelTypeLists;
    }
}
