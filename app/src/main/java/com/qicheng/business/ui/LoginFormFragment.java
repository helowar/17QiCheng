package com.qicheng.business.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qicheng.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFormFragment extends Fragment {


    public LoginFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_form,container,false);
        return v;
    }


}
