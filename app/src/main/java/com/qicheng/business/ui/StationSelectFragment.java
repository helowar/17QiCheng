package com.qicheng.business.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qicheng.R;
import com.qicheng.business.logic.LogicFactory;
import com.qicheng.business.logic.TripLogic;
import com.qicheng.business.logic.event.TripEventArgs;
import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.Trip;
import com.qicheng.business.ui.component.AutoBreakAndNextReverseLineViewGroup;
import com.qicheng.business.ui.component.BadgeView;
import com.qicheng.framework.event.EventArgs;
import com.qicheng.framework.event.EventId;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.ui.base.BaseFragment;
import com.qicheng.framework.ui.helper.Alert;
import com.qicheng.framework.util.DateTimeUtil;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationSelectFragment extends BaseFragment implements Serializable {

    private static final Logger logger = new Logger("com.qicheng.business.ui.StationSelectFragment");

    private AutoBreakAndNextReverseLineViewGroup stationGroup;

    public static final String EXTRA_TRIP = "com.qicheng.business.ui.StationSelectFragment.RESULT_TRIP";

    /**
     * 参数Key
     */
    public static final String PARAM_TRAIN_CODE_KEY = "trainCode";
    public static final String PARAM_TRIP_DATE_KEY = "tripDate";
    public static final String PARAM_STATION_LIST_KEY = "stationList";

    private String mTrainCode;
    private String mTripDate;
    private ArrayList<TrainStation> mTrainStations;

    private int mCarSharing=2;
    private int mTravelTogether=1;
    private int mStayDays=1;
    private TrainStation mStartStation;
    private TrainStation mStopStation;

    private boolean startSet = false;
    private boolean stopSet = false;

    private EditText viewStayDays;

    private List<BadgeView> badgeList = new ArrayList<BadgeView>();
    private List<TextView> stationViewList = new ArrayList<TextView>();


    public StationSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        ActionBar bar = getActivity().getActionBar();
        if(bar!=null){
            bar.setTitle(getResources().getString(R.string.station_select_activity_title));
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        Bundle args = intent.getExtras();
        /**
         * 获取参数
         */
        mTrainCode =  args.getString(PARAM_TRAIN_CODE_KEY);
        mTripDate = args.getString(PARAM_TRIP_DATE_KEY);
        mTrainStations = (ArrayList<TrainStation>)args.getSerializable(PARAM_STATION_LIST_KEY);
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_station_select, (ViewGroup)getActivity().findViewById(R.id.trip_add_fragment), false);
        stationGroup = (AutoBreakAndNextReverseLineViewGroup)convertView.findViewById(R.id.station_view_group);
        //TODO get trainstations
        initLineStations(stationGroup);
        if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stationGroup.setBackgroundDrawable(new StationSelecter());
        } else {
            stationGroup.setBackground(new StationSelecter());
        }
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
                    param.setStartStationCode(mStartStation.getStationCode());
                    param.setStartStationName(mStartStation.getStationName());
                    param.setEndStationCode(mStopStation.getStationCode());
                    param.setEndStationName(mStopStation.getStationName());
                    param.setStartTime(mTripDate + mStartStation.getLeaveTime().replace(":", ""));
                    param.setStopTime(getEndDttm(mStopStation.getEnterTime(), mTripDate, mStopStation.getCrossDays() - mStartStation.getCrossDays()));
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
                                    //记录友盟事件
                                    MobclickAgent.onEvent(getActivity(), Const.MobclickAgent.EVENT_ADD_TRIP);
                                    //更新底部福利数量提示
                                    Const.Application.getBenefitChangedListener().updateBenefitBadge(result.getTrip().getValidBenefit());
                                    sendResult(Const.ActivityResultCode.RESULT_SUCCESS,result.getTrip());
                                    getActivity().finish();
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
            TextView stationView = setTextViewToGroup(mTrainStations.get(i));
            stationViewList.add(stationView);
            stationGroup.addView(stationView);
            BadgeView stationBadge  = new BadgeView(getActivity());
            stationBadge.setHideOnNull(true);
            stationBadge.setBadgeMargin(0);
            stationBadge.setTargetView(stationView);
            stationBadge.setText(null);
            badgeList.add(stationBadge);
        }

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
                int index = station.getIndex()-1;
                //确保起始站不是最后一站
                if(startSet==false&&station.getIndex()==mTrainStations.size()){
                    //不响应
                    return;
                }
                //确保起止站不相同
                if(station==mStartStation||station==mStopStation){
                    //不响应
                    return;
                }
                if(startSet!=false&&(startSet&&stopSet==false)){
                    if(station.getIndex()<mStartStation.getIndex()){
                        return;
                    }
                    badgeList.get(index).setText("止");
                    stopSet=true;//设置到达站
                    mStopStation = station;
                }else{
                    //排除重设直接选末站
                    if(startSet&&stopSet&&station.getIndex()==mTrainStations.size()){
                        return;
                    }
                    //新设或重设起止站
                    startSet = true;
                    stopSet = false;
                    mStartStation = station;
                    clearBadge();
                    badgeList.get(index).setText("起");
                    mStopStation = null;
                }
                if (stopSet){
                    v.setBackgroundResource(R.drawable.bg_station_selected);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    for(int i = index+1;i<stationViewList.size();i++){
                        TextView child = stationViewList.get(i);
                        child.setBackgroundResource(R.drawable.bg_station_unavailable);
                        child.setTextColor(getResources().getColor(R.color.main));
                    }
                }else{
                    v.setBackgroundResource(R.drawable.bg_station_selected);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.white));
                    for(int i = 0;i<index;i++){
                        TextView child = stationViewList.get(i);
                        child.setBackgroundResource(R.drawable.bg_station_unavailable);
                        child.setTextColor(getResources().getColor(R.color.main));
                    }
                    for(int i = index+1;i<stationViewList.size();i++){
                        TextView child = stationViewList.get(i);
                        child.setBackgroundResource(R.drawable.bg_station_unselected);
                        child.setTextColor(getResources().getColor(R.color.main));
                    }
                }

            }
        });
        return textView;
    }

    private void clearBadge(){
        for(BadgeView i :badgeList){
            i.setText(null);
        }
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

    private void sendResult(int resultCode,Trip resultTrip) {
        Intent i = new Intent();
        i.putExtra(EXTRA_TRIP, resultTrip);
        getActivity().setResult(resultCode,i);
    }

    private String getEndDttm(String endTime, String startDate, int crossDays) {
        if(crossDays > 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(DateTimeUtil.parseByyyyyMMdd(startDate));
            c.add(Calendar.DAY_OF_YEAR, crossDays);
            startDate = DateTimeUtil.formatByyyyyMMdd(c.getTime());
        }
        return startDate + endTime.replace(":","");
    }
}
