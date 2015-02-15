package com.qicheng.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.Label;
import com.qicheng.business.module.LabelItem;
import com.qicheng.business.module.LabelType;
import com.qicheng.business.ui.component.LabelViewGroup;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LabelModifyActivity extends BaseActivity {

    private LinearLayout linearLayout;
    private ArrayList<LabelType> labelTypes;
    private Button modifyButton;
    private ArrayList<LabelItem> selectedLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_modify);

        initLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_label_modify, menu);
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

    /**
     * 按返回键退到后台，不关闭程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initLayout() {
        linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
        //获取测试数据
        Intent intent = getIntent();
        selectedLabel = (ArrayList<LabelItem>) intent.getSerializableExtra("Labels");
        //遍历各个type的标签
        View selectView = getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        LabelViewGroup selectLabelViewGroup = (LabelViewGroup) selectView.findViewById(R.id.label_viewGroup);

        View personalView = getLayoutInflater().inflate(R.layout.layout_label_collection, null);
        LabelViewGroup personalLabelViewGroup = (LabelViewGroup) personalView.findViewById(R.id.label_viewGroup);
        for (int i = 0; i < selectedLabel.size(); i++) {
            LabelItem label = selectedLabel.get(i);
            if (label.getId() != null) {
                TextView text = (TextView) selectView.findViewById(R.id.label_text);
                text.setText("已选标签");
                //遍历各个标签，生成TextView添加到viewGroup中
                TextView labelTextView = setTextViewToGroup(label.getName());
                selectLabelViewGroup.addView(labelTextView);
            } else {
                TextView text = (TextView) personalView.findViewById(R.id.label_text);
                text.setText("自定义标签");
                //遍历各个标签，生成TextView添加到viewGroup中
                TextView labelTextView = setTextViewToGroup(label.getName());
                personalLabelViewGroup.addView(labelTextView);
            }
        }
        linearLayout.addView(selectView);
        linearLayout.addView(personalView);
        modifyButton = (Button) findViewById(R.id.button_modify_label);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               jumpToModifyLabel();
            }
        });
    }

    //通过文本文件创建TextView
    public TextView setTextViewToGroup(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextAppearance(this, R.style.labelStyle);
        textView.setBackgroundResource(R.drawable.label_select_shape);
        textView.setTextColor(getResources().getColor(R.color.white));
        return textView;
    }

    public void jumpToModifyLabel(){
        LabelLogic labelLogic = (LabelLogic) LogicFactory.self().get(LogicFactory.Type.Label);
        labelLogic.getLabelList(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                LabelEventArgs labelEventArgs = (LabelEventArgs) args;
                OperErrorCode errCode = labelEventArgs.getErrCode();
                switch (errCode) {
                    case Success:
                        Intent intent = new Intent(getActivity(), UpdateLabelActivity.class);
                        ArrayList<LabelType> labelTypeList = labelEventArgs.getLabel();
                        for (int i = 0; i <labelTypeList.size() ; i++) {
                            ArrayList<LabelItem> labels = labelTypeList.get(i).getTagList();
                            for (int j = 0; j <labels.size() ; j++) {
                                LabelItem item = labels.get(j);
                                String itemId = item.getId();
                                for (int k = 0; k <selectedLabel.size() ; k++) {
                                    String selectId =selectedLabel.get(k).getId();
                                    Log.d("isselect",selectId+" "+itemId);
                                    if(itemId.equals(selectId)){
                                        item.setIsSelected(1);
                                    }
                                }
                            }
                        }
                        LabelType personal = new LabelType();
                        ArrayList<LabelItem> tagList= new ArrayList<LabelItem>();
                        for (int i = 0; i <selectedLabel.size() ; i++) {
                           LabelItem item = selectedLabel.get(i);
                            if(item.getId()==null){
                                item.setIsSelected(1);
                                tagList.add(item);
                            }
                        }
                        personal.setName("自定义标签");
                        personal.setTagList(tagList);
                        labelTypeList.add(personal);

                        intent.putExtra("Labels", labelTypeList);
                        startActivity(intent);
                        break;
                }
            }
        }));
    }

}
