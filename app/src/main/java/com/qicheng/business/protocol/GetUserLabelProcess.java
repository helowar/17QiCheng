package com.qicheng.business.protocol;

import android.util.Log;

import com.qicheng.business.module.LabelItem;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NO3 on 2015/2/13.
 */
public class GetUserLabelProcess extends BaseProcess {

    private final String url = "/user/tag_list.html";

    private ArrayList<LabelItem> labels;

    public ArrayList<LabelItem> getLabels() {
        return labels;
    }

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
        JSONObject o = null;
        try {
            o = new JSONObject(result);
            Log.d("tabel_result", result);
            //获取状态码
            int value = o.optInt(JSONUtil.STATUS_TAG);

            /**
             * 取出返回值
             */
            if (value == 0) {
                JSONObject body = JSONUtil.getResultBody(o);
                Log.d("body", body.toString());
                labels = new ArrayList<LabelItem>();
                JSONArray tagListArray = (JSONArray) body.opt("tag_list");
                for (int i = 0; i < tagListArray.length(); i++) {
                    JSONObject obj = (JSONObject) tagListArray.get(i);
                    LabelItem item = new LabelItem();
                    item.setId(obj.optString("id"));
                    item.setName(obj.optString("name"));
                    labels.add(item);
                }
                JSONArray personalTagArray = (JSONArray) body.opt("personal_tags");
                for (int i = 0; i < personalTagArray.length(); i++) {
                    LabelItem item = new LabelItem();
                    item.setName(personalTagArray.get(i).toString());
                    labels.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }


    }

    @Override
    protected String getFakeResult() {

        return null;
    }
}
