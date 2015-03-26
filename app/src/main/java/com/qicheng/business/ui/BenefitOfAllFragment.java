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
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.module.Benefit;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.chat.activity.ContactActivity;
import com.qicheng.business.ui.chat.utils.Constant;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;


public class BenefitOfAllFragment extends BaseFragment {

    private View view;
    private ListView listView;
    private BenefitListAdapter benefitListAdapter;
    private List<Benefit> benefitList;

    private Benefit targetBenefit;

    private static final int REQUEST_CODE_BENEFIT_TO_CONTACT = 30;

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
                    //从聊天中发起转发行为
                    if (getActivity().getIntent().getBooleanExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY, false)) {
                        benefitList = new ArrayList<Benefit>();
                        for (Benefit b : result.getBenefitList()) {
                            if (b.getPostOpFlag() == 1) {//只有可转让的福利才在从聊天发起的列表界面中显示
                                benefitList.add(b);
                            }
                        }
                    } else {
                        benefitList = result.getBenefitList();
                    }
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Benefit benefit = list.get(position);
            StringBuilder builder = new StringBuilder(getResources().getString(R.string.deadline_text));
            builder.append(benefit.getExpireTime());
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_benefit_item, null);
            //从聊天中发起转发行为
            if (getActivity().getIntent().getBooleanExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY, false)) {
                ImageView benefitIcon = (ImageView) convertView.findViewById(R.id.benefit_icon);
                ImageManager.displayImageDefault(benefit.getLogoUrl(), benefitIcon);
                TextView title = (TextView) convertView.findViewById(R.id.benefit_title);
                title.setText(benefit.getName());
                TextView expireTime = (TextView) convertView.findViewById(R.id.benefit_deadline);
                expireTime.setText(builder.toString());
                TextView description = (TextView) convertView.findViewById(R.id.benefit_content);
                description.setText(benefit.getDescription());
                TextView value = (TextView) convertView.findViewById(R.id.benefit_value);
                if (benefit.getValue() == 0.0) {
                    TextView rmb = (TextView) convertView.findViewById(R.id.rmb_text);
                    rmb.setVisibility(View.GONE);
                    value.setVisibility(View.GONE);
                } else {
                    value.setText(String.valueOf(benefit.getValue()));
                }
                //不需要转发标识，因所有均可转发
                convertView.findViewById(R.id.share_img).setVisibility(View.GONE);
                //增加点击转发事件
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity().getIntent().getBooleanExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY, false)) {
                            getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra(Const.Intent.BENEFIT_ENTITY_FOR_DETAIL, benefit));
                            getActivity().finish();
                        }
                    }
                });
            } else {
                ImageView benefitIcon = (ImageView) convertView.findViewById(R.id.benefit_icon);
                ImageManager.displayImageDefault(benefit.getLogoUrl(), benefitIcon);
                TextView title = (TextView) convertView.findViewById(R.id.benefit_title);
                title.setText(benefit.getName());
                TextView expireTime = (TextView) convertView.findViewById(R.id.benefit_deadline);

                expireTime.setText(builder.toString());
                TextView description = (TextView) convertView.findViewById(R.id.benefit_content);
                description.setText(benefit.getDescription());
                TextView value = (TextView) convertView.findViewById(R.id.benefit_value);
                if (benefit.getValue() == 0.0) {
                    TextView rmb = (TextView) convertView.findViewById(R.id.rmb_text);
                    rmb.setVisibility(View.GONE);
                    value.setVisibility(View.GONE);
                } else {
                    value.setText(String.valueOf(benefit.getValue()));
                }
                ImageView shareImg = (ImageView) convertView.findViewById(R.id.share_img);
                shareImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//在清单页面发起转发
                        //TODO 需调用联系人Activity
                        targetBenefit = benefit;
                        Intent i = new Intent(getActivity(), ContactActivity.class);
                        i.putExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY, false);
                        startActivityForResult(i, REQUEST_CODE_BENEFIT_TO_CONTACT);
                    }
                });
                if (benefit.getPostOpFlag() == 0) {
                    shareImg.setVisibility(View.GONE);
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity().getIntent().getBooleanExtra(Const.Intent.IS_FROM_CHAT_ACTIVITY_KEY, false)) {
                            Benefit b = list.get(position);
                            getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent().putExtra(Const.Intent.BENEFIT_ENTITY_FOR_DETAIL, b));
                            getActivity().finish();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), BenefitDetailActivity.class);
                        intent.putExtra(Const.Intent.BENEFIT_DETAIL, benefit);
                        startActivity(intent);
                    }
                });
            }
            return convertView;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE_BENEFIT_TO_CONTACT && resultCode == Activity.RESULT_OK) {
            User targetUser = (User) intent.getSerializableExtra(Const.Intent.USER_ENTITY_FROM_CONTACT);
            EMConversation conversation = EMChatManager.getInstance().getConversation(targetUser.getUserImId());
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            setUserInfoIntoMessage(message, targetUser);
            TextMessageBody txtBody = new TextMessageBody("转发福利:" + targetBenefit.getName());
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(targetUser.getUserImId());
            //设置发送福利的标识
            message.setAttribute(Constant.MESSAGE_ATTR_IS_TICKET, true);
            message.setAttribute(Const.Easemob.BENEFIT_ICON_URL, targetBenefit.getLogoUrl());
            message.setAttribute(Const.Easemob.BENEFIT_TITLE_TXT, targetBenefit.getName());
            message.setAttribute(Const.Easemob.BENEFIT_VALUE, targetBenefit.getValue() + "");
            BenefitLogic logic = (BenefitLogic) LogicFactory.self().get(LogicFactory.Type.Benefit);
            logic.transferBenefit(targetBenefit.getId(), targetUser.getUserImId());
            // 把messgage加到conversation中
            conversation.addMessage(message);
            benefitList.remove(targetBenefit);
            benefitListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 把环信无法获取的头像/昵称等用户基本信息塞进EMMessage，imId作为标识判断该取哪一个
     *
     * @param message
     */
    private void setUserInfoIntoMessage(EMMessage message, User user) {
        message.setAttribute(Const.Easemob.FROM_USER_NICK, Cache.getInstance().getUser().getNickName());
        message.setAttribute(Const.Easemob.FROM_USER_AVATAR, Cache.getInstance().getUser().getPortraitURL());
        message.setAttribute(Const.Easemob.FROM_USER_ID, Cache.getInstance().getUser().getUserImId());
        message.setAttribute(Const.Easemob.TO_USER_AVATAR, user.getPortraitURL());
        message.setAttribute(Const.Easemob.TO_USER_NICK, user.getNickName());
        message.setAttribute(Const.Easemob.TO_USER_ID, user.getUserImId());
    }
}

