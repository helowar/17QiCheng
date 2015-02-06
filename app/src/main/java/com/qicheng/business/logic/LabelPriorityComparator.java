package com.qicheng.business.logic;

import com.qicheng.business.module.LabelTypeList;

import java.util.Comparator;

/**
 * Created by NO3 on 2015/2/6.
 * 标签类型优先级比较器
 */
public class LabelPriorityComparator implements Comparator<LabelTypeList> {


    @Override
    public int compare(LabelTypeList lhs, LabelTypeList rhs) {
        if (lhs.getPriority() == rhs.getPriority()) {
            return 0;
        } else if (lhs.getPriority() > rhs.getPriority()) {
            return 1;
        } else {
            return -1;
        }

    }
}
