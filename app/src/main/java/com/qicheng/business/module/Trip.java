package com.qicheng.business.module;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by NO1 on 2015/1/22.
 */
public class Trip implements Serializable {

    private String startStationCode;

    private String startStationName;

    private String endStationCode;

    private String endStationName;

    private int orderNum;

    private String tripDate;

    private String startTime;

    private String stopTime;

    private String trainCode;

    private ArrayList<String> startUserList;

    private ArrayList<String> stopUserList;

    private ArrayList<String> trainUserList;

    private int carSharing = 2;

    private int travelTogether = 1;

    private int stayDays = 1;

    private int validBenefit;

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getStartStationCode() {
        return startStationCode;
    }

    public void setStartStationCode(String startStationCode) {
        this.startStationCode = startStationCode;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationCode() {
        return endStationCode;
    }

    public void setEndStationCode(String endStationCode) {
        this.endStationCode = endStationCode;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public ArrayList<String> getStartUserList() {
        return startUserList;
    }

    public void setStartUserList(ArrayList<String> startUserList) {
        this.startUserList = startUserList;
    }

    public ArrayList<String> getStopUserList() {
        return stopUserList;
    }

    public void setStopUserList(ArrayList<String> stopUserList) {
        this.stopUserList = stopUserList;
    }

    public ArrayList<String> getTrainUserList() {
        return trainUserList;
    }

    public void setTrainUserList(ArrayList<String> trainUserList) {
        this.trainUserList = trainUserList;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getCarSharing() {
        return carSharing;
    }

    public void setCarSharing(int carSharing) {
        this.carSharing = carSharing;
    }

    public int getTravelTogether() {
        return travelTogether;
    }

    public void setTravelTogether(int travelTogether) {
        this.travelTogether = travelTogether;
    }

    public int getStayDays() {
        return stayDays;
    }

    public void setStayDays(int stayDays) {
        this.stayDays = stayDays;
    }

    public int getValidBenefit() {
        return validBenefit;
    }

    public void setValidBenefit(int validBenefit) {
        this.validBenefit = validBenefit;
    }
}
