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

/**
 * 服务器协议通信单元基类
 *
 * @author hanlixin
 */
abstract public class BaseProcess {

    private final String clazz = getClass().getSimpleName();
    private static final Logger logger = new Logger("protocol");
    protected static final String STATUS_TAG = "result_code";

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
    abstract protected void onResult(String result);

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

    private class AsyncComm extends AsyncTask<Void, Void, Void> {

        private String mRequestId = "";
        private ResponseListener mListener = null;

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
            if(url.indexOf("?")==-1){
                url=url+"?t="+token;
            }else{
                url=url+"&t="+token;
            }

            String parameter = getInfoParameter();
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
                onResult(getFakeResult());
                return null;
            }


            new HttpComm(false).post(url, parameter, new HttpResultCallback() {

                @Override
                public void onResponse(HttpDownloaderResult success, String url,
                                       String message) {

                    if (success == HttpDownloaderResult.eSuccessful) {
                        logger.d(String.format("recv name:%s url:%s result:%s",
                                clazz, url, message));
                        mStatus = ProcessStatus.Status.Success;
                        onResult(message);
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

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void result) {
            mListener.onResponse(mRequestId);
        }
    }
}
