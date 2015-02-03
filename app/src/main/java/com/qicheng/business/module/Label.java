package com.qicheng.business.module;

import java.io.Serializable;

/**
 * Created by NO3 on 2015/2/2.
 */
public class Label implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    public String name;
    public int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
