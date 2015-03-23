package com.qicheng.business.protocol;

import com.google.gson.Gson;
import com.qicheng.business.module.LabelType;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 金玉龙 on 2015/2/13.
 * 启程App获取标签列表接口处理类
 */
public class GetLabelListProcess extends BaseProcess {

    private String url = "/basedata/tag_list.html";
    private ArrayList<LabelType> mLabelTypes = new ArrayList<LabelType>();

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        return null;
    }

    @Override
    protected void onResult(String result) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            int value = jsonObject.optInt(JSONUtil.STATUS_TAG);
            if (value == 0) {
                JSONArray arry = (JSONArray) jsonObject.opt("body");
                Gson gson = new Gson();
                for (int i = 0; i < arry.length(); i++) {
                    Object type = arry.get(i);
                    LabelType labelType = gson.fromJson(type.toString(), LabelType.class);
                    mLabelTypes.add(labelType);
                }
            }
            setProcessStatus(value);
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }

    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public ArrayList<LabelType> getmLabelTypes() {
        return mLabelTypes;
    }
}
