package com.qicheng.business.ui.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qicheng.R;


/**
 * Created by NO1 on 2015/2/26.
 */
public class ListFootView {

    private View mFooterView;

    private ProgressBar mProgressBar;

    private TextView mFooterMsg;

    private ListFootView(){

    }

    public static class Factory{
        public static ListFootView newListFootView(LayoutInflater inflater){
            ListFootView listFootView = new ListFootView();
            listFootView.mFooterView = inflater.inflate(R.layout.list_footer, null);
            listFootView.mProgressBar = (ProgressBar)listFootView.mFooterView.findViewById(R.id.progress_bar);
            listFootView.mFooterMsg = (TextView)listFootView.mFooterView.findViewById(R.id.footer_msg);
            return listFootView;
        }
    }

    public void show(){
        mFooterMsg.setText(R.string.load_more);
        mFooterView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hide(){
        mFooterView.setVisibility(View.GONE);
    }

    public void reachBottomWithMsg(int resId){
        mProgressBar.setVisibility(View.GONE);
        mFooterMsg.setText(resId);
    }

    public View getView(){
        return mFooterView;
    }
}
