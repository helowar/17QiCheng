package com.qicheng.business.module;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NO3 on 2015/2/5.
 * 标签类型分组的module类
 */
public class LabelTypeList implements Serializable {

    private String id;
    private String name;
    private int priority;
    private ArrayList<LabelItem> tagList = new ArrayList<LabelItem>();

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

        return tagList;
    }

    public void setTagList(ArrayList<LabelItem> tagList) {
        this.tagList = tagList;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "LabelTypeList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", tagList=" + tagList +
                '}';
    }
}
