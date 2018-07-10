package com.www.http.myokhttputils.okhttp.api.httpUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AED on 2017/9/27.
 */

public class NetWorkUtils {

    public static Context context;
    /**
     * 检查网络是否连接
     * context 全局context
     * @return true 已连接 false 未连接
     */
    public static Boolean checkNetworkConnect() {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }
}
