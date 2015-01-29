package com.qicheng.business.ui;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qicheng.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SloganFragment extends Fragment {

    private ImageView mSlogImage;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        mSlogImage = (ImageView)v.findViewById(R.id.image_slogan);
        mSlogImage.setImageResource(R.drawable.slogan_railway);
        return v;
    }


}
