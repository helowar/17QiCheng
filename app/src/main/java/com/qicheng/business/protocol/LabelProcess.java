package com.qicheng.business.protocol;

import android.util.Log;

import com.google.gson.Gson;
import com.qicheng.business.module.LabelType;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by NO3 on 2015/2/5.
 * 标签报文发送与解析
 */
public class LabelProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.VerifyCodeProcess");

    private final String url = "http://192.168.1.107:8080/qps/basedata/tag_list.html";

    private ArrayList<LabelType> list = new ArrayList<LabelType>();


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
        try {
            //取回的JSON结果
            JSONObject o = new JSONObject(result);
            Log.d("tabel_result", result);
            //获取状态码
            int value = o.optInt("result_code");
            switch (value) {
                case 0:
                    setStatus(ProcessStatus.Status.Success);
                    break;
                case 1:
                    setStatus(ProcessStatus.Status.IllegalRequest);
                    break;
                default:
                    setStatus(ProcessStatus.Status.ErrUnkown);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatus(ProcessStatus.Status.ErrUnkown);
        }


    }

    @Override
    public String getFakeResult() {
        String r = "{\n" +
                "    \"result_code\": \"0\", \n" +
                "    \"body\": [\n" +
                "        {\n" +
                "            \"id\": \"XXXXXXXXXXXXXXXXXXA\",\n" +
                "            \"name\": \"看电影\",\n" +
                "            \"priority\": \"2\",\n" +
                "            \"tagList\": [\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXAA\", \"name\": \"动作片\", \"priority\": \"2\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXAB\", \"name\": \"恐怕片\", \"priority\": \"1\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXAC\",\"name\": \"科幻片\", \"priority\": \"3\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"XXXXXXXXXXXXXXXXXXB\",\n" +
                "            \"name\": \"运动\",\n" +
                "            \"priority\": \"1\",\n" +
                "            \"tagList\": [\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXBA\", \"name\": \"网球\", \"priority\": \"3\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXBB\", \"name\": \"羽毛球\", \"priority\": \"1\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXBC\",\"name\": \"保龄球\", \"priority\": \"2\"}\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(r);
            JSONArray arry = (JSONArray) object.opt("body");
            for (int i = 0; i < arry.length(); i++) {
                Object o = arry.get(i);
                LabelType labelTypeList = gson.fromJson(o.toString(), LabelType.class);
                list.add(labelTypeList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    public ArrayList<LabelType> getList() {
        return list;
    }

    public void setList(ArrayList<LabelType> list) {
        this.list = list;
    }
}
