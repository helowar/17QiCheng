package com.qicheng.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.util.Const;

import java.util.Date;

/**
 * @author 金玉龙
 *         功能描述：列表Fragment，用来显示列表视图
 */
public class TopMenuFragment extends BaseFragment {
    private View view;
    private LinearLayout linearLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater);
        return view;
    }


    /**
     * 初始化View视图
     *
     * @param inflater
     */
    public void initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.menu_list, null);
        User user = Cache.getInstance().getUser();
        ImageView portrait = (ImageView) view.findViewById(R.id.personal_information_person_img);
        ImageManager.displayPortrait(user.getPortraitURL(), portrait);
        TextView nicknameView = (TextView) view.findViewById(R.id.personal_information_nickname);
        String nickname = user.getNickName();
        nicknameView.setText(nickname);
        if (user.getGender() == 1) {
            ImageView gender = (ImageView) view.findViewById(R.id.gender);
            gender.setImageResource(R.drawable.ic_male);
        }
        String birthday = user.getBirthday();
        Date birthdayDate = DateTimeUtil.parseByyyyyMMdd10(birthday);
        String age = DateTimeUtil.getAge(birthdayDate);
        TextView ageView = (TextView) view.findViewById(R.id.age);
        ageView.setText(age + "岁");

        linearLayout = (LinearLayout) view.findViewById(R.id.label_scroll_root);
          /*个人资料menu*/
        initViewItem(inflater, R.string.personal, R.drawable.ic_personal);
        /*我的标签menu*/
        initViewItem(inflater, R.string.my_label, R.drawable.ic_personal);
        /*我的相册menu*/
        initViewItem(inflater, R.string.my_photo, R.drawable.ic_personal);
        /*我的动态menu*/
        initViewItem(inflater, R.string.my_activity, R.drawable.ic_fliter);
         /*账户设置menu*/
        initViewItem(inflater, R.string.account_setting, R.drawable.ic_account_setting);

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
                        skipToActivity(UserInfoActivity.class);
                        break;
                    case R.string.my_label:
                        /*跳转到我的标签*/
                        getUserLabel();
                        break;
                    case R.string.my_photo:
                       /*跳转到我的相册*/
                        skipToActivity(AlbumActivity.class);
                        break;
                    case R.string.my_activity:
                        /*跳转到我的动态*/
                        getUserDyn();
                        break;
                    case R.string.account_setting:
                       /*跳转到账户设置*/
                        skipToActivity(UserSettingActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
        linearLayout.addView(view);
    }

    /**
     * 获取用户标签
     */
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

    /**
     * 跳转到我的动态
     */
    private void getUserDyn() {
        Intent intent = new Intent(getActivity(), ToDynActivity.class);
        intent.putExtra(Const.Intent.DYN_QUERY_TYPE, Const.QUERY_TYPE_MY);
        intent.putExtra(Const.Intent.DYN_QUERY_NAME, getResources().getString(R.string.my_activity));
        startActivity(intent);
    }

    /**
     * 跳转到用户信息
     */
    private void skipToActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(),cls);
        startActivity(intent);
    }
}
