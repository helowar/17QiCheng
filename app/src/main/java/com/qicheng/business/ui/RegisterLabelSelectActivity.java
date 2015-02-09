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

import com.google.gson.Gson;
import com.qicheng.R;
import com.qicheng.business.logic.LabelItemPriorityComparator;
import com.qicheng.business.logic.LabelLogic;
import com.qicheng.business.logic.LabelPriorityComparator;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.event.LabelEventArgs;
import com.qicheng.business.module.Label;
import com.qicheng.business.module.LabelType;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.helper.Alert;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 标签选择Activity
 */
public class RegisterLabelSelectActivity extends BaseActivity {
    private final static String TAG = "Selected";
    private ArrayList<Label> labels = new ArrayList<Label>();
    private Button nextButton;
    private List<LabelType> labelTypes;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_label_select);
        linearLayout = (LinearLayout) findViewById(R.id.label_scroll_root);
        //获取测试数据
        labelTypes = getFakeResult();
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
                String[] ids = new String[]{labelType.getId(), labelType.getTagList().get(j).getId()};
                labelTextView.setTag(ids);
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
     * 获取标签完整列表
     */
    private void getTagList() {
        final LabelLogic labelLogic = (LabelLogic) LogicFactory.self().get(LogicFactory.Type.Label);
        labelLogic.getLabelList(createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                OperErrorCode errCode = ((LabelEventArgs) args).getErrCode();
                labelTypes = ((LabelEventArgs) args).getLabelType();
                Collections.sort(labelTypes, new LabelPriorityComparator());
                for (int i = 0; i < labelTypes.size(); i++) {
                    Collections.sort(labelTypes.get(i).getTagList(), new LabelItemPriorityComparator());
                }
                Log.d("test", labelTypes.toString());

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
                    String[] ids = (String[]) v.getTag();
                    label.setTypeId(ids[0]);
                    label.setItemId(ids[1]);
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
            //Intent intent = new Intent(this,RegisterLabelSelectActivity.class);
            //startActivity(intent);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<LabelType> getFakeResult() {
        String r = "{\n" +
                "    \"result_code\": \"0\", \n" +
                "    \"body\": [\n" +
                "        {\n" +
                "            \"id\": \"XXXXXXXXXXXXXXXXXXA\",\n" +
                "            \"name\": \"看电影\",\n" +
                "            \"priority\": \"2\",\n" +
                "            \"tagList\": [\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXAA\", \"name\": \"动作片\", \"priority\": \"2\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXAB\", \"name\": \"恐怕片\", \"priority\": \"1\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXAC\",\"name\": \"科幻片\", \"priority\": \"3\"}\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"XXXXXXXXXXXXXXXXXXB\",\n" +
                "            \"name\": \"运动\",\n" +
                "            \"priority\": \"1\",\n" +
                "            \"tagList\": [\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXBA\", \"name\": \"网球\", \"priority\": \"3\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXBB\", \"name\": \"羽毛球\", \"priority\": \"1\"},\n" +
                "                {\"id\": \"XXXXXXXXXXXXXXXXBC\",\"name\": \"保龄球\", \"priority\": \"2\"}\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}\n";

        List<LabelType> list = new ArrayList<LabelType>();
        try {

            Gson gson = new Gson();
            JSONObject object = new JSONObject(r);
            JSONArray arry = (JSONArray) object.opt("body");
            for (int i = 0; i < arry.length(); i++) {
                Object o = arry.get(i);
                LabelType labelType = gson.fromJson(o.toString(), LabelType.class);
                list.add(labelType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


}
