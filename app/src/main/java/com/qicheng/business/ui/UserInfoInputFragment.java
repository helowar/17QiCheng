package com.qicheng.business.ui;


import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.Logger;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoInputFragment extends BaseFragment {

    private static final Logger logger = new Logger("UserInfoInputFragment");


    /* 组件 */
    private RelativeLayout switchAvatar;
    private ImageView faceImage;
    private TextView mBirthDate;

    private String[] items = new String[] { "选择本地图片", "拍照" };
    /* 头像名称 */
    private static final String IMAGE_FILE_NAME = "faceImage.jpg";

    /* 请求码 */
    private static final int DATE_REQUEST_CODE = 3;

    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;


    public UserInfoInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("资料");
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_user_info_input, container, false);
        //显示返回按钮
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
            ActionBar actionBar= getActivity().getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        faceImage= (ImageView)fragmentView.findViewById(R.id.img_user_portrait);
        faceImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImgPickDialog();
                }
            });

        mBirthDate = ((TextView)fragmentView.findViewById(R.id.editText_age));
        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickDialog();
            }
        });

        fragmentView.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return fragmentView;
    }

    /**
     * 显示生日选择对话框
     */
    private void showDatePickDialog(){
        FragmentManager fm = getActivity().getFragmentManager();
        DatePickFragment dialog = DatePickFragment.newInstance(null);
        dialog.setDialogTitle("请选择生日");
        dialog.setTargetFragment(this,DATE_REQUEST_CODE);
        dialog.show(fm,"date");
    }

    private void updateBirthDate(Date date){
        StringBuffer dateText  = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dateText.append(calendar.get(Calendar.YEAR));
        dateText.append("年");
        dateText.append(calendar.get(Calendar.MONTH)+1);
        dateText.append("月");
        dateText.append(calendar.get(Calendar.DAY_OF_MONTH));
        dateText.append("日");
        mBirthDate.setText(dateText.toString());
    }

    /**
     * 显示头像选择对话框
     */
    private void showImgPickDialog() {

        new AlertDialog.Builder(this.getActivity())
                .setTitle("设置头像")
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
                                    File file = new File(path,IMAGE_FILE_NAME);
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(file));
                                }

                                startActivityForResult(intentFromCapture,CAMERA_REQUEST_CODE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != Activity.RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path,IMAGE_FILE_NAME);
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
                case DATE_REQUEST_CODE:
                    Date date = (Date)data.getSerializableExtra(DatePickFragment.EXTRA_DATE);
                    updateBirthDate(date);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * 保存裁剪之后的图片数据
     *
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            logger.d("photo size:"+photo.getByteCount()/1000+"K");
            Drawable drawable = new BitmapDrawable(this.getResources(),photo);
            faceImage.setImageDrawable(drawable);
        }
    }

    public boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_register,menu);
    }
}
