/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qicheng.R;
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
import com.qicheng.util.Const;

import org.w3c.dom.Text;

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
                Intent i = new Intent(getActivity(),BenefitCountActivity.class);
                startActivity(i);
            }
        });
        LinearLayout friendForBenefit = (LinearLayout)convertView.findViewById(R.id.friend_benefit);
        friendForBenefit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),BenefitRequestActivity.class));
            }
        });
        updateInitView();
        return convertView;
    }

    public void updateInitView(){
        BenefitLogic logic = (BenefitLogic)LogicFactory.self().get(LogicFactory.Type.Benefit);
        logic.initBenefitView(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs userEventArgs = (UserEventArgs)args;
                User user = userEventArgs.getResult();
                mRestNumber.setText(user.getValidBenefitCount()+"");
                mFriendNumber.setText(user.getFriendCount()+"");
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSound() ;
        mShakeListener = new ShakeListener(getActivity());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
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
                           mRestNumber.setText((Integer.parseInt(mRestNumber.getText().toString())+1)+"");
                           showTicket(benefitEventArgs.getBenefit());
                       }else {
                           if(benefitEventArgs.getErrCode() == OperErrorCode.ResultNoGrab){
                               Alert.Toast("没抢到，请再摇一次试试吧！");
                           }else if(benefitEventArgs.getErrCode() == OperErrorCode.ResultDistributeFinished){
                               Alert.Toast("福利全都送完了，快去找土豪分享一个吧！");
                           }else if(benefitEventArgs.getErrCode() == OperErrorCode.ResultNoBenefit){
                               Alert.Toast("本次行程中没有福利哦，快去朋友那儿找找吧！");
                           }
                       }
                       mVibrator.cancel();
                       mShakeListener.start();
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

    private void showTicket(Benefit benefit){
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout)inflaterDl.inflate(R.layout.dialog_ticket, null );
        initTicketDialog(layout,benefit);
        final Dialog dialog = new android.app.AlertDialog.Builder(getActivity()).create();
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

    private void initTicketDialog(View dialogView, Benefit benefit){

    }
}
