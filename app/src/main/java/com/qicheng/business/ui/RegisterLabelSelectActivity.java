package com.qicheng.business.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;
import java.util.ArrayList;

public class RegisterLabelSelectActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_select);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);

        View view2 = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        TextView text = (TextView) view2.findViewById(R.id.label_text);
        text.setText("影视");
        LabelViewGroup labelViewGroup2 = (LabelViewGroup) view2.findViewById(R.id.label_viewGroup);
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_pretty));
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_financial));
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_baigujing));
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_beijing));
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_xiaoxianrou));
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_woshigeshou));
        labelViewGroup2.addView(setTextViewToGroup(R.string.label_text_zhiyegeshou));
        linearLayout.addView(view2);

        View view = (View)getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        TextView text2 = (TextView) view.findViewById(R.id.label_text);
        text2.setText("歌曲");
        LabelViewGroup labelViewGroup = (LabelViewGroup) view.findViewById(R.id.label_viewGroup);
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_pretty));
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_financial));
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_baigujing));
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_beijing));
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_xiaoxianrou));
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_woshigeshou));
        labelViewGroup.addView(setTextViewToGroup(R.string.label_text_zhiyegeshou));
        linearLayout.addView(view);

        Button nextButton = (Button)findViewById(R.id.label_button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterLabelSelectActivity.this,RegisterLabelEditActivity.class);
                Log.d(TAG,labels.toString());
                intent.putExtra("labels",labels);
                startActivity(intent);
            }
        });
    }


    public TextView setTextViewToGroup(int textId) {
        TextView textView = new TextView(this);
        textView.setText(textId);
        textView.setTextAppearance(this, R.style.labelStyle);
        textView.setBackgroundResource(R.drawable.label_shape);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Label label = new Label();
                if (!v.isSelected()) {
                    label.setName(((TextView)v).getText().toString());
                    labels.add(label);
                    v.setBackgroundResource(R.drawable.label_select_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    v.setSelected(true);
                } else {
                    v.setBackgroundResource(R.drawable.label_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray_text));
                    v.setSelected(false);
                }
            }
        });
        return textView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_label_select, menu);
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
