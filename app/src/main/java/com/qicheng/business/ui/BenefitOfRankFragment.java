/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.qicheng.R;
import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.module.BenefitUserRank;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BenefitDetailActivity.class);
                startActivity(intent);
            }
        });
        return view;
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
                    userRankList = result.getBenefitList();
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
            View view = null;
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.layout_benefit__item_rank, null);
            } else {
                view = convertView;
            }
            return view;
        }
    }
}

