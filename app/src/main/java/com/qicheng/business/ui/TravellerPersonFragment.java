package com.qicheng.business.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TravellerPersonLogic;
import com.qicheng.business.logic.UserLogic;
import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.module.User;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseActivity;
import com.qicheng.framework.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_TYPE;
import static com.qicheng.util.Const.Intent.TRAVELLER_QUERY_VALUE;
import static com.qicheng.util.Const.ORDER_BY_EARLIEST;
import static com.qicheng.util.Const.ORDER_BY_NEWEST;
import static com.qicheng.util.Const.STATE_PAUSE_ON_FLING;
import static com.qicheng.util.Const.STATE_PAUSE_ON_SCROLL;

/**
 * TravellerPersonFragment.java是启程APP的展现同路车友Fragment类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月1日
 */
public class TravellerPersonFragment extends BaseFragment {

    /**
     * 车友View
     */
    private GridView personsGridView = null;

    /**
     * 车友列表
     */
    private List<User> personList = new ArrayList<User>();

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

    /**
     * 查询类型
     * <p/>
     * 0：车站 1：出发 2：到达 3：车次 4：未上车 5：上车 6：下车 7：附近 8：城市
     * 如果为空，表示获取全站数据
     */
    private byte queryType;

    /**
     * 查询值
     * <p/>
     * 当query_type=0、1或2时，该值为车站代码；
     * 当query_type=3、4、5或6时，该值为车次；
     * 当query_type=7时，该值为经纬度，其格式为：经度 + | + 纬度；
     * 当query_type=8时，该值为城市代码。
     */
    private String queryValue;

    /**
     * 查询用户信息业务逻辑处理对象
     */
    private TravellerPersonLogic logic = null;

    /**
     * 用户业务逻辑处理对象
     */
    private UserLogic userLogic = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageAdapter = new ImageAdapter();
        Bundle bundle = getArguments();
        queryType = bundle.getByte(TRAVELLER_QUERY_TYPE);
        queryValue = bundle.getString(TRAVELLER_QUERY_VALUE);
        logic = (TravellerPersonLogic) LogicFactory.self().get(LogicFactory.Type.TravellerPerson);
        userLogic = (UserLogic) LogicFactory.self().get(LogicFactory.Type.User);
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
                startUserInfoActivity(position);
            }
        });
        personsGridView.setOnScrollListener(new TravellerOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling));
        refreshPerson();
        startLoading();
        return personView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_PAUSE_ON_SCROLL)) {
                pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
            }
            if (savedInstanceState.containsKey(STATE_PAUSE_ON_FLING)) {
                pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
        outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
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
                personView = getActivity().getLayoutInflater().inflate(R.layout.traveller_person, null);
            } else {
                personView = convertView;
            }
            ImageView imageView = (ImageView) personView.findViewById(R.id.traveller_person_img);
            String portraitUrl = personList.get(position).getPortraitURL();
            ImageManager.displayPortrait(portraitUrl, imageView);
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
        public void onScrollStateChanged(final AbsListView view, int scrollState) {
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
                        User firstUser = personList.get(0);
                        // 第二次拖至顶部
                        logic.queryUser(queryType, queryValue, ORDER_BY_NEWEST, firstUser.getLastLoginTime(), 8, createUIEventListener(new EventListener() {
                            @Override
                            public void onEvent(EventId id, EventArgs args) {
                                stopLoading();
                                UserEventArgs result = (UserEventArgs) args;
                                OperErrorCode errCode = result.getErrCode();
                                if (errCode == OperErrorCode.Success) {
                                    List<User> userList = result.getUserList();
                                    if (userList != null && userList.size() > 0) {
                                        personList.addAll(0, userList);
                                        imageAdapter.notifyDataSetChanged();
                                        personsGridView.smoothScrollToPosition(0);
                                    }
                                }
                            }
                        }));
                        startLoading();
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
                            User lastUser = personList.get(personList.size() - 1);
                            // 第二次拖至顶部
                            logic.queryUser(queryType, queryValue, ORDER_BY_EARLIEST, lastUser.getLastLoginTime(), 8, createUIEventListener(new EventListener() {
                                @Override
                                public void onEvent(EventId id, EventArgs args) {
                                    stopLoading();
                                    UserEventArgs result = (UserEventArgs) args;
                                    OperErrorCode errCode = result.getErrCode();
                                    if (errCode == OperErrorCode.Success) {
                                        List<User> userList = result.getUserList();
                                        if (userList != null && userList.size() > 0) {
                                            personList.addAll(userList);
                                            imageAdapter.notifyDataSetChanged();
                                            personsGridView.smoothScrollToPosition(view.getCount());
                                        }
                                    }
                                }
                            }));
                            startLoading();
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

    /**
     * 刷新整个页面里的用户。
     */
    public void refreshPerson() {
        logic.queryUser(queryType, queryValue, ORDER_BY_NEWEST, null, 32, createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                stopLoading();
                UserEventArgs result = (UserEventArgs) args;
                OperErrorCode errCode = result.getErrCode();
                if (errCode == OperErrorCode.Success) {
                    personList.clear();
                    List<User> userList = result.getUserList();
                    if (userList != null && userList.size() > 0) {
                        personList.addAll(userList);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
        }));
    }

    public void setQueryType(byte queryType) {
        this.queryType = queryType;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }

    private void startUserInfoActivity(int position) {
        User traveller = personList.get(position);
        ((BaseActivity) getActivity()).startUserInfoActivity(traveller.getUserId(), userLogic);
    }
}
