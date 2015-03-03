package com.qicheng.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.image.ImageManager;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TripLogic;
import com.qicheng.business.logic.event.TripEventArgs;
import com.qicheng.business.module.Trip;
import com.qicheng.business.ui.component.ListFootView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.UIUtil;
import com.qicheng.util.Const;

import java.util.ArrayList;

/**
 * 行程列表展示Fragment
 */
public class TripListFragment extends BaseFragment {

    private static Logger logger = new Logger("TripListFragment");
    private static final int REQUEST_CODE_ADD_TRIP = 0;

    private TripLogic logic;

    private int lastTrip;

    private boolean noFurtherData = false;


    //记录当前展开的位置
    private int unfoledPosition = 0;
    //行程列表
    private ArrayList<Trip> pageList = new ArrayList();

    private ListFootView footerView;

    private ListView mListView;

    private TripListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logic =(TripLogic) LogicFactory.self().get(LogicFactory.Type.Trip);
        mAdapter = new TripListAdapter(pageList);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.title_activity_main));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                startActivityForResult(new Intent(getActivity(),TrainSelectActivity.class),REQUEST_CODE_ADD_TRIP);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * 为单个行程增加详情和用户列表，响应点击事件
     *
     * @param inflater 从Activity获得
     * @param parent   上级View即行程记录
     */
    private void addDetailView(int position,LayoutInflater inflater, TableLayout parent) {
        /**
         * 行程数据
         */
        final Trip trip = pageList.get(position);
        /**
         * 获取行程详情控件
         */
        View tripDetailView = inflater.inflate(R.layout.trip_detail_row, null);
        TextView viewStartStation = (TextView)tripDetailView.findViewById(R.id.textview_detail_sstation);
        TextView viewEndStation = (TextView)tripDetailView.findViewById(R.id.textview_detail_estation);
        TextView viewStartTime= (TextView)tripDetailView.findViewById(R.id.textview_detail_stime);
        TextView viewEndTime= (TextView)tripDetailView.findViewById(R.id.textview_detail_etime);
        TextView viewTrainCode= (TextView)tripDetailView.findViewById(R.id.textview_detail_traincode);
        TextView viewTripDate= (TextView)tripDetailView.findViewById(R.id.textview_detail_date);
        /**
         * 控件赋值
         */
        viewStartStation.setText(trip.getStartStationName());
        viewStartStation.setTag(trip.getStartStationCode());

        viewEndStation.setText(trip.getEndStationName());
        viewEndStation.setTag(trip.getEndStationCode());

        viewStartTime.setText(getTimeForView(trip.getStartTime()));
        viewEndTime.setText(getTimeForView(trip.getStopTime()));

        viewTripDate.setText(getDateForView(trip.getStartTime()));
        viewTrainCode.setText(trip.getTrainCode());
        //取得窗口属性
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        int sumWidth = screenWidth-UIUtil.dip2Px(getResources().getDimension(R.dimen.common_gap)*2+10,getResources().getDisplayMetrics().density);
        int width = sumWidth/6;
        /**
         * 获取行程用户列表控件
         */

        View tripUserView = inflater.inflate(R.layout.trip_user_row, null);
        /*跳转至查看出发站旅客列表*/
        tripUserView.findViewById(R.id.home_station).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getActivity(),TravellerActivity.class);
                i.putExtra(Const.Intent.TRAVELLER_QUERY_VALUE,trip.getStartStationCode());
                startActivity(i);
            }
        });
        /*跳转至查看到达站旅客列表*/
        tripUserView.findViewById(R.id.dest_station).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getActivity(),TravellerActivity.class);
                i.putExtra(Const.Intent.TRAVELLER_QUERY_VALUE,trip.getEndStationCode());
                startActivity(i);
            }
        });
        /*跳转至查看同车旅客列表*/
        tripUserView.findViewById(R.id.train_users).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getActivity(),PassengerActivity.class);
                i.putExtra(Const.Intent.TRAVELLER_QUERY_VALUE,trip.getTrainCode());
                startActivity(i);
            }
        });
        ArrayList<ImageView> viewUsers = new ArrayList<ImageView>();
        ImageView viewUser1 =(ImageView) tripUserView.findViewById(R.id.image_user1);
        setImageViewWidth(viewUser1,width);
        viewUsers.add(viewUser1);
        ImageView viewUser2 =(ImageView) tripUserView.findViewById(R.id.image_user2);
        setImageViewWidth(viewUser2,width);
        viewUsers.add(viewUser2);
        ImageView viewUser3 =(ImageView) tripUserView.findViewById(R.id.image_user3);
        setImageViewWidth(viewUser3,width);
        viewUsers.add(viewUser3);
        ImageView viewUser4 =(ImageView) tripUserView.findViewById(R.id.image_user4);
        setImageViewWidth(viewUser4,width);
        viewUsers.add(viewUser4);
        ImageView viewUser5 =(ImageView) tripUserView.findViewById(R.id.image_user5);
        setImageViewWidth(viewUser5,width);
        viewUsers.add(viewUser5);
        ImageView viewUser6 =(ImageView) tripUserView.findViewById(R.id.image_user6);
        setImageViewWidth(viewUser6,width);
        viewUsers.add(viewUser6);
        ImageView viewUser7 =(ImageView) tripUserView.findViewById(R.id.image_user7);
        setImageViewWidth(viewUser7,width);
        viewUsers.add(viewUser7);
        ImageView viewUser8 =(ImageView) tripUserView.findViewById(R.id.image_user8);
        setImageViewWidth(viewUser8,width);
        viewUsers.add(viewUser8);
        ImageView viewUser9 =(ImageView) tripUserView.findViewById(R.id.image_user9);
        setImageViewWidth(viewUser9,width);
        viewUsers.add(viewUser9);
        ImageView viewUser10 =(ImageView) tripUserView.findViewById(R.id.image_user10);
        setImageViewWidth(viewUser10,width);
        viewUsers.add(viewUser10);
        ImageView viewUser11 =(ImageView) tripUserView.findViewById(R.id.image_user11);
        setImageViewWidth(viewUser11,width);
        viewUsers.add(viewUser11);
        ImageView viewUser12 =(ImageView) tripUserView.findViewById(R.id.image_user12);
        setImageViewWidth(viewUser12,width);
        viewUsers.add(viewUser12);
        /**
         * 数据填充
         */
        if(trip.getStartUserList()!=null) {
            ArrayList<String> startList = trip.getStartUserList();
            for (int i = 0; i < startList.size(); i++) {
                ImageManager.displayPortrait(startList.get(i),viewUsers.get(i));
            }
        }
        if(trip.getTrainUserList()!=null){
            ArrayList<String> trainList = trip.getTrainUserList();
            for (int i = 0; i < trainList.size(); i++) {
                ImageManager.displayPortrait(trainList.get(i),viewUsers.get(4+i));
            }
        }
        if(trip.getStopUserList()!=null){
            ArrayList<String> stopList = trip.getStopUserList();
            for (int i = 0; i < stopList.size(); i++) {
                ImageManager.displayPortrait(stopList.get(i),viewUsers.get(8+i));
            }
        }
        parent.addView(tripDetailView);
        parent.addView(tripUserView);
    }

    private class TripListAdapter extends ArrayAdapter<Trip> {

        public TripListAdapter(ArrayList<Trip> trips) {
            super(getActivity(), 0, trips);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_trip, null);
                convertView.setTag("" + position);
            } else {
                //行程展开
                if (position != unfoledPosition && ((TableLayout) convertView).getChildCount() > 1) {
                    ((TableLayout) convertView).removeViewAt(1);
                    ((TableLayout) convertView).removeViewAt(1);
                    logger.d("remove view in get view Tag: " + convertView.getTag());
                } else if (position == unfoledPosition && ((TableLayout) convertView).getChildCount() < 2) {
                    addDetailView(position,getActivity().getLayoutInflater(), (TableLayout) convertView);
                }
            }
            /**
             * 行程数据
             */
            Trip lineTrip = pageList.get(position);
            /**
             * 获取一行Trip的各组件
             */
            TextView viewTrainCode = (TextView)convertView.findViewById(R.id.textview_train_code);
            TextView viewTripDate = (TextView)convertView.findViewById(R.id.textview_trip_date);
            TextView viewStartStation = (TextView)convertView.findViewById(R.id.textview_start_station);
            TextView viewEndStation = (TextView)convertView.findViewById(R.id.textview_end_station);
            /**
             * 控件设置
             */
            viewTrainCode.setText(lineTrip.getTrainCode());
            viewTripDate.setText(getDateForView(lineTrip.getStartTime()));
            viewStartStation.setText(lineTrip.getStartStationName());
            viewEndStation.setText(lineTrip.getEndStationName());
            return convertView;
        }
    }

    /**
     * 服务端提供的日期时间格式转换
     * @param dttm
     * @return
     */
    private String getDateForView(String dttm){
        if(dttm.indexOf(DateTimeUtil.date_separator)!=-1){
            String s = dttm.substring(0,10);
            s = s.replaceFirst(DateTimeUtil.date_separator,getResources().getString(R.string.year_text));
            s = s.replaceFirst(DateTimeUtil.date_separator,getResources().getString(R.string.month_text));
            s = s+getResources().getString(R.string.day_text);
            return s;
        }
        return dttm.substring(0,4)+getResources().getString(R.string.year_text)
                +dttm.substring(4,6)+getResources().getString(R.string.month_text)
                +dttm.substring(6,8)+getResources().getString(R.string.day_text);
    }

    /**
     * 服务端提供的日期时间格式转换
     * @param dttm
     * @return
     */
    private String getTimeForView(String dttm){
        if(dttm.indexOf(DateTimeUtil.time_separator)!=-1){
            return dttm.substring(11);
        }
        return dttm.substring(8,10)+DateTimeUtil.time_separator+dttm.substring(10);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_triplist_list, container, false);

        mListView = (ListView) view.findViewById(android.R.id.list);
        AdapterView.OnItemClickListener mOnClickListener
                = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onListItemClick((ListView)parent, v, position, id);
            }
        };
        mListView.setOnItemClickListener(mOnClickListener);
        footerView = ListFootView.Factory.newListFootView(inflater);
        footerView.hide();
        //上拉刷新
        mListView.addFooterView(footerView.getView());
        // Set the adapter
        mListView.setAdapter(mAdapter);
        /**
         * 为ListView添加下拉滚动监听器，实现List的分批加载
         */
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private boolean lastIndex = false;

            /**
             * 判断是否已滚动到最后一条
             * @param view
             * @param scrollState
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int i = view.getLastVisiblePosition();
                if (scrollState == SCROLL_STATE_FLING && lastIndex) {
                    loadMoreData();// 加载更多数据
//                            bt.setVisibility(View.VISIBLE);
//                            pg.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();// 通知listView刷新数据
                    //标志重置
                    lastIndex = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //已到最后
                if (view.getLastVisiblePosition() + 1 == view.getCount()) {
                    lastIndex = true;
                    footerView.show();
                }
                if (noFurtherData) {
                    footerView.reachBottomWithMsg(R.string.no_more);
                }
//                // 所有的条目已经和最大条数相等，则移除底部的View
//                if (view.getLastVisiblePosition() == trips.size()) {
//                    ((ListView) view).removeFooterView(footerView);
//                    Alert.Toast("别拉了！到底啦！");
//                }
            }

        });
        loadMoreData();
        return view;
    }

    private void loadMoreData() {
        logic.getPersonalTripList(lastTrip,createUIEventListener(new EventListener() {
            @Override
            public void onEvent(EventId id, EventArgs args) {
                footerView.hide();
                TripEventArgs result =  (TripEventArgs)args;
                OperErrorCode errCode = result.getErrCode();
                switch(errCode) {
                    case Success:
                        ArrayList<Trip> tripList = result.getTripList();
                        pageList.addAll(tripList);
                        lastTrip = tripList.get(tripList.size()-1).getOrderNum();
                        mAdapter.notifyDataSetChanged();
                        break;
                    case NoDataFound:
//                        Alert.Toast(getResources().getString(R.string.no_trip_msg));
                        noFurtherData = true;
                        break;
                    default:
//                        Alert.Toast(getResources().getString(R.string.no_trip_msg));
                        break;
                }
            }
        }));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void onListItemClick(ListView list, View view, int position, long id) {
//        String tag = lastPosition+"";
//        lastPosition= position;
//        logger.d("now Tag: "+view.getTag());
//        logger.d("lastItemTag: "+tag);
//        logger.d("lastItem child count: "+lastItem.getChildCount());
        removeDetailView(list);
        unfoledPosition = position;
        addDetailView(position,getActivity().getLayoutInflater(), (TableLayout) view);
    }

    private void removeDetailView(ListView list) {
        int firstVisiblePosition = list.getFirstVisiblePosition();
        int lastVisiblePosition = list.getLastVisiblePosition();
        if (unfoledPosition >= firstVisiblePosition && unfoledPosition <= lastVisiblePosition) {
            TableLayout lastItem = (TableLayout) list.getChildAt(unfoledPosition - firstVisiblePosition);
            if (lastItem.getChildCount() > 1) {
                logger.d("remove view by tag:" + lastItem.getTag());
                lastItem.removeViewAt(1);
                lastItem.removeViewAt(1);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //结果码不等于取消时候
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_CODE_ADD_TRIP :
                    Trip trip = (Trip)data.getSerializableExtra(StationSelectFragment.EXTRA_TRIP);
                    addNewTrip(trip);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addNewTrip(Trip trip){
        pageList.add(0,trip);
        mAdapter.notifyDataSetChanged();
    }

    private void setImageViewWidth(ImageView view,int width){
        ViewGroup.LayoutParams params =  view.getLayoutParams();
        params.height=width;
        params.width=width;
    }
}
