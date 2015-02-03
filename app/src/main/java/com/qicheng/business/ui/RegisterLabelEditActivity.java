package com.qicheng.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.module.Label;
import com.qicheng.framework.ui.base.BaseActivity;

import java.util.ArrayList;


public class RegisterLabelEditActivity extends BaseActivity {
    private ArrayList<View> listView = new ArrayList<View>();
    private LabelViewGroup labelViewGroup;
    private final static String TAG = "EditLabel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_edit);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
        View view = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        labelViewGroup = (LabelViewGroup) view.findViewById(R.id.label_viewGroup);
        TextView text2 = (TextView) view.findViewById(R.id.label_text);
        text2.setText("已选标签");
        Intent intent = getIntent();
        ArrayList<Label> labels = (ArrayList<Label>) intent.getSerializableExtra("labels");
        Log.d(TAG, labels.toString());
        if (labels != null && labels.size() > 0) {
            for (int i = 0; i < labels.size(); i++) {
                labelViewGroup.addView(setTextViewToGroup(labels.get(i).getName()));
            }
        }


        final EditText addEditText = (EditText) findViewById(R.id.edit_label);
        addEditText.setFocusable(true);
        addEditText.requestFocus();
        addEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = addEditText.getText().toString();
                if (!text.trim().equals("")) {
                    v = setTextViewToGroup(text);
                    labelViewGroup.addView(v);
                    addEditText.setText("");
                }
                return false;
            }
        });
        Button button = (Button) findViewById(R.id.button_delete);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listView.size(); i++) {
                    labelViewGroup.removeView(listView.get(i));
                }
            }
        });


        linearLayout.addView(view);
    }


    public TextView setTextViewToGroup(String text) {

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextAppearance(this, R.style.labelStyle);
        textView.setBackgroundResource(R.drawable.label_shape);
        if (!textView.isSelected()) {
            listView.add(textView);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    v.setBackgroundResource(R.drawable.label_select_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    v.setSelected(true);
                    listView.remove(v);

                } else {
                    v.setBackgroundResource(R.drawable.label_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray_text));
                    v.setSelected(false);
                    listView.add(v);
                }

            }
        });
        return textView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_label_edit, menu);
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
