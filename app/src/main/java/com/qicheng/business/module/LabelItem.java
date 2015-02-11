package com.qicheng.business.module;

import java.io.Serializable;

/**
 * Created by NO3 on 2015/2/5.
 * 标签元素的实体类
 */
public class LabelItem implements Serializable {


    private String id;
    private String name;
    private int priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LabelItem{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", priority=").append(priority);
        sb.append('}');
        return sb.toString();
    }
}
