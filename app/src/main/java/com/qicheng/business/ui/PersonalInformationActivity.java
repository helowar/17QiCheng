package com.qicheng.business.ui;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.module.Label;
import com.qicheng.framework.ui.base.BaseActivity;

import java.util.ArrayList;

public class PersonalInformationActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);

        View view2 = (View) getLayoutInflater().inflate(R.layout.personal_information_collection, null);
        TextView text = (TextView) view2.findViewById(R.id.label_text);
        text.setText("标签兴趣");
        LabelViewGroup labelViewGroup2 = (LabelViewGroup) view2.findViewById(R.id.label_viewGroup);
        labelViewGroup2.addView(setTextViewToGroup("美女"));
        labelViewGroup2.addView(setTextViewToGroup("金融"));
        labelViewGroup2.addView(setTextViewToGroup("白骨精"));
        labelViewGroup2.addView(setTextViewToGroup("北京"));
        labelViewGroup2.addView(setTextViewToGroup("小鲜肉"));
        labelViewGroup2.addView(setTextViewToGroup("我是歌手"));
        labelViewGroup2.addView(setTextViewToGroup("职业歌手职业歌手"));
        linearLayout.addView(view2);


        View photo = (View) getLayoutInflater().inflate(R.layout.personal_information_tabel, null);
        TextView photoTitle = (TextView) photo.findViewById(R.id.table_text);
        photoTitle.setText("我的相册");
        LinearLayout liner = (LinearLayout) photo.findViewById(R.id.table_id);
        View photos = (View) getLayoutInflater().inflate(R.layout.personal_information_photo, null);
        GridView gridView =(GridView)photos.findViewById(R.id.photo_list);

        ListAdapter listAdapter = new PersonalInformationPhotoAdapter(this);
        gridView.setAdapter(listAdapter);
        liner.addView(gridView);
        linearLayout.addView(photo);

        View activity = (View) getLayoutInflater().inflate(R.layout.personal_information_acrivity_tabel, null);
        TextView activityTitle = (TextView) activity.findViewById(R.id.table_text);
        activityTitle.setText("我的动态");
        linearLayout.addView(activity);

        View friend = (View) getLayoutInflater().inflate(R.layout.personal_information_tabel, null);
        TextView friendText = (TextView) friend.findViewById(R.id.table_text);
        friendText.setText("交友资料");
        linearLayout.addView(friend);



        View account = (View) getLayoutInflater().inflate(R.layout.personal_information_tabel, null);
        TextView accountText = (TextView) account.findViewById(R.id.table_text);
        accountText.setText("账户设置");
        linearLayout.addView(account);
    }


    public TextView setTextViewToGroup(String textId) {
        TextView textView = new TextView(this);
        textView.setText(textId);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAppearance(this, R.style.personal_information_font);
        textView.setBackgroundResource(R.drawable.personal_information_text_view_circle);
        return textView;
    }


}
