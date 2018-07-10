package com.www.http.myokhttputils.okhttp.api.presenter;

import android.content.Context;

import com.www.http.myokhttputils.okhttp.api.model.LoginModel;
import com.www.http.myokhttputils.okhttp.api.model.LoginModelImpl;
import com.www.http.myokhttputils.okhttp.api.view.LoginView;
import com.www.http.myokhttputils.okhttp.api.bean.LoginData;

import java.util.HashMap;


/**
 * Created by AED on 2017/9/27.
 */

public class LoginPresenterImpl extends BaseImpl implements LoginPresenter {

    private LoginView loginView;
    private LoginModel loginApi;

    public LoginPresenterImpl(Context context ,LoginView Callback) {
        super(context);
        this.loginView = Callback;
        this.loginApi = new LoginModelImpl();
    }

    @Override
    public void onReadyLogin(HashMap<String, String> map) {
        loginApi.LoginCall(map,getActivityLifecycleProvider(),this);
    }

    @Override
    public void onNetworkDisable() {
        loginView.onNetworkDisable();
    }

    @Override
    public void onPre() {
        loginView.onPre();
    }

    @Override
    public void onFailure(String msg) {
        loginView.onFailure(msg);
    }

    @Override
    public void onSuccess(LoginData ret) {
        loginView.onSuccess(ret);
    }

    @Override
    public void onError(String code, String message) {
        loginView.onError(code, message);
    }

    @Override
    public void onFinish() {
        loginView.onFinish();
        doDestroy();
    }
}
