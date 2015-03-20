/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;


import android.content.Context;
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
import android.widget.Toast;

import com.qicheng.R;
import com.qicheng.business.ui.component.ShakeListener;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.util.Const;

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
        return convertView;

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
                //TODO 调用获取福利的logic
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        sndPool.play(soundPoolMap.get(1), (float) 1, (float) 1, 0, 0,(float) 1.0);

                        //mtoast.setGravity(Gravity.CENTER, 0, 0);
                        mVibrator.cancel();
                        mShakeListener.start();

                    }
                }, 2000);
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
}
