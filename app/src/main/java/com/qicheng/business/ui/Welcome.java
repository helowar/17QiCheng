package com.qicheng.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;

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

public class Welcome extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CountDownTimer timer = new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
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
            }
        }.start();
    }
}
