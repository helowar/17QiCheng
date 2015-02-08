package com.qicheng.framework.ui.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qicheng.framework.ui.helper.Alert;

import java.util.ArrayList;

/**
 * Created by NO1 on 2015/1/27.
 */
public abstract class BaseListFragment extends Fragment {

    //数据列表
    private ArrayList trips;

    private ArrayList pageList = new ArrayList();

    private int pageSize;

    private View footerView;

    private OnFragmentInteractionListener mListener;

    protected ListView mListView;


    protected abstract ArrayAdapter getAdapter();

    protected abstract ListView getListView();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class ListOnScrollListener implements AbsListView.OnScrollListener {
        private boolean lastIndex = false;

        /**
         * 判断是否已滚动到最后一条
         *
         * @param view
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int i = view.getLastVisiblePosition();
            if (scrollState == SCROLL_STATE_IDLE && lastIndex) {
                loadMoreData();// 加载更多数据
//                            bt.setVisibility(View.VISIBLE);
//                            pg.setVisibility(View.GONE);
                getAdapter().notifyDataSetChanged();// 通知listView刷新数据

                //标志重置
                lastIndex = false;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //已到最后

            if (view.getLastVisiblePosition() + 1 == view.getCount()) {
                lastIndex = true;
            }
            // 所有的条目已经和最大条数相等，则移除底部的View
            if (view.getLastVisiblePosition() == trips.size()) {
                ((ListView) view).removeFooterView(footerView);
                Alert.Toast("别拉了！到底啦！");
            }
        }
    }

    private void loadMoreData() {
        int count = getAdapter().getCount();
        if (count + pageSize <= trips.size()) {
            for (int i = 0; i < pageSize; i++) {
                pageList.add(trips.get(count + i));
            }
        } else {
            for (int i = count; i < trips.size(); i++) {
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
