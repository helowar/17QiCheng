package com.qicheng.business.ui;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.module.Label;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.ui.component.AutoBreakAndNextReverseLineViewGroup;
import com.qicheng.framework.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationSelectFragment extends BaseFragment {

    private AutoBreakAndNextReverseLineViewGroup stationGroup;


    public StationSelectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_station_select, container, false);
        stationGroup = (AutoBreakAndNextReverseLineViewGroup)convertView.findViewById(R.id.station_view_group);
        setFakeStations(stationGroup);
        stationGroup.setBackgroundDrawable(new Drawable() {
            @Override
            public void draw(Canvas canvas) {

                Paint paint = new Paint();
                paint.setStrokeWidth(5);
                paint.setColor(getResources().getColor(R.color.main));
                final int lineGap=20;//换行时的横向延长线

                final int count = stationGroup.getChildCount();
                int row = 0;// which row lay you view relative to parent
                for (int i = 0; i < count-1; i++) {
                    final View child = stationGroup.getChildAt(i);
                    final View childNext = stationGroup.getChildAt(i+1);

                    int width = child.getMeasuredWidth();
                    int height = child.getMeasuredHeight();
                    float startX = child.getX()+width;
                    float startY = child.getY()+height/2;
                    float stopX = childNext.getX();
                    float stopY = childNext.getY()+height/2;

                    if(startY!=stopY){
                        /**
                         * 出现换行
                         */
                        if(stationGroup.getRightTurnedNodes().contains(child)){
                            /**
                             * 右转换行
                             */
                            int nextWidth = childNext.getMeasuredWidth();
                            stopX = childNext.getX()+nextWidth;
                            float firstLineStartX = 0;
                            float firstLineStopX = 0;
                            float firstLineStartY = 0;
                            float firstLineStopY = 0;
                            if(startX>stopX){
                                firstLineStartX = startX;
                                firstLineStopX = startX+lineGap;
                                firstLineStartY = startY;
                                firstLineStopY = firstLineStartY;
                            }else{
                                firstLineStartX = startX;
                                firstLineStopX = lineGap+stopX;
                                firstLineStartY = startY;
                                firstLineStopY = firstLineStartY;
                            }
                            /**
                             * 第一段横向延长线
                             */
                            canvas.drawLine(firstLineStartX, firstLineStartY, firstLineStopX, firstLineStopY, paint);
                            /**
                             * 纵向行间连接线
                             */
                            canvas.drawLine(firstLineStopX, startY-paint.getStrokeWidth()/2, firstLineStopX, stopY+paint.getStrokeWidth()/2, paint);
                            /**
                             * 下一行的横向延长线
                             */
                            canvas.drawLine(stopX, stopY, firstLineStopX, stopY, paint);
                        }else{
                            /**
                             * 左转换行
                             */
                            int nextWidth = childNext.getMeasuredWidth();
                            startX = child.getX();
                            startY = child.getY()+height/2;
                            stopX = childNext.getX();
                            stopY = childNext.getY()+height/2;
                            float firstLineStartX = 0;
                            float firstLineStopX = 0;
                            float firstLineStartY = 0;
                            float firstLineStopY = 0;
                            if(startX>stopX){
                                firstLineStartX = stopX-lineGap;
                                firstLineStopX = startX;
                                firstLineStartY = startY;
                                firstLineStopY = firstLineStartY;
                            }else{
                                firstLineStartX = startX-lineGap;
                                firstLineStopX = startX;
                                firstLineStartY = startY;
                                firstLineStopY = firstLineStartY;
                            }
                            /**
                             * 第一段横向延长线
                             */
                            canvas.drawLine(firstLineStartX, firstLineStartY, firstLineStopX, firstLineStopY, paint);
                            /**
                             * 纵向行间连接线
                             */
                            canvas.drawLine(firstLineStartX, startY-paint.getStrokeWidth()/2, firstLineStartX, stopY+paint.getStrokeWidth()/2, paint);
                            /**
                             * 下一行的横向延长线
                             */
                            canvas.drawLine(firstLineStartX, stopY, stopX, stopY, paint);
                        }
                    }else{
                        canvas.drawLine(startX, startY, stopX, stopY, paint);
                    }



                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        });
        return convertView;

    }


    private void setFakeStations(AutoBreakAndNextReverseLineViewGroup stationGroup){
        stationGroup.addView(setTextViewToGroup("上海虹桥"));
        stationGroup.addView(setTextViewToGroup("无锡"));
        stationGroup.addView(setTextViewToGroup("苏州"));
        stationGroup.addView(setTextViewToGroup("南京"));
        stationGroup.addView(setTextViewToGroup("常州"));
        stationGroup.addView(setTextViewToGroup("济南"));
        stationGroup.addView(setTextViewToGroup("北京"));
        stationGroup.addView(setTextViewToGroup("上海虹桥"));
        stationGroup.addView(setTextViewToGroup("无锡"));
        stationGroup.addView(setTextViewToGroup("苏州"));
        stationGroup.addView(setTextViewToGroup("南京"));
        stationGroup.addView(setTextViewToGroup("常州"));
        stationGroup.addView(setTextViewToGroup("济南"));
        stationGroup.addView(setTextViewToGroup("北京"));
    }

    public TextView setTextViewToGroup(String textId) {
        TextView textView = new TextView(getActivity());
        textView.setText(textId);
        textView.setBackgroundResource(R.drawable.bg_station_unselected);
        textView.setTextColor(getResources().getColor(R.color.main));
//        final TrainStation station = new TrainStation();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!v.isSelected()) {
//                    label.setName(((TextView) v).getText().toString());

                for(int i = 0;i<stationGroup.getChildCount();i++){
                    TextView child = (TextView)stationGroup.getChildAt(i);
                    child.setBackgroundResource(R.drawable.bg_station_unselected);
                    child.setTextColor(getResources().getColor(R.color.main));
                }
                v.setBackgroundResource(R.drawable.bg_station_selected);
                ((TextView) v).setTextColor(getResources().getColor(R.color.white));
//                    v.setSelected(true);
//                } else {
//                    labels.remove(label);
//                    v.setBackgroundResource(R.drawable.label_shape);
//                    ((TextView) v).setTextColor(getResources().getColor(R.color.gray_text));
//                    v.setSelected(false);
//                    if (labels.size() <= 0) {
//                        nextButton.setEnabled(false);
//                    }
//                }
            }
        });
        return textView;
    }


}
