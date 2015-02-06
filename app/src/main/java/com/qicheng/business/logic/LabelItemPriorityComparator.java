package com.qicheng.business.logic;

import com.qicheng.business.module.LabelItem;

import java.util.Comparator;

/**
 * Created by NO3 on 2015/2/6.
 * 比较小标签的的优先级
 */
public class LabelItemPriorityComparator implements Comparator<LabelItem> {

    @Override
    public int compare(LabelItem lhs, LabelItem rhs) {
        if (lhs.getPriority() == rhs.getPriority()) {
            return 0;
        } else if (lhs.getPriority() > rhs.getPriority()) {
            return 1;
        } else {
            return -1;
        }
    }
}
