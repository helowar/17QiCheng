/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.BenefitLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.BenefitEventArgs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.Benefit;
import com.qicheng.business.module.User;
import com.qicheng.business.ui.component.ShakeListener;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BenifitFragment extends BaseFragment {

    private LinearLayout viewTicket;

    ShakeListener mShakeListener = null;
    Vibrator mVibrator;
    private SoundPool sndPool;
    private HashMap<Integer, Integer> soundPoolMap = new HashMap<Integer, Integer>();

    private TextView mRestNumber;
    private TextView mFriendNumber;


    public BenifitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVibrator = (Vibrator) Const.Application.getSystemService(Context.VIBRATOR_SERVICE);
        mShakeListener = new ShakeListener(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_benifit, container, false);
        viewTicket =(LinearLayout) convertView.findViewById(R.id.ticket_view);
        mRestNumber = (TextView)convertView.findViewById(R.id.rest_number);
        mFriendNumber = (TextView)convertView.findViewById(R.id.friend_number);
        LinearLayout showBenefitList = (LinearLayout)convertView.findViewById(R.id.show_benefit_list);
        showBenefitList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(mRestNumber.getText().toString())>0) {
                    Intent i = new Intent(getActivity(), BenefitCountActivity.class);
                    startActivity(i);
                }
            }
        });
        LinearLayout friendForBenefit = (LinearLayout)convertView.findViewById(R.id.friend_benefit);
        friendForBenefit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(mFriendNumber.getText().toString())>0){
                    startActivity(new Intent(getActivity(),BenefitRequestActivity.class));
                }
            }
        });
        return convertView;
    }

    /**
     * 获取界面初始化显示值
     */
    public void updateInitView(){
        BenefitLogic logic = (BenefitLogic)LogicFactory.self().get(LogicFactory.Type.Benefit);
        logic.initBenefitView(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs userEventArgs = (UserEventArgs)args;
                if(userEventArgs.getErrCode()==OperErrorCode.Success){
                    User user = userEventArgs.getResult();
                    mRestNumber.setText(user.getValidBenefitCount()+"");
                    Const.Application.getBenefitChangedListener().updateBenefitBadge(user.getValidBenefitCount());
                    mFriendNumber.setText(user.getFriendCount()+"");
                }
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateInitView();
        loadSound() ;
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                //记录友盟事件
                MobclickAgent.onEvent(getActivity(),Const.MobclickAgent.EVENT_SHAKE_BENEFIT);
                //Toast.makeText(getApplicationContext(), "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
                startAnim();  //开始 摇一摇手掌动画
                mShakeListener.stop();
                sndPool.play(soundPoolMap.get(0), (float) 1, (float) 1, 0, 0,(float) 1.2);
                startVibrato();
               BenefitLogic logic = (BenefitLogic) LogicFactory.self().get(LogicFactory.Type.Benefit);
               logic.getNewBenefit(createUIEventListener(new EventListener() {
                   @Override
                   public void onEvent(EventId id, EventArgs args) {
                       BenefitEventArgs benefitEventArgs = (BenefitEventArgs)args;
                       if(benefitEventArgs.getErrCode()== OperErrorCode.Success){
                           sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1, 0, 0,(float) 1.0);
                           int benefitCount = Integer.parseInt(mRestNumber.getText().toString())+1;
                           mRestNumber.setText(benefitCount+"");
                           //更新底部福利数量提示
                           Const.Application.getBenefitChangedListener().updateBenefitBadge(benefitCount);
                           showTicket(benefitEventArgs.getBenefit(),null);
                       }else {
                           if(benefitEventArgs.getErrCode() == OperErrorCode.ResultNoGrab){
                               showTicket(null, "没抢到，请再摇一次试试吧！");
                           }else if(benefitEventArgs.getErrCode() == OperErrorCode.ResultDistributeFinished){
                               showTicket(null, "福利全都送完了，快去找土豪分享一个吧！");
                           }else if(benefitEventArgs.getErrCode() == OperErrorCode.ResultNoBenefit){
                               showTicket(null, "本次行程中没有福利哦，快去朋友那儿找找吧！");
                           }
                       }
                       mVibrator.cancel();
                   }
               }));
            }
        });
    }

    private void loadSound() {

        sndPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
        new Thread() {
            public void run() {
                try {
                    soundPoolMap.put(
                            0,
                            sndPool.load(getActivity().getAssets().openFd(
                                    "sound/shake_sound_male.mp3"), 1));

                    soundPoolMap.put(
                            1,
                            sndPool.load(getActivity().getAssets().openFd(
                                    "sound/shake_match.mp3"), 1));
                    soundPoolMap.put(
                            2,
                            sndPool.load(getActivity().getAssets().openFd(
                                    "sound/shake_nomatch.mp3"), 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void startAnim () {   //定义摇一摇动画动画
        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+0.5f);
        mytranslateanimdn0.setDuration(1000);
        TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
        mytranslateanimdn1.setDuration(1000);
        mytranslateanimdn1.setStartOffset(1000);
        animdn.addAnimation(mytranslateanimdn0);
        animdn.addAnimation(mytranslateanimdn1);
        viewTicket.startAnimation(animdn);
    }

    public void startVibrato(){     //定义震动
        mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mShakeListener != null) {
            mShakeListener.stop();
        }
    }

    private void showTicket(Benefit benefit,String failReason){
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout)inflaterDl.inflate(R.layout.dialog_ticket, null );
        initTicketDialog(layout,benefit,failReason);
        final Dialog dialog = new android.app.AlertDialog.Builder(getActivity()).create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mShakeListener.start();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //对话框
        dialog.show();
        dialog.getWindow().setContentView(layout);
    }

    private void initTicketDialog(View dialogView, Benefit benefit,String failReason){
        ImageView iconView = (ImageView)dialogView.findViewById(R.id.benefit_icon);
        TextView titleView = (TextView)dialogView.findViewById(R.id.benefit_title);
        TextView deadLineView = (TextView)dialogView.findViewById(R.id.benefit_deadline);
        TextView contentView = (TextView)dialogView.findViewById(R.id.benefit_content);
        TextView valueView = (TextView)dialogView.findViewById(R.id.benefit_value);
        if(benefit!=null && StringUtil.isEmpty(failReason)){
            ImageManager.displayImageDefault(benefit.getLogoUrl(),iconView);
            titleView.setText(benefit.getName());
            deadLineView.setText(benefit.getExpireTime());
            contentView.setText(benefit.getDescription());
            valueView.setText(benefit.getValue()+"");
        }else {
            iconView.setVisibility(View.GONE);
            titleView.setText(failReason);
            deadLineView.setVisibility(View.GONE);
            contentView.setVisibility(View.GONE);
            valueView.setVisibility(View.GONE);
            dialogView.findViewById(R.id.deadline_icon).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ic_price).setVisibility(View.GONE);
            dialogView.findViewById(R.id.ic_value).setVisibility(View.GONE);
        }
    }

    /**
     * 外部调用开启shakeListener，保证切换回福利tab时可监听shake
     */
    public void startShakeListener(){
        if(mShakeListener!=null){
            mShakeListener.start();
        }
    }

    /**
     * 外部调用停止shakeListener，保证切换出去后不再监听shake
     */
    public void stopShakeListener(){
        if(mShakeListener!=null){
            mShakeListener.stop();
        }
    }

}
