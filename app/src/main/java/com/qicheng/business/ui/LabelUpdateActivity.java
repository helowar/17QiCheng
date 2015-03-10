package com.qicheng.business.ui;

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
import com.qicheng.business.module.LabelItem;
import com.qicheng.business.module.LabelType;
import com.qicheng.business.ui.component.LabelViewGroup;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;

import java.util.ArrayList;
import java.util.List;

public class  LabelUpdateActivity extends BaseActivity {
    /*
    用户存储选中的View
     */
    private ArrayList<View> listView = new ArrayList<View>();
    /*
    用于保存需要更新的标签
     */
    private ArrayList<Label> labels = new ArrayList<Label>();
    /*
    提交按钮
     */
    private Button commitButton;
    /*
    传递过来的已选标签和自定义标签
     */
    private ArrayList<LabelType> labelTypes;
    /*
    布局
     */
    private LinearLayout linearLayout;
    /*
    文本编辑框
     */
    private EditText addEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_label);
        initLayOut();
    }


    private void initLayOut() {
        linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
        //获取测试数据
        Intent intent = getIntent();
        labelTypes = (ArrayList<LabelType>) intent.getSerializableExtra("Labels");
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
                LabelItem item = labelType.getTagList().get(j);
                TextView labelTextView = setTextViewToGroup(item.getName(), item.getIsSelected());   //将标签的类型Id和标签Id添加到textView的Tag中
                Label label = new Label();
                //如果label的id不为空，说明它是选择标签，负责是自定义标签
                if (item.getId() != null) {
                    label.setItemId(item.getId());
                    label.setItemName(item.getName());
                    label.setTypeId("type");
                } else {
                    label.setItemName(item.getName());
                }
                //为textView设置Tag
                labelTextView.setTag(label);
                labelViewGroup2.addView(labelTextView);
            }
            linearLayout.addView(view2);
        }
        /*操作输入框*/
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
                    for (int i = 0; i < listView.size(); i++) {
                        View view = listView.get(i);
                        Label label = (Label) view.getTag();
                        if (label.getItemName().equals(labelName)) {
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
                    v = (TextView) setTextViewToGroup(labelName, 1);
                    v.setTag(addLabel);
                    View view = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
                    LabelViewGroup personalViewGroup = (LabelViewGroup) view.findViewById(R.id.label_viewGroup);
                    personalViewGroup.addView(v);
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

        /*操作提交修改按钮*/
        commitButton = (Button) findViewById(R.id.button_commit);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = null;
                int checkSelect = 0;
                for (int i = 0; i < listView.size(); i++) {
                    view = listView.get(i);
                    Label label = (Label) view.getTag();
                    //labels.add(label);
                    if (label.getTypeId() != null) {
                        checkSelect++;
                    }
                }

                if (checkSelect >= 3) {
                    for (int i = 0; i < listView.size(); i++) {
                        view = listView.get(i);
                        Label label = (Label) view.getTag();
                        labels.add(label);
                    }
                    updateLabelList(labels);
                } else {
                    Alert.Toast(R.string.check_label);
                }
            }
        });
    }


    public void updateLabelList(List<Label> list) {
        final LabelLogic labelLogic = (LabelLogic) LogicFactory.self().get(LogicFactory.Type.Label);
        labelLogic.checkUpdate(list, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((StatusEventArgs) args).getErrCode();
                switch (errCode) {
                    case Success:
                        Intent intent = new Intent(getActivity(), LabelModifyActivity.class);
                        ArrayList<LabelItem> labelItems = new ArrayList<LabelItem>();
                        for (int i = 0; i < labels.size(); i++) {
                            Label label = labels.get(i);
                            LabelItem labelItem = new LabelItem();
                            labelItem.setId(label.getItemId());
                            labelItem.setName(label.getItemName());
                            labelItems.add(labelItem);
                        }
                        intent.putExtra("Labels", labelItems);
                        startActivity(intent);
                        finish();
                        break;
                    default:
//                        Alert.handleErrCode(errCode);
                        Alert.Toast(getResources().getString(R.string.label_reject));
                        break;
                }
            }
        }));
    }

    /**
     * 通过TextName生成TextView
     *
     * @param itemName
     * @return TextView
     */
    public TextView setTextViewToGroup(String itemName, int isSelected) {
        TextView textView = new TextView(this);
        textView.setText(itemName);
        if (isSelected == 1) {
            textView.setSelected(true);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.label_select_shape);
            listView.add(textView);
        } else {
            textView.setTextAppearance(this, R.style.labelStyle);
            textView.setBackgroundResource(R.drawable.label_shape);
        }
        Log.d("view", listView.toString());
        //定义临时的类，存储typeId，itemId，itemName
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果TextView被选择了取出Tag中的数据，添加到list中用于传递到下一级别
                if (!v.isSelected()) {
                    listView.add(v);
                    v.setBackgroundResource(R.drawable.label_select_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    v.setSelected(true);
                    if (listView.size() > 0) {
                        commitButton.setEnabled(true);
                    }
                } else {
                    listView.remove(v);
                    //替换TextView的样式
                    v.setBackgroundResource(R.drawable.label_shape);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray_text));
                    v.setSelected(false);
                    if (listView.size() <= 0) {
                        commitButton.setEnabled(false);
                    }
                }
            }
        });
        return textView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_label, menu);
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
