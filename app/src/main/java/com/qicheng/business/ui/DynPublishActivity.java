package com.qicheng.business.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
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

public class DynPublishActivity extends Activity {
    private RichEdit edit;
    private TextView surplus;
    private ImageView camera, back;
    final int MAX_LENGTH = 170;
    int Rest_Length = MAX_LENGTH;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.dyn_publish);
        edit = (RichEdit) this.findViewById(R.id.msg_write_content);
        surplus = (TextView) this.findViewById(R.id.surplus);
        surplus.setText("您还能输入" + Rest_Length + "个字");
        camera = (ImageView) this.findViewById(R.id.camera);
        //back = (ImageView) this.findViewById(R.id.back);


        camera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

//        back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DynPublishActivity.this.finish();
//            }
//        });

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            try {
                edit.setText(Html.fromHtml(("<img src='" + uri.toString() + "'/>"), imageGetter, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //获取图片缩略图
    private ImageGetter imageGetter = new ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            ContentResolver cr = DynPublishActivity.this.getContentResolver();
            try {
                Uri u = Uri.parse(source);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inPurgeable = true;
                opts.inInputShareable = true;
                opts.inJustDecodeBounds = true;
                opts.inSampleSize = 2;
                opts.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(u), null, opts);
                BitmapDrawable d = new BitmapDrawable(bitmap);
                d.setBounds(0, 0, d.getIntrinsicWidth() / 2, d.getIntrinsicHeight() / 2);
                return d;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                DynPublishActivity.this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}