package com.qicheng.business.logic;

import com.qicheng.business.module.User;
import com.qicheng.business.protocol.TravellerPersonProcess;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

import java.util.List;

/**
 * TravellerPersonLogic.java是启程APP的查询同路用户业务逻辑类。
 *
 * @author 花树峰
 * @version 1.0 2015年2月8日
 */
public class TravellerPersonLogic extends BaseLogic {

    private static Logger logger = new Logger("com.qicheng.business.logic.TravellerPersonLogic");

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new TravellerPersonLogic();
        }
    }

    /**
     * 根据车站代码，查询含有该车站的行程所属用户信息。
     *
     * @param station       车站代码
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByStation(String station, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_STATION, station, orderBy, lastLoginTime, size);
    }

    /**
     * 根据出发车站代码，查询含有该车站的行程所属用户信息。
     *
     * @param startStation  出发车站代码
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByStartStation(String startStation, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_BEGIN, startStation, orderBy, lastLoginTime, size);
    }

    /**
     * 根据到达车站代码，查询含有该车站的行程所属用户信息。
     *
     * @param endStation    到达车站代码
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByEndStation(String endStation, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_END, endStation, orderBy, lastLoginTime, size);
    }

    /**
     * 根据车次，查询含有该车次的行程所属用户信息。
     *
     * @param train         车次
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByTrain(String train, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_TRAIN, train, orderBy, lastLoginTime, size);
    }

    /**
     * 根据车次，查询含有该车次的未上车的行程所属用户信息。
     *
     * @param train         车次
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByNotOnCar(String train, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_NOT_ON_CAR, train, orderBy, lastLoginTime, size);
    }

    /**
     * 根据车次，查询含有该车次的已上车的行程所属用户信息。
     *
     * @param train         车次
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByOnCar(String train, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_ON_CAR, train, orderBy, lastLoginTime, size);
    }

    /**
     * 根据车次，查询含有该车次的已下车的行程所属用户信息。
     *
     * @param train         车次
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUserByOffCar(String train, byte orderBy, String lastLoginTime, int size) {
        queryUser(Const.USER_QUERY_TYPE_OFF_CAR, train, orderBy, lastLoginTime, size);
    }

    /**
     * 根据查询类型和查询值，查询对应的用户信息。
     *
     * @param queryType     查询类型 0：车站 1：出发 2：到达 3：车次 4：未上车 5：上车 6：下车
     * @param queryValue    查询值
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     */
    public void queryUser(byte queryType, String queryValue, byte orderBy, String lastLoginTime, int size) {
        TravellerPersonProcess process = new TravellerPersonProcess();
        process.setInfoParameter(queryType, queryValue, orderBy, lastLoginTime, size);
        process.run("queryUser", new ResponseListener() {
            @Override
            public void onResponse(String requestId) {

            }
        });
    }
}
