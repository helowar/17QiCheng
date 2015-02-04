package com.qicheng.framework.util;

import org.json.JSONObject;

/**
 * Created by NO1 on 2015/2/4.
 */
public class JSONUtil {

    private static final String BODY_TAG = "body";

    public static JSONObject getResultBody(JSONObject result) throws Exception{
        if(result!=null){
            return result.getJSONObject("body");
        }
        return null;
    }
}
