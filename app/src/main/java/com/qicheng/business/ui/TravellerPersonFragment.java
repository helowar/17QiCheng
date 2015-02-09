package com.qicheng.business.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qicheng.R;
import com.qicheng.business.module.TravellerPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * 展现同路车友Fragment类
 */
public class TravellerPersonFragment extends Fragment {

    private static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    private static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

    /**
     * 车友View
     */
    private GridView personsGridView = null;

    /**
     * 车友列表
     */
    private List<TravellerPerson> personList = new ArrayList<TravellerPerson>();

    /**
     * 车友View的适配器
     */
    private ImageAdapter imageAdapter = null;

    /**
     * 图片加载器及其相关参数
     */
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;
    private DisplayImageOptions options;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageAdapter = new ImageAdapter();
        for (int i = 0; i < 2; i++) {
            personList.add(new TravellerPerson());
            personList.add(new TravellerPerson());
            personList.add(new TravellerPerson());
            personList.add(new TravellerPerson());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View personView = inflater.inflate(R.layout.fragment_traveller_person, container, false);
        // 初始化车友View
        personsGridView = (GridView) personView.findViewById(R.id.traveller_persons_grid_view);
        personsGridView.setAdapter(imageAdapter);
        personsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startUserActivity(position);
            }
        });
        return personView;
    }

    @Override
    public void onResume() {
        super.onResume();
        personsGridView.setOnScrollListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
    }

    private void startUserActivity(int position) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);//TODO
        startActivity(intent);
    }

    public class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return personList.size();
        }

        @Override
        public Object getItem(int position) {
            return personList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View personView;
            if (convertView == null) {
                personView = getActivity().getLayoutInflater().inflate(R.layout.traveller_person, parent, false);
            } else {
                personView = convertView;
            }
            ImageView imageView = (ImageView) personView.findViewById(R.id.traveller_person_img);
            String portraitUrl = personList.get(position).getPortrait_url();
            if (portraitUrl == null) {
                imageView.setImageResource(R.drawable.ic_default_portrait);
            } else {
                imageLoader.displayImage(portraitUrl, imageView, options);
            }
            return personView;
        }
    }

    public class TravellerOnScrollListener extends PauseOnScrollListener {

        /**
         * 拖至顶部的第一个可见位置和Y轴坐标
         */
        private int firstVisiblePosition = -1, firstLocationY = 0;

        /**
         * 拖至底部的最后可见位置和Y轴坐标
         */
        private int lastVisiblePosition = 0, lastLocationY = 0;

        public TravellerOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
            super(imageLoader, pauseOnScroll, pauseOnFling);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                int currentFirstVisiblePosition = view.getFirstVisiblePosition();
                if (currentFirstVisiblePosition == 0) {
                    // 滚动到顶部
                    View v = (View) view.getChildAt(0);
                    int[] location = new int[2];
                    // 获取在整个屏幕内的绝对坐标
                    v.getLocationOnScreen(location);
                    int y = location[1];
                    if (currentFirstVisiblePosition != firstVisiblePosition && firstLocationY != y) {
                        // 第一次拖至顶部
                        firstVisiblePosition = currentFirstVisiblePosition;
                        firstLocationY = y;
                        return;
                    } else if (currentFirstVisiblePosition == firstVisiblePosition && firstLocationY == y) {
                        // 第二次拖至顶部
                        personList.add(new TravellerPerson());
                        personList.add(new TravellerPerson());
                        personList.add(new TravellerPerson());
                        personList.add(new TravellerPerson());
                        imageAdapter.notifyDataSetChanged();
                        personsGridView.smoothScrollToPosition(0);
                    }
                } else {
                    int currentLastVisiblePosition = view.getLastVisiblePosition();
                    if (currentLastVisiblePosition == (view.getCount() - 1)) {
                        // 滚动到底部
                        View v = (View) view.getChildAt(view.getChildCount() - 1);
                        int[] location = new int[2];
                        // 获取在整个屏幕内的绝对坐标
                        v.getLocationOnScreen(location);
                        int y = location[1];
                        if (currentLastVisiblePosition != lastVisiblePosition && lastLocationY != y) {
                            // 第一次拖至底部
                            lastVisiblePosition = currentLastVisiblePosition;
                            lastLocationY = y;
                            return;
                        } else if (currentLastVisiblePosition == lastVisiblePosition && lastLocationY == y) {
                            // 第二次拖至底部
                            personList.add(new TravellerPerson());
                            personList.add(new TravellerPerson());
                            personList.add(new TravellerPerson());
                            personList.add(new TravellerPerson());
                            imageAdapter.notifyDataSetChanged();
                            personsGridView.smoothScrollToPosition(view.getCount());
                        }
                    }
                }

                // 未滚动到顶部
                firstVisiblePosition = -1;
                firstLocationY = 0;
                // 未滚动到底部
                lastVisiblePosition = 0;
                lastLocationY = 0;
            }
        }
    }
}
