package com.qicheng.business.logic.event;

import com.qicheng.business.module.TrainStation;
import com.qicheng.business.module.Trip;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.event.StatusEventArgs;

import java.util.ArrayList;

/**
 * Created by NO1 on 2015/2/12.
 */
public class TripEventArgs extends StatusEventArgs {

    private ArrayList<Trip> mTripList;

    private ArrayList<TrainStation> mTrainStations;

    private Trip mTrip;

    public TripEventArgs(OperErrorCode errCode){
        super(errCode);
    }

    public TripEventArgs(ArrayList<Trip> tripList, OperErrorCode errCode){
        super(errCode);
        this.mTripList = tripList;
    }

    public TripEventArgs(Trip trip, OperErrorCode errCode){
        super(errCode);
        this.mTrip = trip;
    }

    @Override
    public OperErrorCode getErrCode() {
        return super.getErrCode();
    }

    public ArrayList<Trip> getTripList() {
        return mTripList;
    }

    public void setTripList(ArrayList<Trip> tripList) {
        mTripList = tripList;
    }

    public Trip getTrip() {
        return mTrip;
    }

    public void setTrip(Trip trip) {
        mTrip = trip;
    }

    public ArrayList<TrainStation> getTrainStations() {
        return mTrainStations;
    }

    public void setTrainStations(ArrayList<TrainStation> trainStations) {
        mTrainStations = trainStations;
    }
}
