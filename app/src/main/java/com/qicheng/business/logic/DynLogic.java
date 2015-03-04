package com.qicheng.business.logic;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.logic.event.DynEventAargs;
import com.qicheng.business.logic.event.StationEventAargs;
import com.qicheng.business.protocol.GetDynListProcess;
import com.qicheng.business.protocol.GetStationListProcess;
import com.qicheng.business.protocol.GetTripRelatedCityList;
import com.qicheng.business.protocol.GetTripRelatedTrainList;
import com.qicheng.business.protocol.InteractProcess;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.business.ui.ActyFragment;
import com.qicheng.framework.event.EventListener;
import com.qicheng.framework.event.OperErrorCode;
import com.qicheng.framework.logic.BaseLogic;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.StringUtil;

/**
 * Created by NO3 on 2015/2/28.
 */
public class DynLogic extends BaseLogic {

    /*工厂方法创建DynLogic*/
    static class Factory implements BaseLogic.Factory {
        @Override
        public BaseLogic create() {
            return new DynLogic();
        }
    }

    /*获取动态列表的Logic方法*/
    public void getDynList(ActyFragment.DynSearch dynSearch, final EventListener listener) {
        final GetDynListProcess process = new GetDynListProcess();
        process.setDynSearch(dynSearch);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                DynEventAargs dynEventAargs = new DynEventAargs(process.getDynList(), errCode);
                fireEvent(listener, dynEventAargs);
            }
        });

    }

    public void interact(String id,byte action,final EventListener listener){
        final InteractProcess process = new InteractProcess();
        process.setId(id);
        process.setAction(action);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                DynEventAargs dynEventAargs = new DynEventAargs(errCode);
                fireEvent(listener, dynEventAargs);
            }
        });
    }


    public void getStationList(String cityCode,final EventListener listener){
        final GetStationListProcess process = new GetStationListProcess();
        process.setCityCode(cityCode);
        process.run(new ResponseListener() {
            @Override
            public void onResponse(String requestId) {
                OperErrorCode errCode = ProcessStatus.convertFromStatus(process.getStatus());
                StationEventAargs stationEventAargs = new StationEventAargs(process.getStationList(),errCode);
                fireEvent(listener, stationEventAargs);
            }
        });

    }

}
