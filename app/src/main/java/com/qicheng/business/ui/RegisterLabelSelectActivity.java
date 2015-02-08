package com.qicheng.business.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LabelItemPriorityComparator;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LabelPriorityComparator;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.Label;
import com.qicheng.business.module.LabelTypeList;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 标签选择Activity
 */
public class RegisterLabelSelectActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    private Button nextButton;
    private ArrayList<LabelTypeList> labelTypeLists = new ArrayList<LabelTypeList>();
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_select);
        getTagList();


        View view = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        TextView text2 = (TextView) view.findViewById(R.id.label_text);
        text2.setText("歌曲");
        LabelViewGroup labelViewGroup = (LabelViewGroup) view.findViewById(R.id.label_viewGroup);
        for (int i = 0; i < 10; i++) {

            labelViewGroup.addView(setTextViewToGroup("美女"));
        }

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

    /**
     * 获取标签完整列表
     */
    private void getTagList() {
        final LabelLogic labelLogic = (LabelLogic) LogicFactory.self().get(LogicFactory.Type.Label);

        labelLogic.getLabelList(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((LabelEventArgs) args).getErrCode();
                labelTypeLists = ((LabelEventArgs) args).getLabelTypeLists();
                Collections.sort(labelTypeLists, new LabelPriorityComparator());
                for (int i = 0; i < labelTypeLists.size(); i++) {
                    Collections.sort(labelTypeLists.get(i).getTagList(), new LabelItemPriorityComparator());
                }
                Log.d("test", labelTypeLists.toString());

                linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
                for (int i = 0; i < labelTypeLists.size(); i++) {
                    View view2 = (View) getLayoutInflater().inflate(R.layout.layout_label_collection, null);
                    TextView text = (TextView) view2.findViewById(R.id.label_text);
                    LabelTypeList labelType = labelTypeLists.get(i);
                    text.setText(labelType.getName());
                    LabelViewGroup labelViewGroup2 = (LabelViewGroup) view2.findViewById(R.id.label_viewGroup);
                    for (int j = 0; j < labelType.getTagList().size(); j++) {
                        labelViewGroup2.addView(setTextViewToGroup(labelType.getTagList().get(j).getName()));
                    }
                    linearLayout.addView(view2);
                }

                switch (errCode) {
                    case Success:

                        break;
                    default:
                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.verify_code_send_failed_msg));
                        break;
                }
            }
        }));
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
