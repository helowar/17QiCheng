package com.qicheng.business.logic;

import com.qicheng.business.logic.event.TripEventArgs;
import com.qicheng.business.module.Trip;
import com.qicheng.business.protocol.AddTripProcess;
import com.qicheng.business.protocol.GetTrainInfoProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.protocol.TripListProcess;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;

/**
 * Created by NO1 on 2015/2/11.
 */
public class TripLogic extends BaseLogic{

    private static Logger logger = new Logger("com.qicheng.business.logic.BaseLogic");

    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new TripLogic();
        }
    }

    /**
     * 获取用户行程列表
     * @param listener
     */
    public void getPersonalTripList(int lastTripOrderNum,final EventListener listener){
        final TripListProcess process = new TripListProcess();
        process.setParam(lastTripOrderNum);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                TripEventArgs tripEventArgs = new TripEventArgs(process.getResult(), errCode);
                //发送事件
                fireEvent(listener, tripEventArgs);

            }
        });
    }

    /**
     * 获取一趟列车的时刻信息
     * @param trainCode
     * @param listener
     */
    public void getTrainInfo(String trainCode,final EventListener listener){
        final GetTrainInfoProcess process = new GetTrainInfoProcess();
        process.setParam(trainCode);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                TripEventArgs tripEventArgs = new TripEventArgs(errCode);
                tripEventArgs.setTrainStations(process.getResult());
                //发送事件
                fireEvent(listener, tripEventArgs);

            }
        });

    }

    /**
     * 保存行程
     * @param inputTrip
     * @param listener
     */
    public void saveTrip(Trip inputTrip,final EventListener listener){
        final AddTripProcess process = new AddTripProcess();
        process.setParam(inputTrip);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                // 状态转换：从调用结果状态转为操作结果状态
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                TripEventArgs tripEventArgs = new TripEventArgs(errCode);
                tripEventArgs.setTrip(process.getResult());
                //发送事件
                fireEvent(listener, tripEventArgs);
            }
        });

    }

    /**
     * 重新从服务端获取并更新列车清单
     * 仅在发现有不能自动完成的G/D列车时调用
     */
    public void freshTrainList(){

    }

}
