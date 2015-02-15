package com.qicheng.business.ui;


import android.app.FragmentTransaction;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TripLogic;
import com.qicheng.business.logic.event.TripEventArgs;
import com.qicheng.business.module.Label;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.Trip;
import com.qicheng.business.ui.component.AutoBreakAndNextReverseLineViewGroup;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationSelectFragment extends BaseFragment {

    private AutoBreakAndNextReverseLineViewGroup stationGroup;

    /**
     * 参数Key
     */
    private static final String PARAM_TRAIN_CODE_KEY = "trainCode";
    private static final String PARAM_TRIP_DATE_KEY = "tripDate";
    private static final String PARAM_STATION_LIST_KEY = "stationList";

    private String mTrainCode;
    private String mTripDate;
    private ArrayList<TrainStation> mTrainStations;

    private int mCarSharing=2;
    private int mTravelTogether=1;
    private int mStayDays=1;
    private String mStartStationCode;
    private String mStopStationCode;

    private boolean startSet = false;
    private boolean stopSet = false;

    private View convertView;
    private EditText viewStayDays;


    public StationSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mTrainCode =  args.getString(PARAM_TRAIN_CODE_KEY);
        mTripDate = args.getString(PARAM_TRIP_DATE_KEY);
        mTrainStations = (ArrayList<TrainStation>)args.getSerializable(PARAM_STATION_LIST_KEY);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        convertView = inflater.inflate(R.layout.fragment_station_select, (ViewGroup)getActivity().findViewById(R.id.trip_add_fragment), false);
        forViewGroupTest();
//        stationGroup = (AutoBreakAndNextReverseLineViewGroup)convertView.findViewById(R.id.station_view_group);

        //TODO get trainstations
        //initLineStations(stationGroup);
        setFakeStations(stationGroup);
        if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stationGroup.setBackgroundDrawable(new StationSelecter());
        } else {
            stationGroup.setBackground(new StationSelecter());
        }
    }

    private void forViewGroupTest(){
        LinearLayout root =(LinearLayout)convertView.findViewById(R.id.label_scroll_root);
        View l = getActivity().getLayoutInflater().inflate(R.layout.train_station_map,null);
        stationGroup= (AutoBreakAndNextReverseLineViewGroup)l.findViewById(R.id.station_view_group);
        root.addView(l);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.getChildCount();
        RadioGroup viewCarSharing  = (RadioGroup)convertView.findViewById(R.id.radiobutton_car_sharing);
        viewCarSharing.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               switch (checkedId){
                   case R.id.radiobutton_wish:
                       mCarSharing = 2;
                       break;
                   case R.id.radiobutton_share:
                       mCarSharing = 1;
                       break;
                   default:
                       mCarSharing=0;
               }
            }
        });
        RadioGroup viewTravelTogether =  (RadioGroup)convertView.findViewById(R.id.radiobutton_travel_together);
        viewTravelTogether.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton_travel_yes:
                        mCarSharing = 1;
                        break;
                    default:
                        mCarSharing = 0;
                }
            }
        });
        viewStayDays = (EditText)convertView.findViewById(R.id.edittext_stay_time);
        convertView.findViewById(R.id.decrease_stay_time).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(viewStayDays.getText().toString())>0){
                    viewStayDays.setText((Integer.parseInt(viewStayDays.getText().toString())-1)+"");
                }
                mStayDays = Integer.parseInt(viewStayDays.getText().toString());
            }
        });
        convertView.findViewById(R.id.increase_stay_time).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(viewStayDays.getText().toString())>=0){
                    viewStayDays.setText((Integer.parseInt(viewStayDays.getText().toString())+1)+"");
                }
                Integer.parseInt(viewStayDays.getText().toString());
            }
        });

        convertView.findViewById(R.id.button_add).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startSet&&stopSet){
                    TripLogic logic = (TripLogic)LogicFactory.self().get(LogicFactory.Type.Trip);
                    /**
                     * 组装要保存的行程传入参数
                     */
                    Trip param = new Trip();
                    param.setTrainCode(mTrainCode);
                    param.setTripDate(mTripDate);
                    param.setStartStationCode(mStartStationCode);
                    param.setEndStationCode(mStopStationCode);
                    param.setCarSharing(mCarSharing);
                    param.setTravelTogether(mTravelTogether);
                    param.setStayDays(mStayDays);
                    logic.saveTrip(param,createUIEventListener(new EventListener() {
                        @Override
                        public void onEvent(EventId id, EventArgs args) {
                            stopLoading();
                            TripEventArgs result =  (TripEventArgs)args;
                            OperErrorCode errCode = result.getErrCode();
                            switch(errCode) {
                                case Success:
                                    Trip savedTrip = result.getTrip();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("trip",savedTrip);
                                    //TODO 返回数据到Activity
                                    break;
                                default:
//                                    Alert.Toast(getResources().getString(R.string.no_such_train_err));
                                    break;
                            }
                        }
                    }));
                    startLoading();
                }else{
                    Alert.Toast(R.string.station_no_select_err);
                }

            }
        });
        return convertView;

    }

    private void initLineStations(AutoBreakAndNextReverseLineViewGroup stationGroup){
        for(int i=0;i<mTrainStations.size();i++){
            stationGroup.addView(setTextViewToGroup(mTrainStations.get(i)));
        }
    }


    /**
     * 测试假数据方法
     * @param stationGroup
     */
    private void setFakeStations(AutoBreakAndNextReverseLineViewGroup stationGroup){
        stationGroup.addView(setTextViewToGroup(new TrainStation("hgh","上海虹桥",0)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("wx","无锡",1)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("sz","苏州",2)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("nj","南京",3)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("cz","常州",4)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("jn","济南",5)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("bj","北京",6)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("hgh","上海虹桥",7)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("wx","无锡",8)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("sz","苏州",9)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("nj","南京",10)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("cz","常州",11)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("jn","济南",12)));
        stationGroup.addView(setTextViewToGroup(new TrainStation("bj","北京",13)));
    }

    public TextView setTextViewToGroup(TrainStation station) {
        TextView textView = new TextView(getActivity());
        textView.setText(station.getStationName());
        textView.setTag(station);
        textView.setBackgroundResource(R.drawable.bg_station_unselected);
        textView.setTextColor(getResources().getColor(R.color.main));
//        final TrainStation station = new TrainStation();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainStation station = (TrainStation)v.getTag();
                int index = station.getIndex();
                if(startSet&&stopSet==false){
                    stopSet=true;//设置到达站
                    mStopStationCode = station.getStationCode();
                }else{
                    //新设或重设起止站
                    startSet = true;
                    stopSet = false;
                    mStartStationCode = station.getStationCode();
                }
                if (stopSet){
                    v.setBackgroundResource(R.drawable.bg_station_selected);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    for(int i = index+1;i<stationGroup.getChildCount();i++){
                        TextView child = (TextView)stationGroup.getChildAt(i);
                        child.setBackgroundResource(R.drawable.bg_station_unavailable);
                        child.setTextColor(getResources().getColor(R.color.main));
                    }
                }else{
                    v.setBackgroundResource(R.drawable.bg_station_selected);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    for(int i = 0;i<index;i++){
                        TextView child = (TextView)stationGroup.getChildAt(i);
                        child.setBackgroundResource(R.drawable.bg_station_unavailable);
                        child.setTextColor(getResources().getColor(R.color.main));
                    }
                    for(int i = index+1;i<stationGroup.getChildCount();i++){
                        TextView child = (TextView)stationGroup.getChildAt(i);
                        child.setBackgroundResource(R.drawable.bg_station_unselected);
                        child.setTextColor(getResources().getColor(R.color.main));
                    }
                }

            }
        });
        return textView;
    }

    private class StationSelecter extends Drawable{
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

    }


}
