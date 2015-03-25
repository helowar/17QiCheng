/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.module.BenefitUserRank;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.util.StringUtil;

import java.util.List;


public class BenefitOfRankFragment extends BaseFragment {

    private View view;
    private ListView listView;
    private BenefitListAdapter benefitListAdapter;
    private List<BenefitUserRank> userRankList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_benefit_of_rank, container, false);
        listView = (ListView) view.findViewById(R.id.benefit_rank_list);
        getBenefitRankList(null);
        return view;
    }


    /**
     * 初始化当前用户的福利排名
     *
     * @param userRankList
     */
    private void initSelfView(List<BenefitUserRank> userRankList) {
        for (BenefitUserRank userRank : userRankList) {
            if (StringUtil.isEmpty(userRank.getUserId())) {
                ImageView portrait = (ImageView) view.findViewById(R.id.self_portrait);
                ImageManager.displayPortrait(userRank.getPortraitUrl(), portrait);
                TextView ranking = (TextView) view.findViewById(R.id.benefit_ranking);
                ranking.setText(String.valueOf(userRank.getRanking()));
                TextView benefitNum = (TextView) view.findViewById(R.id.self_benefit_num);
                benefitNum.setText(String.valueOf(userRank.getBenefitNum()));
            }
        }
    }

    /**
     * 获取用户福利排名列表
     *
     * @param benefitTypeId
     */
    private void getBenefitRankList(String benefitTypeId) {
        BenefitLogic logic = (BenefitLogic) LogicFactory.self().get(LogicFactory.Type.Benefit);
        logic.getBenefitRankList(benefitTypeId, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                BenefitEventArgs result = (BenefitEventArgs) args;
                OperErrorCode errorCode = result.getErrCode();
                if (errorCode == OperErrorCode.Success) {
                    userRankList = result.getBenefitUserRankList();
                    initSelfView(userRankList);
                    benefitListAdapter = new BenefitListAdapter(getActivity(), userRankList);
                    listView.setAdapter(benefitListAdapter);
                }
            }
        }));
    }


    /**
     * 福利列表的Adapter
     */
    public class BenefitListAdapter extends BaseAdapter {
        private Context context;

        private List<BenefitUserRank> list;

        BenefitListAdapter(Context context) {
            this.context = context;
        }

        BenefitListAdapter(Context context, List<BenefitUserRank> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                BenefitUserRank rank = list.get(position);
                convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_benefit__item_rank, null);
                ImageView portrait = (ImageView) convertView.findViewById(R.id.portrait_img);
                ImageManager.displayPortrait(rank.getPortraitUrl(), portrait);
                TextView name = (TextView) convertView.findViewById(R.id.user_name);
                name.setText(rank.getUserName());
                TextView benefitNum = (TextView) convertView.findViewById(R.id.all_num);
                benefitNum.setText(String.valueOf(rank.getBenefitNum()));
                ImageView benefitWinner = (ImageView) convertView.findViewById(R.id.benefit_winner);
                /*
                 *前三名的皇冠
                 */
                switch (position) {
                    case 0:
                        benefitWinner.setImageResource(R.drawable.ic_gold);
                        break;
                    case 1:
                        benefitWinner.setImageResource(R.drawable.ic_silver);
                        break;
                    case 2:
                        benefitWinner.setImageResource(R.drawable.ic_copper);
                        break;
                    default:
                        benefitWinner.setVisibility(View.GONE);
                }


            }
            return convertView;
        }
    }
}

