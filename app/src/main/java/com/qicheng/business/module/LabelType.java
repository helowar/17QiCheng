package com.qicheng.business.module;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NO3 on 2015/2/5.
 * 标签类型分组的module类
 */
public class LabelType implements Serializable {

    private String id;

    private String name;

    private int priority;

    private ArrayList<LabelItem> tag_list = new ArrayList<LabelItem>();

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

    public ArrayList<LabelItem> getTagList() {

        return tag_list;
    }

    public void setTagList(ArrayList<LabelItem> tagList) {
        this.tag_list = tagList;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LabelType{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", priority=").append(priority);
        sb.append(", tagList=").append(tag_list);
        sb.append('}');
        return sb.toString();
    }
}
