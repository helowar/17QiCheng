package com.qicheng.business.protocol;

import com.qicheng.business.module.Dyn;
import com.qicheng.business.ui.component.DynSearch;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * Created by NO3 on 2015/2/28.
 */
public class GetDynListProcess extends BaseProcess {
    private static Logger logger = new Logger("com.qicheng.business.protocol.GetDynListProcess");

    private String url = "/activity/list.html";

    private DynSearch dynSearch;

    private ArrayList<Dyn> dynList;

    public void setDynSearch(DynSearch dynSearch) {
        this.dynSearch = dynSearch;
    }

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        // 组装传入服务端参数
        try {
            JSONObject o = new JSONObject();
            Byte queryType = dynSearch.getQueryType();
            if (queryType != null) {
                spliceParameter(o);
                logger.e("组装传入搜索城市动态参数成功");
                logger.d(o.toString());
                //后期出问题时进行排查，default是否有作用
//                switch (queryType) {
//                    case Const.QUERY_TYPE_CITY:
//                        spliceParameter(o);
//                        logger.e("组装传入搜索城市动态参数成功");
//                        logger.d(o.toString());
//                        break;
//                    case Const.QUERY_TYPE_TRAIN:
//                        spliceParameter(o);
//                        logger.e("组装传入搜索车次动态参数成功");
//                        logger.d(o.toString());
//                        break;
//                    case Const.QUERY_TYPE_NEAR:
//                        spliceParameter(o);
//                        logger.e("组装传入搜索附近动态参数成功");
//                        logger.d(o.toString());
//                        break;
//                    case Const.QUERY_TYPE_STATION:
//                        spliceParameter(o);
//                        logger.e("组装传入搜索车站动态参数成功");
//                        logger.d(o.toString());
//                        break;
//                    case Const.QUERY_TYPE_MY:
//                        spliceParameter(o);
//                        logger.e("组装传入搜索我的动态参数成功");
//                        logger.d(o.toString());
//                        break;
//                    case Const.QUERY_TYPE_USER:
//                        spliceParameter(o);
//                        logger.e("组装传入搜索我的动态参数成功");
//                        logger.d(o.toString());
//                        break;
//
//                    default:
//                        o.put("order_by", dynSearch.getOrderBy());
//                        break;
//                }
            } else {
                o.put("order_by", dynSearch.getOrderBy());
                if (dynSearch.getOrderNum() != 0) {
                    o.put("order_num", dynSearch.getOrderNum());
                }
            }
            return o.toString();
        } catch (Exception e) {
            logger.e("组装传入搜索动态参数异常");
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            // 取回的JSON结果
            JSONObject o = new JSONObject(result);
            int resultCode = o.optInt(STATUS_TAG);
            setProcessStatus(resultCode);
            if (resultCode == Const.ResponseResultCode.RESULT_SUCCESS) {
                JSONArray jsonArrayDynList = o.has("body") ? o.optJSONArray("body") : null;
                if (jsonArrayDynList != null) {
                    dynList = new ArrayList<Dyn>();
                    for (int i = 0, size = jsonArrayDynList.length(); i < size; i++) {
                        JSONObject jsonDyn = (JSONObject) jsonArrayDynList.getJSONObject(i);
                        Dyn dyn = new Dyn();
                        dyn.setUserId(jsonDyn.optString("user_id"));
                        dyn.setUserImId(jsonDyn.optString("user_im_id"));
                        dyn.setPortraitUrl(jsonDyn.optString("portrait_url"));
                        dyn.setNickName(jsonDyn.optString("nickname"));
                        dyn.setAnmName(jsonDyn.optString("anm_name"));
                        dyn.setActivityId(jsonDyn.optString("activity_id"));
                        dyn.setContent(jsonDyn.optString("content"));
                        dyn.setType(jsonDyn.optInt("type"));
                        dyn.setLikedNum(jsonDyn.optInt("liked_num"));
                        dyn.setSharedNum(jsonDyn.optInt("shared_num"));
                        dyn.setIsLiked(jsonDyn.optInt("is_liked"));
                        //dyn.setIsShared(jsonDyn.optInt("is_shared"));
                        dyn.setCreateTime(StringUtil.stringToDate(jsonDyn.optString("create_time")));
                        dyn.setOrderNum(jsonDyn.optInt("order_num"));
                        JSONArray jsonFileArray = jsonDyn.has("files") ? jsonDyn.optJSONArray("files") : null;
                        if (jsonFileArray != null && jsonFileArray.length() > 0) {
                            JSONObject jsonFile = (JSONObject) jsonFileArray.getJSONObject(0);
                            dyn.setFileType(jsonFile.optInt("file_type"));
                            dyn.setThumbnailUrl(jsonFile.optString("thumbnail_url"));
                            dyn.setFileUrl(jsonFile.optString("file_url"));
                        }
                        dynList.add(dyn);
                    }
                    logger.d(dynList.toString());
                } else {
                    setStatus(ProcessStatus.Status.InfoNoData);
                }
                logger.d("获取动态信息成功");
            } else {
                logger.e("获取动态失败");
            }
        } catch (Exception e) {
            setStatus(ProcessStatus.Status.ErrException);
            logger.e("处理搜索动态响应结果时异常");
            e.printStackTrace();
        }
    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public DynSearch getDynSearch() {
        return dynSearch;
    }

    public ArrayList<Dyn> getDynList() {
        return dynList;
    }

    public void setDynList(ArrayList<Dyn> dynList) {
        this.dynList = dynList;
    }

    /**
     * 当搜索的type不为空的时候拼接Json字符串
     */
    public void spliceParameter(JSONObject o) throws Exception {
        o.put("order_by", dynSearch.getOrderBy());
        o.put("query_type", dynSearch.getQueryType());
        if (dynSearch.getQueryType() != Const.QUERY_TYPE_MY) {
            o.put("query_value", dynSearch.getQueryValue());
        }
        if (dynSearch.getOrderNum() != 0) {
            o.put("order_num", dynSearch.getOrderNum());
        }
    }

}
