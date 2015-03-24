/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.module.Benefit;
import com.qicheng.business.ui.component.GeneralListView;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.module.Benefit;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.util.UIUtil;
import com.qicheng.util.Const;
import com.qicheng.util.Const;

import java.util.List;


public class BenefitOfAllFragment extends BaseFragment {

    private View view;
    private ListView listView;
    private BenefitListAdapter benefitListAdapter;
    private List<Benefit> benefitList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_benefit_of_all, container, false);
        listView = (ListView) view.findViewById(R.id.benefit_list);
        Bundle bundle = this.getArguments();
        int typeThing = bundle.getInt(Const.Intent.TYPE_THING);
        String benefitTypeId = bundle.getString(Const.Intent.BENEFIT_TYPE_ID);
        getBenefitList(benefitTypeId, typeThing);
        return view;
    }

    /**
     * 获取用户福利列表
     *
     * @param benefitTypeId
     * @param thingType
     */
    private void getBenefitList(String benefitTypeId, int thingType) {
        BenefitLogic logic = (BenefitLogic) LogicFactory.self().get(LogicFactory.Type.Benefit);
        logic.getBenefitList(benefitTypeId, thingType, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                BenefitEventArgs result = (BenefitEventArgs) args;
                OperErrorCode errorCode = result.getErrCode();
                if (errorCode == OperErrorCode.Success) {
                    benefitList = result.getBenefitList();
                    benefitListAdapter = new BenefitListAdapter(getActivity(), benefitList);
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

        private List<Benefit> list;

        BenefitListAdapter(Context context) {
            this.context = context;
        }

        BenefitListAdapter(Context context, List<Benefit> list) {
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
            final Benefit benefit = list.get(position);
            if (convertView == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.layout_benefit_item, null);
                ImageView benefitIcon = (ImageView) view.findViewById(R.id.benefit_icon);
                ImageManager.displayImageDefault(benefit.getLogoUrl(), benefitIcon);
                TextView title = (TextView) view.findViewById(R.id.benefit_title);
                title.setText(benefit.getName());
                TextView expireTime = (TextView) view.findViewById(R.id.benefit_deadline);
                expireTime.setText("截止日期 " + benefit.getExpireTime());
                TextView description = (TextView) view.findViewById(R.id.benefit_content);
                description.setText(benefit.getDescription());
                TextView value = (TextView) view.findViewById(R.id.benefit_value);
                value.setText(benefit.getValue() + "");
                ImageView shareImg = (ImageView) view.findViewById(R.id.share_img);
                if (benefit.getPostOpFlag() == 0) {
                    shareImg.setVisibility(View.GONE);
                }
                LinearLayout textLayout = (LinearLayout) view.findViewById(R.id.benefit_text);
                textLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BenefitDetailActivity.class);
                        intent.putExtra(Const.Intent.BENEFIT_DETAIL, benefit);
                        startActivity(intent);
                    }
                });

            } else {
                view = convertView;
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity().getIntent().getBooleanExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY,false)){
                        Benefit b = list.get(position);
                        getActivity().setResult(Activity.RESULT_OK,getActivity().getIntent().putExtra(Const.Intent.BENEFIT_ENTITY_FOR_DETAIL,b));
                        getActivity().finish();
                    }
                }
            });
            return view;
        }
    }
}

