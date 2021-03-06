package com.qicheng.framework.protocol;

import android.os.AsyncTask;

import com.qicheng.business.cache.Cache;
import com.qicheng.business.module.User;
import com.qicheng.business.protocol.ProcessStatus;
import com.qicheng.framework.net.HttpComm;
import com.qicheng.framework.net.HttpResultCallback;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 服务器协议通信单元基类
 *
 * @author hanlixin
 */
abstract public class BaseProcess {

    private final String clazz = getClass().getSimpleName();
    private static final Logger logger = new Logger("protocol");


    /**
     * 获得请求地址
     */
    abstract protected String getRequestUrl();

    /**
     * 获得 请求时的info参数，派生类实现
     */
    abstract protected String getInfoParameter();

    /**
     * 通信完成，解析结果，派生类实现
     */
    abstract protected void onResult(JSONObject result);

    /**
     * 获得测试用假数据
     */
    abstract protected String getFakeResult();

    /**
     * 通信结果错误码
     */
    private ProcessStatus.Status mStatus = ProcessStatus.Status.Success;

    public ProcessStatus.Status getStatus() {
        return mStatus;
    }

    protected void setStatus(ProcessStatus.Status value) {
        mStatus = value;
    }

    public void run() {
        run(new EmptyResponseListener());
    }

    public void run(ResponseListener listener) {
        run(null, listener);
    }

    public void run(String requestId, ResponseListener listener) {
        new AsyncComm(requestId, listener).execute();
    }

    protected void onCreate() {
    }

    protected void setProcessStatus(int resultCode) {
        switch (resultCode) {
            case Const.ResponseResultCode.RESULT_SUCCESS:
                mStatus = ProcessStatus.Status.Success;
                break;
            case Const.ResponseResultCode.RESULT_FAIL:
                mStatus = ProcessStatus.Status.ErrFailure;
                break;
            case Const.ResponseResultCode.RESULT_ILLEGAL_CALL:
                mStatus = ProcessStatus.Status.IllegalRequest;
                break;
            case Const.ResponseResultCode.RESULT_NOT_LOGINED:
                mStatus = ProcessStatus.Status.ErrNotLogin;
                break;
            case Const.ResponseResultCode.RESULT_LOGIN_TIMEOUT:
                mStatus = ProcessStatus.Status.ErrLoginTimeOut;
                break;
            case Const.ResponseResultCode.RESULT_CELL_NUM_EXIST:
                mStatus = ProcessStatus.Status.ErrExistCellNum;
                break;
            case Const.ResponseResultCode.RESULT_CELL_NUM_NOT_EXIST:
                mStatus = ProcessStatus.Status.ErrCellNumNotExist;
                break;
            case Const.ResponseResultCode.RESULT_VERIFY_CODE_ERROR:
                mStatus = ProcessStatus.Status.ErrWrongVerCode;
                break;
            case Const.ResponseResultCode.RESULT_VERIFY_CODE_INVALID:
                mStatus = ProcessStatus.Status.ErrVerCodeExpire;
                break;
            case Const.ResponseResultCode.RESULT_PWD_ERROR:
                mStatus = ProcessStatus.Status.ErrPass;
                break;
            case Const.ResponseResultCode.RESULT_USER_NAME_NOT_EXIST:
                mStatus = ProcessStatus.Status.ErrUserNameNotExist;
                break;
            case Const.ResponseResultCode.RESULT_USER_NAME_INVALID:
                mStatus = ProcessStatus.Status.ErrUidInvalid;
                break;
            case Const.ResponseResultCode.RESULT_NICKNAME_EXIST:
                mStatus = ProcessStatus.Status.ErrVerCodeExpire;
                break;
            case Const.ResponseResultCode.RESULT_EXCEPTION:
                mStatus = ProcessStatus.Status.ErrFailure;
                break;
            case Const.ResponseResultCode.RESULT_NO_GRAB:
                mStatus = ProcessStatus.Status.ResultNoGrab;
                break;
            case Const.ResponseResultCode.RESULT_DISTRIBUTE_FINISHED:
                mStatus = ProcessStatus.Status.ResultDistributeFinished;
                break;
            case Const.ResponseResultCode.RESULT_NO_BENEFIT:
                mStatus = ProcessStatus.Status.ResultNoBenefit;
                break;
            case Const.ResponseResultCode.RESULT_BODY_IS_NOT_EXIST:
                mStatus = ProcessStatus.Status.InfoNoData;
                break;
            default:
                mStatus = ProcessStatus.Status.ErrUnkown;
        }
    }

    private class AsyncComm extends AsyncTask<Void, Void, Void> {

        private String mRequestId = "";
        private ResponseListener mListener = null;
        private String parameter;

        public AsyncComm(String requestId, ResponseListener listener) {
            mRequestId = requestId;
            mListener = listener;
        }

        @Override
        protected Void doInBackground(Void... params) {

            onCreate();
            //从相对路径拼为绝对路径
            String url = Const.BASE_URL+getRequestUrl();
            //加入TOKEN参数
            User cacheUser = Cache.getInstance().getUser();
            String token;
            if(cacheUser==null){
                token = "";
            }else{
                token = cacheUser.getToken();
            }
            //token="4JC6ZIXW9RDO8GDQT6SCJG1O9EXFHP92YT4W4LHY8RNZLWYLNZM4T8YRUILUVAZLSEC7PEAU0DX";
            if(url.indexOf("?")==-1){
                url=url+"?t="+token;
            }else{
                url=url+"&t="+token;
            }

            parameter = getInfoParameter();
            if (!StringUtil.isEmpty(parameter)) {
                parameter = parameter.replace("\\/", "/");
            }
            logger.d(String.format("send name:%s url:%s param:%s",
                    clazz, url, parameter));

            if (StringUtil.isEmpty(url)) {
                mStatus = ProcessStatus.Status.ErrUnkown;
                return null;
            }

            if (Const.FakeProtocol) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
//                onResult(getFakeResult());
                return null;
            }


            final HttpComm comm  = new HttpComm(false);
            comm.post(url, parameter, new HttpResultCallback() {

                @Override
                public void onResponse(HttpDownloaderResult success, String url,
                                       String message) {

                    if (success == HttpDownloaderResult.eSuccessful) {
                        logger.d(String.format("recv name:%s url:%s result:%s",
                                clazz, url, message));
                        mStatus = ProcessStatus.Status.Success;
                        try{
                            JSONObject o = new JSONObject(message);
                            if(commonExceptionHandler(o)){
                                onResult(o);
                            }else{
                                //可自恢复的异常
                                onResult(Const.Application.reLoginAndRepeat(url,parameter));
                            }
                        }catch (JSONException e){
                            logger.d("recv response url:" + url + "; fail");
                            mStatus = ProcessStatus.Status.ErrNetDisable;
                        }
                    } else {
                        logger.d("recv response url:" + url + "; fail");
                        mStatus = ProcessStatus.Status.ErrNetDisable;
                    }
                }

                @Override
                public void onProgress(String url, float rate) {
                }
            });
            return null;
        }

        private boolean commonExceptionHandler(JSONObject message){
           int resultCode =  message.optInt("result_code");
           switch (resultCode){
               case Const.ResponseResultCode.RESULT_NOT_LOGINED:
                   return false;
               case Const.ResponseResultCode.RESULT_LOGIN_TIMEOUT:
                   return false;
               default:
                   return true;
           }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            mListener.onResponse(mRequestId);
        }

    }
}
