package com.qicheng.business.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qicheng.R;
import com.qicheng.framework.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainPickFragment extends BaseFragment {


    public TrainPickFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_train_pick, container, false);
    }


}
