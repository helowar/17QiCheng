package com.qicheng.business.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.qicheng.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NO3 on 2015/2/4.
 */
public class PersonalInformationPhotoAdapter extends BaseAdapter {

    private List<Integer> imageList = new ArrayList<Integer>();

    private Context context;

    public PersonalInformationPhotoAdapter(Context context) {
        super();
        this.context = context;

        imageList.add(R.drawable.ic_test_img);
        imageList.add(R.drawable.ic_test_img);
        imageList.add(R.drawable.ic_test_img);
        imageList.add(R.drawable.ic_test_img);

        ;

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("run", "---------------------------");
        ImageView imageView = new ImageView(context);
        imageView.setPadding(5, 0, 5, 0);


        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(imageList.get(position));
        return imageView;
    }
}
