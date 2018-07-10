package com.www.http.myokhttputils.okhttp.api.model;

import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.www.http.myokhttputils.okhttp.RetrofitWrapper;
import com.www.http.myokhttputils.okhttp.api.presenter.LoginPresenter;
import com.www.http.myokhttputils.okhttp.api.service.HttpService;
import com.www.http.myokhttputils.okhttp.api.httpUtils.StatusUtils;
import com.www.http.myokhttputils.okhttp.api.httpUtils.NetWorkUtils;
import com.www.http.myokhttputils.okhttp.api.bean.LoginData;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AED on 2017/9/27.
 */

public class LoginModelImpl implements LoginModel {

    @Override
    public void LoginCall(HashMap<String, String> map, LifecycleProvider provider, final LoginPresenter loginPresenter) {
        if (!NetWorkUtils.checkNetworkConnect()) {  //需判断 网络是否 连接
            loginPresenter.onNetworkDisable();
            return;
        }
        if (map.size() <= 0) return;  //在这里 可做非空判断
        loginPresenter.onPre();
        HttpService service = RetrofitWrapper.getInstance().create(HttpService.class);
        service.login(map)
                .subscribeOn(Schedulers.io())
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("Bruce","----------onSubscribe--------------");
                    }

                    @Override
                    public void onNext(LoginData value) {
                        if (value.code.equals(StatusUtils.SUCCESS)) {
                            loginPresenter.onSuccess(value);
                            loginPresenter.onFinish();
                        } else {
                            loginPresenter.onError(value.code, value.message);
                            loginPresenter.onFinish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginPresenter.onFailure(e.getMessage());
                        loginPresenter.onFinish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d("Bruce","----------onComplete--------------");
                    }
                });
    }
}
