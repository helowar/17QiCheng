package com.qicheng.business.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qicheng.R;
import com.qicheng.business.module.User;
import com.qicheng.framework.ui.HorizontalScrollListView;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.util.Const;

import java.util.ArrayList;
import java.util.List;

public class TravellerActivity extends BaseActivity {

    private static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    private static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

    /**
     * 推荐车友View
     */
    private HorizontalScrollListView recommendPersonsView = null;

    /**
     * 推荐车友Layout
     */
    private LinearLayout recommendPersonsLayout = null;

    /**
     * 推荐车友列表
     */
    private List<User> recommendPersonList = new ArrayList<User>();

    /**
     * 推荐车友适配器
     */
    private RecommendPersonAdapter recommendPersonAdapter = null;

    /**
     * 出发车友按钮
     */
    private Button startBtn = null;

    /**
     * 到达车友按钮
     */
    private Button endBtn = null;

    /**
     * 出发车友FrameLayout
     */
    private FrameLayout startFrameLayout = null;

    /**
     * 到达车友FrameLayout
     */
    private FrameLayout endFrameLayout = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;
    private DisplayImageOptions options;

    /**
     * 出发车友Fragment
     */
    private TravellerPersonFragment startTravellerFragment = null;

    /**
     * 到达车友Fragment
     */
    private TravellerPersonFragment endTravellerFragment = null;

    /**
     * Fragment事物管理对象
     */
    private FragmentTransaction fragmentTransaction = null;

    /**
     * 查询类型
     */
    private byte queryType = 0;

    /**
     * 查询值
     */
    private String queryValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller);
        // 获取各种View对象
        recommendPersonsView = (HorizontalScrollListView) findViewById(R.id.traveller_recommend_persons_view);
        recommendPersonsLayout = (LinearLayout) findViewById(R.id.traveller_recommend_persons_layout);
        startBtn = (Button) findViewById(R.id.traveller_start_btn);
        endBtn = (Button) findViewById(R.id.traveller_end_btn);
        startFrameLayout = (FrameLayout) findViewById(R.id.traveller_start_Layout);
        endFrameLayout = (FrameLayout) findViewById(R.id.traveller_end_Layout);
        // 图片缓存加载选项值
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_default_portrait)
                .showImageForEmptyUri(R.drawable.ic_default_portrait)
                .showImageOnFail(R.drawable.ic_default_portrait)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        // 设置出发车友和到达车友区域里的各种View对象
        startTravellerFragment = new TravellerPersonFragment();
        endTravellerFragment = new TravellerPersonFragment();
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.traveller_start_Layout, startTravellerFragment);
        fragmentTransaction.add(R.id.traveller_end_Layout, endTravellerFragment);
        fragmentTransaction.commit();
        startFrameLayout.setVisibility(View.VISIBLE);
        endFrameLayout.setVisibility(View.VISIBLE);
        // 设置出发车友和到达车友按钮监听事件
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                endBtn.setTextColor(getResources().getColor(R.color.main));
                startBtn.setBackgroundColor(getResources().getColor(R.color.main));
                startBtn.setTextColor(getResources().getColor(R.color.white));
                endFrameLayout.setVisibility(View.GONE);
                startFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setBackgroundResource(R.drawable.bg_form_input_container);
                startBtn.setTextColor(getResources().getColor(R.color.main));
                endBtn.setBackgroundColor(getResources().getColor(R.color.main));
                endBtn.setTextColor(getResources().getColor(R.color.white));
                startFrameLayout.setVisibility(View.GONE);
                endFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        // 获取上一个Activity传递过来的值
        Bundle extras= getIntent().getExtras();
        queryType = extras.getByte(Const.Intent.TRAVELLER_QUERY_TYPE);
        queryValue = extras.getString(Const.Intent.TRAVELLER_QUERY_VALUE);
    }

    private void startUserActivity(int position) {
        Intent intent = new Intent(this, LoginActivity.class);//TODO
        startActivity(intent);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
        pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < 6; i++) {
            User travellerPerson = new User();
            travellerPerson.setStationName("杭州" + i);
            recommendPersonsLayout.addView(createRecommendPersonView(travellerPerson));
        }
        recommendPersonsView.setOnScrollStopListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_traveller, menu);
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

    public class TravellerOnScrollListener extends PauseOnScrollListener implements HorizontalScrollListView.OnScrollStopListener {

        /**
         * 是否第一次拖至最左边标识
         */
        private boolean isFirstScrollToLeftEdge = false;

        /**
         * 是否第一次拖至最右边标识
         */
        private boolean isFirstScrollToRightEdge = false;

        public TravellerOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super(imageLoader, pauseOnScroll, pauseOnFling);
        }

        @Override
        public void onScrollStoped() {
        }

        @Override
        public void onScrollToLeftEdge() {
            if (!isFirstScrollToLeftEdge) {
                isFirstScrollToLeftEdge = true;
            } else {
                for (int i = 0; i < 3; i++) {
                    User travellerPerson = new User();
                    travellerPerson.setStationName("杭州" + i);
                    recommendPersonsLayout.addView(createRecommendPersonView(travellerPerson), 0);
                }
            }
        }

        @Override
        public void onScrollToRightEdge() {
            if (!isFirstScrollToRightEdge) {
                isFirstScrollToRightEdge = true;
            } else {
                for (int i = 0; i < 3; i++) {
                    User travellerPerson = new User();
                    travellerPerson.setStationName("杭州" + i);
                    recommendPersonsLayout.addView(createRecommendPersonView(travellerPerson));
                }
            }
        }

        @Override
        public void onScrollToMiddle() {
            isFirstScrollToLeftEdge = false;
            isFirstScrollToRightEdge = false;
        }
    }

    public class RecommendPersonAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return recommendPersonList.size();
        }

        @Override
        public Object getItem(int position) {
            return recommendPersonList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 设置推荐车友View
            final View recommendPerson;
            ImageView imageView = null;
            TextView textView = null;
            User travellerPerson = null;
            if (convertView == null) {
                recommendPerson = getActivity().getLayoutInflater().inflate(R.layout.traveller_recommend_person, parent, false);
                imageView = (ImageView) recommendPerson.findViewById(R.id.traveller_recommend_person_img);
                travellerPerson = recommendPersonList.get(position);
                String portraitUrl = travellerPerson.getPortraitURL();
                if (portraitUrl == null) {
                    imageView.setImageResource(R.drawable.ic_default_portrait);
                } else {
                    imageLoader.displayImage(portraitUrl, imageView, options);
                }
                textView = (TextView) recommendPerson.findViewById(R.id.traveller_recommend_person_end);
                textView.setText(getString(R.string.traveller_to) + ' ' + travellerPerson.getStationName());
//                recommendPerson.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startUserActivity(1);
//                    }
//                });
            } else {
                recommendPerson = convertView;
            }
            return recommendPerson;
        }
    }

    private View createRecommendPersonView(User travellerPerson) {
        // 创建推荐车友View
        View recommendPersonView;
        ImageView imageView = null;
        TextView textView = null;
        recommendPersonView = getActivity().getLayoutInflater().inflate(R.layout.traveller_recommend_person, recommendPersonsLayout, false);
        imageView = (ImageView) recommendPersonView.findViewById(R.id.traveller_recommend_person_img);
        String portraitUrl = travellerPerson.getPortraitURL();
        if (portraitUrl == null) {
            imageView.setImageResource(R.drawable.ic_default_portrait);
        } else {
            imageLoader.displayImage(portraitUrl, imageView, options);
        }
        textView = (TextView) recommendPersonView.findViewById(R.id.traveller_recommend_person_end);
        textView.setText(getString(R.string.traveller_to) + ' ' + travellerPerson.getStationName());
//        recommendPersonView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startUserActivity(1);
//            }
//        });
        return recommendPersonView;
    }
}
