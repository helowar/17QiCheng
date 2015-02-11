package com.qicheng.business.module;

import java.io.Serializable;

/**
 * Created by NO3 on 2015/2/2.
 */
public class Label implements Serializable {
    private static final long serialVersionUID = 1L;

    private String typeId;
    private String itemId;
    private String itemName;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Label{");
        sb.append("typeId='").append(typeId).append('\'');
        sb.append(", itemId='").append(itemId).append('\'');
        sb.append(", itemName='").append(itemName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
