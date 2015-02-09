package com.qicheng.util;

import android.net.Uri;

/**
 * Created by NO3 on 2015/2/5.
 */
public class QiChengDb {
    public static final String DBNAME = "qichengdb";
    public static final String TNAME = "basedata_tag";
    public static final int VERSION = 1;

    public static String TID = "id";
    public static final String NAME = "name";
    public static final String PRIORITY = "priority";
    public static final String VALID = "valid";
    public static final String HOT = "hot";


    public static final String AUTOHORITY = "com.qicheng.tag";
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.qicheng.login";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.qicheng.login";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTOHORITY + "/basedata_tag");

}
