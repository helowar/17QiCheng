/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.protocol;

import com.qicheng.business.module.BenefitUserRank;
import com.qicheng.framework.protocol.BaseProcess;
import com.qicheng.util.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.framework.util.JSONUtil.STATUS_TAG;

/**
 * Created by 金玉龙 on 2015/3/23.
 * 启程APP的获取用户福利排名列表的接口处理类
 */
public class GetBenefitRankListProcess extends BaseProcess {
    private String url = "/benefit/get_friend_benefits.html";

    private String benefitTypeId;

    private List<BenefitUserRank> rankList;

    @Override
    protected String getRequestUrl() {
        return url;
    }

    @Override
    protected String getInfoParameter() {
        try {
            JSONObject o = new JSONObject();
            o.put("benefit_type_id", benefitTypeId);
            return o.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResult(String result) {
        try {
            JSONObject o = new JSONObject(result);
            int value = o.optInt(STATUS_TAG);
            setProcessStatus(value);
            if (value == Const.ResponseResultCode.RESULT_SUCCESS) {
                JSONArray benefitRankArray = o.has("body") ? o.optJSONArray("body") : null;
                if (benefitRankArray != null && benefitRankArray.length() > 0) {
                    int length = benefitRankArray.length();
                    rankList = new ArrayList<BenefitUserRank>(length);
                    for (int i = 0; i < length; i++) {
                        o = benefitRankArray.getJSONObject(i);
                        BenefitUserRank benefitRank = new BenefitUserRank();
                        benefitRank.setUserId(o.optString("user_id"));
                        benefitRank.setUserImId(o.optString("user_im_id"));
                        benefitRank.setUserName(o.optString("nickname"));
                        benefitRank.setPortraitUrl(o.optString("portrait_url"));
                        benefitRank.setBenefitNum(o.optInt("benefit_num"));
                        benefitRank.setGender(o.optInt("gender"));
                        benefitRank.setRanking(o.optInt("ranking"));
                        rankList.add(benefitRank);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String getFakeResult() {
        return null;
    }

    public void setBenefitTypeId(String benefitTypeId) {
        this.benefitTypeId = benefitTypeId;
    }

    public List<BenefitUserRank> getRankList() {
        return rankList;
    }
}
