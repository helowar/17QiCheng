package com.qicheng.business.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.qicheng.R;

import com.qicheng.business.module.Trip;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.Logger;

import java.util.ArrayList;

/**
 * 行程列表展示Fragment
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TripListFragment extends ListFragment {

    private static Logger logger = new Logger("TripListFragment");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //记录当前展开的位置
    private int unfoledPosition=0;
    //行程列表
    private ArrayList<Trip> trips = new ArrayList<Trip>();

    private ArrayList pageList = new ArrayList();

    private int pageSize = 10;

    private View footerView;

    private OnFragmentInteractionListener mListener;

    private ListView mListView;

    private TripListAdapter mAdapter;

    public static TripListFragment newInstance(String param1, String param2) {
        TripListFragment fragment = new TripListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TripListFragment() {
    }

    private void initFakeTrips(){
        for(int i = 0;i<100;i++){
            Trip trip = new Trip();
            trip.setStartStation(i+"");
            trip.setTrainCode("G4");
            trips.add(trip);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFakeTrips();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        logger.d("size of trips: "+ trips.size());
        mAdapter = new TripListAdapter(pageList);
        loadMoreData();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 为单个行程增加详情和用户列表，响应点击事件
     * @param inflater 从Activity获得
     * @param parent 上级View即行程记录
     */
    private void addDetailView(LayoutInflater inflater,TableLayout parent){
        parent.addView(inflater.inflate(R.layout.trip_detail_row,null));
        parent.addView(inflater.inflate(R.layout.trip_user_row,null));
    }

    private class TripListAdapter extends ArrayAdapter<Trip>{

        public TripListAdapter(ArrayList<Trip> trips){
            super(getActivity(),0,trips);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_trip,null);
                convertView.setTag(""+position);
                ( (TextView)convertView.findViewById(R.id.textview_start_station)).setText(""+position);
            }else{
                if(position!=unfoledPosition && ((TableLayout)convertView).getChildCount()>1){
                    ((TableLayout) convertView).removeViewAt(1);
                    ((TableLayout) convertView).removeViewAt(1);
                    logger.d("remove view in get view Tag: "+convertView.getTag());
                }else if(position == unfoledPosition && ((TableLayout)convertView).getChildCount()<2){
                    addDetailView(getActivity().getLayoutInflater(),(TableLayout) convertView);
                }
            }
//            ((TextView)convertView.findViewById(R.id.textview_start_station)).setText(trips.get(position).getStartStation());
//            if(position==0&&lastPosition==0){
//                logger.d("enter getview and lastposition = "+lastPosition);
//                addDetailView( getActivity().getLayoutInflater(),(TableLayout)convertView);
//            }


            return convertView;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_triplist, container, false);

        mListView = (ListView) view.findViewById(android.R.id.list);
        footerView  = inflater.inflate(R.layout.list_footer,null);
        //上拉刷新
        mListView.addFooterView(footerView);
        // Set the adapter
        mListView.setAdapter(mAdapter);
        /**
         * 为ListView添加下拉滚动监听器，实现List的分批加载
         */
        mListView.setOnScrollListener( new AbsListView.OnScrollListener() {

            private boolean lastIndex = false;

            /**
             * 判断是否已滚动到最后一条
             * @param view
             * @param scrollState
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int  i = view.getLastVisiblePosition();
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

                if(view.getLastVisiblePosition()+1==view.getCount()){
                    lastIndex=true;
                }
                // 所有的条目已经和最大条数相等，则移除底部的View
                if (view.getLastVisiblePosition() == trips.size()) {
                    ((ListView)view).removeFooterView(footerView);
                    Alert.Toast("别拉了！到底啦！");
                }
            }

        });
        // Set OnItemClickListener so we can be notified on item clicks
        return view;
    }

    private void loadMoreData(){
        int count = mAdapter.getCount();
        if(count+pageSize<=trips.size()){
            for(int i = 0;i<pageSize;i++){
                pageList.add(trips.get(count+i));
            }
        }else{
            for(int i = count;i<trips.size();i++){
                pageList.add(trips.get(i));
            }
        }
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
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
//        String tag = lastPosition+"";
//        lastPosition= position;
//        logger.d("now Tag: "+view.getTag());
//        logger.d("lastItemTag: "+tag);
//        logger.d("lastItem child count: "+lastItem.getChildCount());
        removeDetailView(list);
        unfoledPosition = position;
        addDetailView(getActivity().getLayoutInflater(),(TableLayout)view);
    }

    private void removeDetailView(ListView list){
        int firstVisiblePosition = list.getFirstVisiblePosition();
        int lastVisiblePosition = list.getLastVisiblePosition();
        if(unfoledPosition>=firstVisiblePosition && unfoledPosition<= lastVisiblePosition){
            TableLayout lastItem = (TableLayout) list.getChildAt(unfoledPosition-firstVisiblePosition);
            if(lastItem.getChildCount()>1){
                logger.d("remove view by tag:" + lastItem.getTag());
                lastItem.removeViewAt(1);
                lastItem.removeViewAt(1);
            }
        }
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
