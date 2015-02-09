package com.qicheng.business.logic;

import android.content.Intent;

import com.qicheng.business.module.LabelType;

import java.util.Comparator;

/**
 * Created by NO3 on 2015/2/6.
 * 标签类型优先级比较器
 */
public class LabelPriorityComparator implements Comparator<LabelType> {


    @Override
    public int compare(LabelType lhs, LabelType rhs) {

        if (lhs.getPriority() == rhs.getPriority()) {
            return 0;
        } else if (lhs.getPriority() > rhs.getPriority()) {
            return 1;
        } else {
            return -1;
        }

    }
}
