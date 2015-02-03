package com.qicheng.business.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseActivity;

public class TravellerActivity extends BaseActivity {

    private static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    private static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

    /**
     * 推荐车友View
     */
    private HorizontalScrollView recommendPersonsView = null;

    /**
     * 推荐车友Layout
     */
    private LinearLayout recommendPersonsLayout = null;

    /**
     * 出发车友按钮
     */
    private Button startBtn = null;

    /**
     * 到达车友按钮
     */
    private Button endBtn = null;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    private String[] imageUrls = new String[]{
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f9585c9cce2fe9925bc317d22.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/fcfaaf51f3deb48fed45bbd0f21f3a292df5788b.jpg"
    };

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveller);
        // 获取各种View对象
        recommendPersonsView = (HorizontalScrollView) findViewById(R.id.traveller_recommend_persons_view);
        recommendPersonsLayout = (LinearLayout) findViewById(R.id.traveller_recommend_persons_layout);
        startBtn = (Button) findViewById(R.id.traveller_start_btn);
        endBtn = (Button) findViewById(R.id.traveller_end_btn);
        // 图片缓存加载选项值
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_default_portrait)
                .showImageForEmptyUri(R.drawable.ic_default_portrait)
                .showImageOnFail(R.drawable.ic_default_portrait)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        getFragmentManager().beginTransaction().add(R.id.traveller_persons_Layout, new TravellerPersonFragment()).commit();
        findViewById(R.id.traveller_persons_Layout).setVisibility(View.VISIBLE);
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
        // 设置推荐车友View
        LinearLayout recommendPersonLayout = null;
        ImageView imageView = null;
        TextView textView = null;
        for(int i = 0; i < 10; i++) {
            recommendPersonLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.traveller_recommend_person, null);
            imageView = (ImageView) recommendPersonLayout.findViewById(R.id.traveller_recommend_person_img);
            imageLoader.displayImage(imageUrls[i], imageView, options);
            textView  = (TextView) recommendPersonLayout.findViewById(R.id.traveller_recommend_person_end);
            textView.setText("上海" + i);
            recommendPersonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserActivity(1);
                }
            });
            recommendPersonsLayout.addView(recommendPersonLayout);
        }
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
}
