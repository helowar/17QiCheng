package com.qicheng.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.util.Const;

public class LoginActivity extends BaseActivity {

    private Button mLoginButton;

    private EditText mUserName;

    private EditText mPassWord;

    private TextView mForgetPassword;

    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //获取并保存公钥
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.fetchPublicKey(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((StatusEventArgs) args).getErrCode();
                //可能因网络故障等导致无法获取公钥
                if (errCode != OperErrorCode.Success) {
                    Alert.handleErrCode(errCode);
                }
            }
        }));
        mLoginButton = (Button) findViewById(R.id.button_login);
        mRegisterButton = (Button) findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                finish();
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    login();
                }

            }
        });

        mForgetPassword = (TextView) findViewById(R.id.forget_password);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPasswordUpdateActivity.class);
                intent.putExtra(Const.Intent.UPDATE_PASSWORD_RESOURCE, Const.UPDATE_PWD_FROM_FORGET);
                startActivity(intent);
            }
        });
    }

    /**
     * 验证登录输入非空
     *
     * @return
     */
    private boolean checkInput() {
        mUserName = (EditText) findViewById(R.id.edit_username);
        mPassWord = (EditText) findViewById(R.id.edit_password);
        String userName = mUserName.getText().toString();
        String passWord = mPassWord.getText().toString();
        if (userName.isEmpty()) {
            mUserName.setSelected(true);//获取焦点
            Alert.Toast(R.string.username_is_empty);//显示提示
            return false;
        } else {
            if (passWord.isEmpty()) {
                mPassWord.setSelected(true);//获取焦点
                Alert.Toast(R.string.password_is_empty);//显示提示
                return false;
            }
            return true;
        }
    }

    /**
     * 执行登录过程
     */
    private void login() {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.login(mUserName.getText().toString(), mPassWord.getText().toString(), createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserEventArgs result = (UserEventArgs) args;
                OperErrorCode errCode = result.getErrCode();

                switch (errCode) {
                    case Success:
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("token", result.getResult().getToken());
                        startActivity(intent);
                        finish();
                        break;
                    case UidNoExist:
                        Alert.Toast("用户名不存在");
                        break;
                    case PasswordError:
                        Alert.Toast("密码错误");
                        break;
                    case LocationNotAviable:
                        Alert.Toast("请先开启定位服务后重新启动程序");
                        break;
                    case NetNotAviable:
                        Alert.showNetAvaiable();
                        break;
                    default:
                        Alert.Toast("登录失败");
                        break;
                }
            }
        }));

        startLoading();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
