package com.qicheng.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;

import java.util.Random;

public class Welcome extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView cover = (ImageView)findViewById(R.id.img_cover);
//        Random r = new Random();
//        int i = r.nextInt()%2;
//        switch (i){
//            case 0:
//                cover.setImageResource(R.drawable.img_welcome);
//                break;
//            case  1:
                cover.setImageResource(R.drawable.img_welcome_2);
//                break;
//            case -1:
//                cover.setImageResource(R.drawable.img_welcome_2);
//                break;
//            default:
//                cover.setImageResource(R.drawable.img_welcome);
//                break;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        CountDownTimer timer = new CountDownTimer(5000, 500) {
//            public void onTick(long millisUntilFinished) {
//            }
//            public void onFinish() {
                UserLogic logic = (UserLogic)LogicFactory.self().get(LogicFactory.Type.User);
                logic.loginWithCache( createUIEventListener(new EventListener() {
                        @Override
                        public void onEvent(EventId id, EventArgs args) {
                            OperErrorCode errCode = ((StatusEventArgs) args).getErrCode();
                            switch (errCode) {
                                case Success:
                                    startActivity(new Intent(getActivity(),MainActivity.class));
                                    Alert.Toast("马上一起启程吧！");
                                    finish();
                                    break;
                                default:
                                    startActivity(new Intent(getActivity(),LoginActivity.class));
                                    finish();
                                    break;
                            }
                        }
                    })
                );
//            }
//        }.start();
    }
}
