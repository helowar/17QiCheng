package com.qicheng.business.logic;

import com.qicheng.business.logic.event.UserEventArgs;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.protocol.TravellerPersonProcess;
import com.qicheng.business.protocol.TravellerRecommendPersonProcess;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;
import com.qicheng.util.Const;

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
     * @param listener      查询结果事件监听器
     */
    public void queryUserByStation(String station, byte orderBy, String lastLoginTime, int size, final EventListener listener) {
        queryUser(Const.QUERY_TYPE_STATION, station, orderBy, lastLoginTime, size, listener);
    }

    /**
     * 根据车次，查询含有该车次的行程所属用户信息。
     *
     * @param train         车次
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     * @param listener      查询结果事件监听器
     */
    public void queryUserByTrain(String train, byte orderBy, String lastLoginTime, int size, final EventListener listener) {
        queryUser(Const.QUERY_TYPE_TRAIN, train, orderBy, lastLoginTime, size, listener);
    }

    /**
     * 根据查询类型和查询值，查询对应的普通用户信息。
     *
     * @param queryType     查询类型 -1：全站 0：车站 1：出发 2：到达 3：车次 4：未上车 5：上车 6：下车 7：附近 8：城市
     * @param queryValue    查询值
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     * @param listener      查询结果事件监听器
     */
    public void queryUser(byte queryType, String queryValue, byte orderBy, String lastLoginTime, int size, final EventListener listener) {
        final TravellerPersonProcess process = new TravellerPersonProcess();
        process.setInfoParameter(queryType, queryValue, orderBy, lastLoginTime, size);
        process.run("queryUser", new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("查询普通用户信息结果码为：" + errCode);
                UserEventArgs userEventArgs = new UserEventArgs(process.getUserList(), errCode);
                // 发送事件
                fireEvent(listener, userEventArgs);
            }
        });
    }

    /**
     * 查询推荐用户信息。
     *
     * @param orderBy       查询方向 0：往最新方向查询 1：往最早方向查询
     * @param lastLoginTime 最后登录时间
     * @param size          查询个数
     * @param listener      查询结果事件监听器
     */
    public void queryRecommendUser(byte orderBy, String lastLoginTime, int size, final EventListener listener) {
        final TravellerRecommendPersonProcess process = new TravellerRecommendPersonProcess();
        process.setInfoParameter(orderBy, lastLoginTime, size);
        process.run("queryRecommendUser", new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                logger.d("查询推荐用户信息结果码为：" + errCode);
                UserEventArgs userEventArgs = new UserEventArgs(process.getUserList(), errCode);
                // 发送事件
                fireEvent(listener, userEventArgs);
            }
        });
    }
}
