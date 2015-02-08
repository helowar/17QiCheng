package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.module.Label;
import com.qicheng.framework.ui.base.BaseActivity;

import java.util.ArrayList;

public class RegisterLabelSelectActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_select);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);

        View view2 = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        TextView text = (TextView) view2.findViewById(R.id.label_text);
        text.setText("影视");

        LabelViewGroup labelViewGroup2 = (LabelViewGroup) view2.findViewById(R.id.label_viewGroup);
        labelViewGroup2.addView(setTextViewToGroup("美女"));
        labelViewGroup2.addView(setTextViewToGroup("金融"));
        labelViewGroup2.addView(setTextViewToGroup("白骨精"));
        labelViewGroup2.addView(setTextViewToGroup("北京"));
        labelViewGroup2.addView(setTextViewToGroup("小鲜肉"));
        labelViewGroup2.addView(setTextViewToGroup("我是歌手"));
        labelViewGroup2.addView(setTextViewToGroup("职业歌手"));
        linearLayout.addView(view2);
        View view = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        TextView text2 = (TextView) view.findViewById(R.id.label_text);
        text2.setText("歌曲");
        LabelViewGroup labelViewGroup = (LabelViewGroup) view.findViewById(R.id.label_viewGroup);
        labelViewGroup.addView(setTextViewToGroup("美女"));
        labelViewGroup.addView(setTextViewToGroup("金融"));
        labelViewGroup.addView(setTextViewToGroup("白骨精"));
        labelViewGroup.addView(setTextViewToGroup("北京"));
        labelViewGroup.addView(setTextViewToGroup("小鲜肉"));
        labelViewGroup.addView(setTextViewToGroup("我是歌手"));
        labelViewGroup.addView(setTextViewToGroup("职业歌手"));
        linearLayout.addView(view);


        nextButton = (Button) findViewById(R.id.label_button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLabelSelectActivity.this, RegisterLabelEditActivity.class);
                Log.d(TAG, labels.toString());
                intent.putExtra("labels", labels);
                startActivity(intent);
            }
        });
    }


    public TextView setTextViewToGroup(String textId) {
        TextView textView = new TextView(this);
        textView.setText(textId);
        textView.setTextAppearance(this, R.style.labelStyle);
        textView.setBackgroundResource(R.drawable.label_shape);
        final Label label = new Label();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    label.setName(((TextView) v).getText().toString());
                    labels.add(label);
                    v.setBackgroundResource(R.drawable.label_select_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    v.setSelected(true);
                    if (labels.size() > 0) {
                        nextButton.setEnabled(true);
                    }
                } else {
                    labels.remove(label);
                    v.setBackgroundResource(R.drawable.label_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray_text));
                    v.setSelected(false);
                    if (labels.size() <= 0) {
                        nextButton.setEnabled(false);
                    }
                }
            }
        });
        return textView;
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
            //Intent intent = new Intent(this,RegisterLabelSelectActivity.class);
            //startActivity(intent);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
