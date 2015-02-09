package com.qicheng.business.protocol;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.qicheng.business.cache.Cache;
import com.qicheng.framework.net.HttpComm;
import com.qicheng.framework.net.HttpResultCallback;
import com.qicheng.framework.protocol.FileImageUpload;
import com.qicheng.framework.protocol.ResponseListener;
import com.qicheng.framework.util.Logger;
import com.qicheng.framework.util.StringUtil;
import com.qicheng.util.Const;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by NO1 on 2015/2/8.
 */
public class ImageUploadProcess {

    private final String clazz = getClass().getSimpleName();
    private static final Logger logger = new Logger("protocol");
    protected static final String STATUS_TAG = "result_code";

    private static final String url ="http://192.168.1.107:8080/qps/common/upload.html";
    /**
     * 文件用途
     */
    private int fileUsage = 0;
    public static final int USAGE_VIDEO = 2;
    public static final int USAGE_PORTRAIT = 1;
    public static final int USAGE_COMMON=0;

    private File imageToUpload;

    private String resultUrl = null;


    /** 通信结果错误码 */
    private ProcessStatus.Status mStatus = ProcessStatus.Status.Success;
    public ProcessStatus.Status getStatus() {
        return mStatus;
    }

    public void run(String requestId, int usage, File image, ResponseListener listener) {
        fileUsage = usage;
        imageToUpload = image;
        new AsyncComm(requestId, listener).execute();
    }

    private String getRequestUrl(){
        return url+"?fileUsage="+fileUsage+"&token="+ Cache.getInstance().getUser().getToken();
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

            String url = getRequestUrl();

            if(StringUtil.isEmpty(url)) {
                mStatus = ProcessStatus.Status.ErrUnkown;
                return null;
            }
            if(imageToUpload==null){
                mStatus = ProcessStatus.Status.ErrUnkown;
                return null;
            }
            FileImageUpload fileImageUpload = new FileImageUpload();
            FileImageUpload.UploadResult result = fileImageUpload.uploadFile(imageToUpload,url);

            if(result.getResult()== FileImageUpload.SUCCESS){
                mStatus = ProcessStatus.Status.Success;
                resultUrl = result.getUrl();
                return null;
            }
            mStatus = ProcessStatus.Status.ErrUnkown;
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

    public String getResultUrl() {
        return resultUrl;
    }
}
