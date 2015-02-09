package com.qicheng.business.module;

/**
 * Created by NO1 on 2015/2/6.
 */
public class TrainStation {

    private String stationCode;

    private String stationName;

    public TrainStation(String stationCode, String stationName) {
        this.stationCode = stationCode;
        this.stationName = stationName;
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
}
