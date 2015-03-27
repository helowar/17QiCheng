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
import com.qicheng.business.module.Label;
import com.qicheng.business.module.LabelType;
import com.qicheng.business.ui.component.LabelViewGroup;
import com.qicheng.framework.ui.base.BaseActivity;

import java.util.ArrayList;

/**
 * 标签选择Activity
 */
public class RegisterLabelSelectActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    private Button nextButton;
    private ArrayList<LabelType> labelTypes;
    private LinearLayout linearLayout;
    public static RegisterLabelSelectActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_select);
        instance = this;
        linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
        //获取测试数据
        Intent intent = getIntent();
        labelTypes = (ArrayList<LabelType>) intent.getSerializableExtra("Label");
        //遍历各个type的标签
        for (int i = 0; i < labelTypes.size(); i++) {
            View view2 = getLayoutInflater().inflate(R.layout.layout_label_collection, null);
            TextView text = (TextView) view2.findViewById(R.id.label_text);
            //获得单个种类的标签
            LabelType labelType = labelTypes.get(i);
            //设置标签类型textview
            text.setText(labelType.getName());
            LabelViewGroup labelViewGroup2 = (LabelViewGroup) view2.findViewById(R.id.label_viewGroup);
            //遍历各个标签，生成TextView添加到viewGroup中
            for (int j = 0; j < labelType.getTagList().size(); j++) {
                TextView labelTextView = setTextViewToGroup(labelType.getTagList().get(j).getName());
                //将标签的类型Id和标签Id添加到textView的Tag中
                String itemId = labelType.getTagList().get(j).getId();
                labelTextView.setTag(itemId);
                labelViewGroup2.addView(labelTextView);
            }
            linearLayout.addView(view2);
        }
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
     * 通过TextName生成TextView
     *
     * @param itemName
     * @return TextView
     */
    public TextView setTextViewToGroup(String itemName) {
        TextView textView = new TextView(this);
        textView.setText(itemName);
        textView.setTextAppearance(this, R.style.labelStyle);
        textView.setBackgroundResource(R.drawable.label_shape);
        //定义临时的类，存储typeId，itemId，itemName
        final Label label = new Label();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果TextView被选择了取出Tag中的数据，添加到list中用于传递到下一级别
                if (!v.isSelected()) {
                    label.setItemName(((TextView) v).getText().toString());
                    String itemId = (String) v.getTag();
                    label.setItemId(itemId);
                    label.setTypeId("1");
                    Log.d("ttt", label.toString());
                    labels.add(label);
                    //替换TextView的样式
                    v.setBackgroundResource(R.drawable.label_select_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    v.setSelected(true);
                    if (labels.size() > 0) {
                        nextButton.setEnabled(true);
                    }
                } else {
                    labels.remove(label);
                    //替换TextView的样式
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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
