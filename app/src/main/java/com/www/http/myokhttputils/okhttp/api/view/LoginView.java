package com.www.http.myokhttputils.okhttp.api.view;

import com.www.http.myokhttputils.okhttp.api.bean.LoginData;

/**
 * Created by AED on 2017/9/27.
 */

public interface LoginView {
        //api调用回调
        void onNetworkDisable();//网络未连接
        void onPre(); //加载中显示
        void onSuccess(LoginData ret);        //ret= 200 时返回
        void onError(String err_code,String err_msg);   // ret 不为200 时返回错误信息
        void onFailure(String message);   //网络请求失败
        void onFinish();
}
