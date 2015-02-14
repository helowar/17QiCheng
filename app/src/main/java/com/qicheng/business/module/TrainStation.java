package com.qicheng.business.module;

/**
 * Created by NO1 on 2015/2/6.
 */
public class TrainStation {

    private String stationCode;

    private String stationName;

    private String leaveTime;

    private int crossDays;

    private int index;

    public TrainStation(){

    }

    public TrainStation(String stationCode, String stationName,int index) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.index = index;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getCrossDays() {
        return crossDays;
    }

    public void setCrossDays(int crossDays) {
        this.crossDays = crossDays;
    }
}
