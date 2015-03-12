package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.Label;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;
import com.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class PersonalInformationActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    private Button nextButton;
    private SlidingMenu menu;

    private View view;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(getLayoutInflater());
        setContentView(view);
    }

    /**
     * 初始化View视图
     *
     * @param inflater
     */
    public void initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.activity_personal_information, null);
        linearLayout = (LinearLayout) view.findViewById(R.id.label_scroll_root);
          /*个人头像*/
        initPortraitItem(inflater, R.string.personal_portrait_text, R.drawable.ic_test_img);
        /*个人昵称*/
        initViewItem(inflater, R.string.personal_nickname_text, R.drawable.ic_personal);
        /*性别*/
        initViewItem(inflater, R.string.personal_gender_text, R.drawable.ic_personal);
        /*工作*/
        initViewItem(inflater, R.string.personal_job_text, R.drawable.ic_personal);
        /*家乡*/
        initViewItem(inflater, R.string.personal_home_text, R.drawable.ic_fliter);

    }

    /**
     * 创建menu元素并加入到布局文件中
     *
     * @param inflater
     * @param stringID 字符串id
     * @param resId    icon id
     */

    public void initViewItem(LayoutInflater inflater, final int stringID, int resId) {
        View view = inflater.inflate(R.layout.personal_information_tabel, null);
        TextView viewText = (TextView) view.findViewById(R.id.table_text);
        viewText.setText(stringID);
        ImageView personalImg = (ImageView) view.findViewById(R.id.table_img);
        personalImg.setImageResource(resId);
        /*绑定点击事件*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stringID) {
                    case R.string.personal:
                        /*跳转到个人资料页面*/
                        break;
                    case R.string.my_label:
                        /*跳转到我的标签*/
                        getUserLabel();
                        break;
                    case R.string.my_photo:
                       /*跳转到我的相册*/
                        break;
                    case R.string.my_activity:
                        /*跳转到我的动态*/
                        getUserDyn();
                        break;
                    case R.string.select_setting:
                         /*跳转到筛选设置*/
                        break;
                    case R.string.account_setting:
                       /*跳转到账户设置*/
                        break;
                    default:
                        break;
                }
            }
        });
        linearLayout.addView(view);
    }


    /**
     * 初始化头像修改item
     * @param inflater
     * @param stringID
     * @param resId
     */
   private void initPortraitItem(LayoutInflater inflater, final int stringID, int resId){
       View view = inflater.inflate(R.layout.personal_information_portrait_tabel, null);
       TextView viewText = (TextView) view.findViewById(R.id.table_text);
       viewText.setText(stringID);

       ImageView personalPortraitImg = (ImageView) view.findViewById(R.id.portrait);
       personalPortraitImg.setImageResource(resId);
        /*绑定点击事件*/
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               switch (stringID) {
                   case R.string.personal:
                        /*跳转到个人资料页面*/
                       break;
                   case R.string.my_label:
                        /*跳转到我的标签*/
                       getUserLabel();
                       break;
                   case R.string.my_photo:
                       /*跳转到我的相册*/
                       break;
                   case R.string.my_activity:
                        /*跳转到我的动态*/
                       getUserDyn();
                       break;
                   case R.string.select_setting:
                         /*跳转到筛选设置*/
                       break;
                   case R.string.account_setting:
                       /*跳转到账户设置*/
                       break;
                   default:
                       break;
               }
           }
       });
       linearLayout.addView(view);
    }
    public void getUserLabel() {
        LabelLogic labelLogic = (LabelLogic) LogicFactory.self().get(LogicFactory.Type.Label);
        labelLogic.getUserLabel(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                LabelEventArgs labelEventArgs = (LabelEventArgs) args;
                OperErrorCode errCode = labelEventArgs.getErrCode();
                switch (errCode) {
                    case Success:
                        Intent intent = new Intent(getActivity(), LabelModifyActivity.class);
                        intent.putExtra("Labels", labelEventArgs.getLabel());
                        startActivity(intent);
                        break;
                }
            }
        }));
    }

    private void getUserDyn() {
        Intent intent = new Intent(getActivity(), ToDynActivity.class);
        intent.putExtra(Const.Intent.DYN_QUERY_TYPE, Const.QUERY_TYPE_MY);
        intent.putExtra(Const.Intent.DYN_QUERY_NAME, "我的动态");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register_label_select, menu);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
