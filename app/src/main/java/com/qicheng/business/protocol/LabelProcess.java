package com.qicheng.business.protocol;

import android.util.Log;

import com.qicheng.business.module.Label;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 金玉龙 on 2015/2/5.
 * 启程App添加标签接口处理类
 */
public class LabelProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.VerifyCodeProcess");

    private final String url = "/user/add_tags.html";

    private List<Label> selectLabelList = new ArrayList<Label>();

    /**
     * 获取请求的url
     *
     * @return url
     */
    @Override
    protected String getRequestUrl() {
        return url;
    }


    /**
     * 拼装json作为参数传入url中
     *
     * @return json字符串
     */
    @Override
    protected String getInfoParameter() {

        JSONObject jsonObj = new JSONObject();
        JSONArray labelIds = new JSONArray();
        JSONArray personalTags = new JSONArray();
        try {
            for (int i = 0; i < selectLabelList.size(); i++) {
                Label label = selectLabelList.get(i);
                if (label.getTypeId() != null) {
                    labelIds.put(label.getItemId());
                } else {
                    personalTags.put(label.getItemName());
                }
            }
            jsonObj.put("tag_ids", labelIds);
            if (personalTags.length() > 0) {
                jsonObj.put("personal_tags", personalTags);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /**
     * 获取返回码
     *
     * @param result
     */
    @Override
    protected void onResult(String result) {
        try {
            //取回的JSON结果
            JSONObject o = new JSONObject(result);
            Log.d("tabel_result", result);
            //获取状态码
            int value = o.optInt("result_code");
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

    public List<Label> getSelectLabelList() {
        return selectLabelList;
    }

    public void setSelectLabelList(List<Label> selectLabelList) {
        this.selectLabelList = selectLabelList;
    }
}
