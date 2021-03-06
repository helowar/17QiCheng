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
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.logic.event.UserDetailEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.module.UserDetail;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import static com.qicheng.util.Const.Intent.USER_DETAIL_KEY;

/**
 * @author 金玉龙
 *         功能描述：列表Fragment，用来显示列表视图
 */
public class TopMenuFragment extends BaseFragment {
    private View view;
    private LinearLayout linearLayout;

    private static final int UPDATE_USER_INFORMATION = 0;

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
        getUserInformation();
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
        /*检查新版本menu*/
        initViewItem(inflater, R.string.update_check, R.drawable.ic_update);


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
                        startUserInfoActivity(null, PersonalInformationActivity.class);
                        break;
                    case R.string.my_label:
                        /*跳转到我的标签*/
                        getUserLabel();
                        break;
                    case R.string.my_photo:
                       /*跳转到我的相册*/
                        startUserInfoActivity(null, AlbumActivity.class);
                        break;
                    case R.string.my_activity:
                        /*跳转到我的动态*/
                        getUserDyn();
                        break;
                    case R.string.account_setting:
                       /*跳转到账户设置*/
                        skipToActivity(UserSettingActivity.class);
                        break;
                    case R.string.update_check:
                        /*新版本检查*/
                        UmengUpdateAgent.forceUpdate(getActivity());
                        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                            @Override
                            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                                switch (i) {
                                    case UpdateStatus.No: // has no update
                                        Alert.Toast(R.string.no_update);
                                        break;
                                    case UpdateStatus.Timeout: // time out
                                        Alert.Toast(R.string.update_time_out);
                                        break;
                                }
                            }
                        });
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
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        getUserInformation();
        super.onResume();
    }

    private void getUserInformation() {
        User user = Cache.getInstance().getUser();
        ImageView portrait = (ImageView) view.findViewById(R.id.personal_information_person_img);
        String portraitUrl = user.getPortraitURL();
        ImageManager.displayPortrait(portraitUrl, portrait);
        TextView nicknameView = (TextView) view.findViewById(R.id.personal_information_nickname);
        String nickname = user.getNickName();
        nicknameView.setText(nickname);
        if (user.getGender() == 1) {
            ImageView gender = (ImageView) view.findViewById(R.id.gender);
            gender.setImageResource(R.drawable.ic_male);
        }
        String birthday = user.getBirthday();
        TextView ageView = (TextView) view.findViewById(R.id.age);
        setAge(ageView, birthday);
    }


    /**
     * 设置年龄TextView的文本值。
     *
     * @param ageTextView 年龄TextView对象
     * @param birthday    生日字符串（yyyy-MM-dd）
     */
    private void setAge(TextView ageTextView, String birthday) {
        if (StringUtil.isEmpty(birthday)) {
            ageTextView.setText(R.string.secret_text);
        } else {
            String age = DateTimeUtil.getAge(birthday);
            if (StringUtil.isEmpty(age)) {
                ageTextView.setText(R.string.secret_text);
            } else {
                ageTextView.setText(age + getResources().getString(R.string.age_text));
            }
        }
    }

    /**
     * 迁移到用户详细信息页面。
     *
     * @param userId 用户ID
     */
    private void startUserInfoActivity(String userId, final Class cls) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.getUserDetail(userId, Const.ID_TYPE_USER_ID, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserDetailEventArgs result = (UserDetailEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    UserDetail userDetail = result.getUserDetail();
                    Intent intent = new Intent(getActivity(), cls);
                    intent.putExtra(USER_DETAIL_KEY, userDetail);
                    startActivity(intent);
                }
            }
        }));
        startLoading();
    }


}
