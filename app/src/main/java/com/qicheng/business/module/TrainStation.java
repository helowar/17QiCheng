package com.qicheng.business.module;

import java.io.Serializable;

/**
 * Created by NO1 on 2015/2/6.
 */
public class TrainStation implements Serializable{

    private String stationCode;

    private String stationName;

    /**
     * 进站时间,格式:HH:mm
     */
    private String enterTime;

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

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrainStation{");
        sb.append("stationCode='").append(stationCode).append('\'');
        sb.append(", stationName='").append(stationName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
