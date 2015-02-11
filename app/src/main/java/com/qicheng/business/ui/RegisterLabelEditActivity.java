package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.module.Label;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;

import java.util.ArrayList;
import java.util.List;


public class RegisterLabelEditActivity extends BaseActivity {
    private ArrayList<View> listView = new ArrayList<View>();
    private LabelViewGroup labelViewGroup;
    private final static String TAG = "EditLabel";
    private LinearLayout labelRoot;
    private EditText addEditText;
    private ArrayList<Label> labels = new ArrayList<Label>();
    private List<Label> labelData = new ArrayList<Label>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_edit);
        //描绘自定义布局ViewGroup
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
        View view = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        labelViewGroup = (LabelViewGroup) view.findViewById(R.id.label_viewGroup);
        TextView textTitle = (TextView) view.findViewById(R.id.label_text);
        textTitle.setText(R.string.already_add_label);
        Intent intent = getIntent();
        labels = (ArrayList<Label>) intent.getSerializableExtra("labels");
        if (labels != null && labels.size() > 0) {
            for (int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                //labels.add(label);
                TextView labelText = setTextViewToGroup(label.getItemName());
                labelText.setTag(label);
                labelViewGroup.addView(labelText);
            }
        }

        addEditText = (EditText) findViewById(R.id.edit_label);
        addEditText.setFocusable(true);
        addEditText.requestFocus();
        addEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = addEditText.getText().toString();
                String labelName = text.trim();
                Label addLabel = new Label();
                if (!labelName.equals("")) {
                    for (int i = 0; i < labels.size(); i++) {
                        if (labels.get(i).getItemName().equals(labelName)) {
                            Alert.Toast(R.string.label_not_unique_reject);
                            //按下回车丢失焦点
                            addEditText.clearFocus();
                            //收起软键盘
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(addEditText.getWindowToken(), 0);
                            return false;
                        }
                    }
                    addLabel.setItemName(labelName);
                    labels.add(addLabel);
                    v = (TextView) setTextViewToGroup(labelName);
                    v.setTag(addLabel);
                    labelViewGroup.addView(v);
                    addEditText.setText("");
                    //按下回车丢失焦点
                    addEditText.clearFocus();
                    //收起软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addEditText.getWindowToken(), 0);
                }
                return false;
            }
        });
        Button button = (Button) findViewById(R.id.button_delete);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 删除不愿意被添加的TextView
                 */
                for (int i = 0; i < listView.size(); i++) {
                    labelViewGroup.removeView(listView.get(i));
                }
                /**
                 * 遍历自定义ViewGroup中的并传递数据
                 */
                for (int i = 0; i < labelViewGroup.getChildCount(); i++) {
                    Log.d("re", labelViewGroup.getChildAt(i).getTag().toString());
                    labelData.add((Label) labelViewGroup.getChildAt(i).getTag());
                }
                updateLabelList(labelData);
            }
        });


        linearLayout.addView(view);
    }


    //通过文本文件创建TextView
    public TextView setTextViewToGroup(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextAppearance(this, R.style.labelStyle);
        textView.setBackgroundResource(R.drawable.label_select_shape);
        textView.setTextColor(getResources().getColor(R.color.white));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    v.setBackgroundResource(R.drawable.label_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray_text));
                    v.setSelected(true);
                    listView.add(v);
                } else {
                    v.setBackgroundResource(R.drawable.label_select_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    v.setSelected(false);
                    listView.remove(v);
                }

            }
        });
        return textView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_label_edit, menu);
        ActionBar bar = this.getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //跳转至登录
                //Intent intent = new Intent(this,RegisterLabelSelectActivity.class);
                //startActivity(intent);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateLabelList(List<Label> list) {
        final LabelLogic labelLogic = (LabelLogic) LogicFactory.self().get(LogicFactory.Type.Label);
        labelLogic.checkUpdate(list, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((StatusEventArgs) args).getErrCode();
                switch (errCode) {
                    case Success:
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.label_reject));
                        break;
                }
            }
        }));
    }


}
