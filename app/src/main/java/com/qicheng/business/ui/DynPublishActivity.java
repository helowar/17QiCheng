package com.qicheng.business.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html.ImageGetter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.DynLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.DynEventAargs;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.DynFile;
import com.qicheng.business.ui.component.DynSearch;
import com.qicheng.business.ui.component.RichEdit;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.BitmapUtils;
import com.qicheng.framework.util.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DynPublishActivity extends BaseActivity {
    private RichEdit edit;
    private TextView surplus;
    private ImageView camera, picture, publish;
    private DynSearch dynSearch;
    private static Logger logger = new Logger("com.qicheng.business.protocol.DynPublishActivity");

    private String[] items = new String[]{"选择本地图片", "拍照"};

    final int MAX_LENGTH = 170;
    int Rest_Length = MAX_LENGTH;

    /*  动态图片名称 */
    private static final String IMAGE_FILE_NAME = "dynImage.jpg";

    private Byte queryType;
    private String queryValue;
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int DATE_REQUEST_CODE = 3;

    private static final int ADD_SUCCESS = 0;
    private Bitmap bitmap;

    private Uri uri;

    private Bitmap uploadBitmap;

    private String dynPictureUrl;

    private boolean flag;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.dyn_publish);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            queryType = bundle.getByte("query_type");
            queryValue = getIntent().getExtras().getString("query_value");
            logger.d(queryType + " " + queryValue);
        }
        edit = (RichEdit) this.findViewById(R.id.msg_write_content);
        surplus = (TextView) this.findViewById(R.id.surplus);
        surplus.setText("您还能输入" + Rest_Length + "个字");
        camera = (ImageView) this.findViewById(R.id.camera);
        picture = (ImageView) this.findViewById(R.id.activity_pic);
        publish = (ImageView) this.findViewById(R.id.publish);
        //back = (ImageView) this.findViewById(R.id.back);


        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showImgPickDialog();
            }
        });
        /*当编辑框发生改变时的监听器*/
        edit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Rest_Length > 0) {
                    Rest_Length = MAX_LENGTH - edit.getText().length();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                surplus.setText("您还能输入" + Rest_Length + "个字");

            }

            public void afterTextChanged(Editable s) {
                surplus.setText("您还能输入" + Rest_Length + "个字");
            }
        });

        /*发布动态*/
        publish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoading();
                if (flag) {
                    saveDynPictuer(bitmap);

                } else {
                    DynBody body = new DynBody();
                    addDyn(body);
                }

            }
        });
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    uri = data.getData();
                    showPic(uri);
                    break;
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File tempFile = new File(path, IMAGE_FILE_NAME);
                        uri = Uri.fromFile(tempFile);
                        showPic(uri);

                    } else {
                        Alert.Toast("未找到存储卡，无法存储照片！");
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*显示缩略图*/
    private void showPic(Uri uri) {
        Log.e("uri", uri.toString());
        try {
            picture.setImageDrawable(imageGetter.getDrawable(uri.toString()));
            picture.setVisibility(View.VISIBLE);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //获取图片缩略图
    private ImageGetter imageGetter = new ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            ContentResolver cr = DynPublishActivity.this.getContentResolver();
            try {
                logger.d(source);
                bitmap= BitmapUtils.getThumbUploadPath(cr, source, 1000);
                BitmapDrawable d = new BitmapDrawable(bitmap);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };


    /**
     * 保存并上传
     *
     * @param photo
     */
    private void saveDynPictuer(Bitmap photo) {
        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.saveDynPicture(photo, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                UserEventArgs result = (UserEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                switch (errCode) {
                    case Success:
                        logger.d("Dyn file url:" + result.getResult().getPortraitURL());
                        dynPictureUrl = result.getResult().getPortraitURL();
                        DynBody body = new DynBody();
                        List<DynFile> files = new ArrayList<DynFile>();
                        DynFile file = new DynFile();
                        file.setFileUrl(dynPictureUrl);
                        files.add(file);
                        body.setFiles(files);
                        addDyn(body);
                        break;
                    case FileUpLoadFailed:
                        Alert.Toast("动态图片上传失败");
                        break;
                    default:
                        Alert.Toast("动态图片上传失败");
                        break;
                }
            }
        }));
    }

    private void addDyn(DynBody body) {
        if (queryType != null) {
            body.setQueryType(queryType);
            body.setQueryValue(queryValue);
        }
        body.setContent(edit.getText().toString());
        DynLogic dynLogic = (DynLogic) LogicFactory.self().get(LogicFactory.Type.Dyn);
        dynLogic.addDyn(body, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                DynEventAargs dynEventAargs = (DynEventAargs) args;
                OperErrorCode errCode = dynEventAargs.getErrCode();
                switch (errCode) {
                    case Success:
                        sentAnswerResult(true);
                        break;
                    case ResultNotPermit:
                        Alert.Toast("请先添加行程");
                }
            }
        }));
    }


    private void sentAnswerResult(boolean addSuccess) {
        Intent intent = new Intent();
        setResult(ADD_SUCCESS, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DynPublishActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示动态图片选择对话框
     */
    private void showImgPickDialog() {

        new AlertDialog.Builder(this)
                .setTitle("添加动态图片")
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

    public boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }






    /**
     * 发布动态要传给后台的参数
     */
    public class DynBody {
        /*
        动态内容
         */
        private String content;
        /*
        动态类型
         */
        private byte type;
        /*
        是否匿名
         */
        private byte isAnms;
        /*
        行程id
         */
        private String tripId;
        /*
        查询类型
         */
        private Byte queryType;
        /*
        查询值
         */
        private String queryValue;
        /*
        文件数组
         */
        private List<DynFile> files;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public byte getType() {
            return type;
        }

        public void setType(byte type) {
            this.type = type;
        }

        public byte getIsAnms() {
            return isAnms;
        }

        public void setIsAnms(byte isAnms) {
            this.isAnms = isAnms;
        }

        public String getTripId() {
            return tripId;
        }

        public void setTripId(String tripId) {
            this.tripId = tripId;
        }


        public String getQueryValue() {
            return queryValue;
        }

        public void setQueryValue(String queryValue) {
            this.queryValue = queryValue;
        }

        public List<DynFile> getFiles() {
            return files;
        }

        public void setFiles(List<DynFile> files) {
            this.files = files;
        }

        public Byte getQueryType() {
            return queryType;
        }

        public void setQueryType(Byte queryType) {
            this.queryType = queryType;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("DynBody{");
            sb.append("content='").append(content).append('\'');
            sb.append(", type=").append(type);
            sb.append(", isAnms=").append(isAnms);
            sb.append(", tripId='").append(tripId).append('\'');
            sb.append(", queryType=").append(queryType);
            sb.append(", queryValue='").append(queryValue).append('\'');
            sb.append(", files=").append(files);
            sb.append('}');
            return sb.toString();
        }
    }

}