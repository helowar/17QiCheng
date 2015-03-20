/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.cache.Cache;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.business.module.UserDetail;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.util.Const;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static com.qicheng.util.Const.Intent.USER_DETAIL_KEY;

public class UserInformationFragment extends BaseFragment {
    private View view;
    private LinearLayout linearLayout;

    private TextView birthdayView;

    private TextView nicknameView;

    private TextView homeView;

    private TextView residenceView;

    private TextView industryView;
    private TextView educationView;

    private String portraitUrl;

    private ImageView portraitView;

    /* 请求码 */
    private static final int DATE_REQUEST_CODE = 3;

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private String[] items = new String[]{"选择本地图片", "拍照"};
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_infomation_modify, container, false);
        initView(inflater);
        return view;
    }

    /**
     * 初始化View视图
     *
     * @param inflater
     */
    public void initView(LayoutInflater inflater) {
        linearLayout = (LinearLayout) view.findViewById(R.id.label_scroll_root);
        UserDetail userDetail =(UserDetail)getActivity().getIntent().getExtras().get(USER_DETAIL_KEY);
        User user = Cache.getInstance().getUser();
          /*个人头像*/
        initPortraitItem(inflater, R.string.personal_portrait_text, user.getPortraitURL());
        /*个人昵称*/
        initViewItem(inflater, R.string.personal_nickname_text, user.getNickName());
        /*出生日期*/
        initViewItem(inflater, R.string.personal_birthday_text, user.getBirthday());

        /*添加分割段*/
        addSeparation(inflater);

        /*行业*/
        initViewItem(inflater, R.string.personal_industry_text, userDetail.getIndustry());
        /*学历*/
        initViewItem(inflater, R.string.personal_education_text, userDetail.getEducation());
        /*所在地*/
        initViewItem(inflater, R.string.personal_residence_text, userDetail.getResidence());
        /*家乡*/
        initViewItem(inflater, R.string.personal_home_text, userDetail.getHometown());
        /*添加分割段*/
        addSeparation(inflater);

         /*手机号码*/
        initViewItem(inflater, R.string.personal_cell_text, user.getCellNum());


    }

    /**
     * 创建menu元素并加入到布局文件中
     *
     * @param inflater
     * @param stringID 字符串id
     * @param text     icon id
     */

    public void initViewItem(LayoutInflater inflater, final int stringID, final String text) {
        View view = inflater.inflate(R.layout.personal_information_text_tabel, null);
        TextView viewText = (TextView) view.findViewById(R.id.table_text);
        viewText.setText(stringID);
        final TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        /*绑定点击事件*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = (TextView) view.findViewById(R.id.text);
                Intent intent = new Intent(getActivity(), UserInformationModifyActivity.class);
                switch (stringID) {
                    case R.string.personal_birthday_text:
                        showDatePickDialog(textView.getText().toString());
                        birthdayView = textView;
                        break;
                    case R.string.personal_nickname_text:
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TYPE, Const.UserUpdateCode.UPDATE_NICKNAME);
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_VALUE, textView.getText().toString());
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TITLE, getResources().getString(R.string.title_activity_user_nickname_modify));
                        startActivityForResult(intent, Const.UserUpdateCode.UPDATE_NICKNAME);
                        nicknameView = textView;
                        break;
                    case R.string.personal_home_text:
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TYPE, Const.UserUpdateCode.UPDATE_HOMETOWN);
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_VALUE, textView.getText().toString());
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TITLE, getResources().getString(R.string.title_activity_user_hometown_modify));
                        startActivityForResult(intent, Const.UserUpdateCode.UPDATE_HOMETOWN);
                        homeView = textView;
                        break;
                    case R.string.personal_industry_text:
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TYPE, Const.UserUpdateCode.UPDATE_INDUSTRY);
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_VALUE, textView.getText().toString());
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TITLE, getResources().getString(R.string.title_activity_user_industry_modify));
                        startActivityForResult(intent, Const.UserUpdateCode.UPDATE_INDUSTRY);
                        industryView = textView;
                        break;
                    case R.string.personal_residence_text:
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TYPE, Const.UserUpdateCode.UPDATE_RESIDENCE);
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_VALUE, textView.getText().toString());
                        intent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TITLE, getResources().getString(R.string.title_activity_user_residence_modify));
                        startActivityForResult(intent, Const.UserUpdateCode.UPDATE_RESIDENCE);
                        residenceView = textView;
                        break;
                    case R.string.personal_education_text:
                        Intent eduIntent = new Intent(getActivity(), SelectEducationActivity.class);
                        eduIntent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TYPE, Const.UserUpdateCode.UPDATE_EDUCATION);
                        eduIntent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_VALUE, textView.getText().toString());
                        eduIntent.putExtra(Const.Intent.UPDATE_USER_INFORMATION_TITLE, getResources().getString(R.string.title_activity_user_education_modify));
                        startActivityForResult(eduIntent, Const.UserUpdateCode.UPDATE_EDUCATION);
                        educationView = textView;
                        break;
                    default:
                        break;
                }
            }
        });
        linearLayout.addView(view);
    }

    private void addSeparation(LayoutInflater inflater) {
        View separation = inflater.inflate(R.layout.personal_information_separation, null);
        linearLayout.addView(separation);
    }

    /**
     * 初始化头像修改item
     *
     * @param inflater
     * @param stringID
     * @param url
     */
    private void initPortraitItem(LayoutInflater inflater, final int stringID, String url) {
        View view = inflater.inflate(R.layout.personal_information_portrait_tabel, null);
        TextView viewText = (TextView) view.findViewById(R.id.table_text);
        viewText.setText(stringID);
        portraitView = (ImageView) view.findViewById(R.id.portrait);
        ImageManager.displayPortrait(url, portraitView);
        /*绑定点击事件*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImgPickDialog();
            }
        });
        linearLayout.addView(view);
    }


    /**
     * 显示生日选择对话框
     */
    private void showDatePickDialog(String date) {
        FragmentManager fm = getActivity().getFragmentManager();
        DatePickFragment dialog = DatePickFragment.newInstance(DateTimeUtil.parseByyyyyMMdd10(date));
        dialog.setDialogTitle(getResources().getString(R.string.title_activity_user_birthday_modify));
        dialog.setTargetFragment(this, DATE_REQUEST_CODE);
        dialog.show(fm, "date");
    }


    /**
     * 更新生日
     *
     * @param date
     * @return
     */
    private String updateBirthDate(Date date) {
        StringBuffer dateText = new StringBuffer();
        StringBuffer birthday = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dateText.append(calendar.get(Calendar.YEAR));
        birthday.append(calendar.get(Calendar.YEAR));
        dateText.append("-");
        dateText.append(calendar.get(Calendar.MONTH) + 1);
        if (Calendar.MONTH + 1 < 10) {
            birthday.append("0" + (calendar.get(Calendar.MONTH) + 1));
        } else {
            birthday.append((calendar.get(Calendar.MONTH) + 1));
        }
        dateText.append("-");
        dateText.append(calendar.get(Calendar.DAY_OF_MONTH));
        birthday.append(calendar.get(Calendar.DAY_OF_MONTH));
        return dateText.toString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("code", requestCode + " " + resultCode);
        //当没有data时直接返回
        if (data == null) {
            return;
        }

        //处理其他更改信息的
        switch (resultCode) {
            case Const.UserUpdateCode.UPDATE_NICKNAME:
                String nickname = data.getStringExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT);
                nicknameView.setText(nickname);
                Cache.getInstance().getUser().setNickName(nickname);
                Cache.getInstance().refreshCacheUser();
                break;
            case Const.UserUpdateCode.UPDATE_HOMETOWN:
                homeView.setText(data.getStringExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT));
                break;
            case Const.UserUpdateCode.UPDATE_RESIDENCE:
                residenceView.setText(data.getStringExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT));
                break;
            case Const.UserUpdateCode.UPDATE_INDUSTRY:
                industryView.setText(data.getStringExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT));
                break;
            case Const.UserUpdateCode.UPDATE_EDUCATION:
                educationView.setText(data.getStringExtra(Const.Intent.UPDATE_USER_INFORMATION_RESULT));
                break;
        }
        //特殊处理修改生日的逻辑
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case DATE_REQUEST_CODE:
                    Date date = (Date) data.getSerializableExtra(DatePickFragment.EXTRA_DATE);
                    updateUserInformation(Const.UserUpdateCode.UPDATE_BIRTHDAY, DateTimeUtil.formatByyyyyMMdd(date), date);
                    break;
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                    } else {
                        Alert.Toast("未找到存储卡，无法存储照片！");
                    }
                    break;
                case RESULT_REQUEST_CODE: //图片缩放完成后
                    if (data != null) {
                        getImageToView(data);
                    }
                    break;
            }
            return;
        }
        // }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 更新用户生日信息
     *
     * @param updateType
     * @param updateValue
     * @param date
     */
    private void updateUserInformation(int updateType, String updateValue, final Date date) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.updateUserInformation(updateType, updateValue, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs userEventArgs = (UserEventArgs) args;
                OperErrorCode errCode = userEventArgs.getErrCode();
                switch (errCode) {
                    case Success:
                        String birthday = updateBirthDate(date);
                        birthdayView.setText(birthday);
                        Cache.getInstance().getUser().setBirthday(birthday);
                        Cache.getInstance().refreshCacheUser();
                        break;
                }
            }
        }));
    }

    /**
     * 更新用户头像信息
     *
     * @param updateType
     * @param updateValue
     */
    private void updateUserInformation(int updateType, final String updateValue) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.updateUserInformation(updateType, updateValue, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs userEventArgs = (UserEventArgs) args;
                OperErrorCode errCode = userEventArgs.getErrCode();
                switch (errCode) {
                    case Success:
                        Cache.getInstance().getUser().setPortraitURL(Const.BASE_URL+updateValue);
                        Cache.getInstance().refreshCacheUser();
                        break;
                }
            }
        }));
    }

    /**
     * 显示头像选择对话框
     */
    private void showImgPickDialog() {

        new AlertDialog.Builder(this.getActivity())
                .setTitle(getResources().getString(R.string.title_activity_user_portrait_modify))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery,
                                        IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (hasSdcard()) {
                                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                    File file = new File(path, IMAGE_FILE_NAME);
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(file));
                                }
                                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    /**
     * 判断是否有内存卡
     *
     * @return
     */
    public boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存并上传
     *
     * @param photo
     */
    private void savePortrait(Bitmap photo) {
        UserLogic userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
        userLogic.saveUserPortrait(photo, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserEventArgs result = (UserEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                switch (errCode) {
                    case Success:
                        portraitUrl = result.getResult().getPortraitURL();
                        updateUserInformation(Const.UserUpdateCode.UPDATE_PORTRAIT_URL, portraitUrl);
                        break;
                    case FileUpLoadFailed:
                        Alert.Toast(getResources().getString(R.string.portrait_save_failed));
                        break;
                    default:
                        Alert.Toast(getResources().getString(R.string.portrait_save_failed));
                        break;
                }
            }
        }));
        startLoading();
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            savePortrait(photo);
//            logger.d("photo size:"+photo.getByteCount()/1000+"K");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            portraitView.setImageDrawable(drawable);

        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }



}
