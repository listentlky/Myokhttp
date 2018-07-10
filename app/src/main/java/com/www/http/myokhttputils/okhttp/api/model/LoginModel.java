package com.www.http.myokhttputils.okhttp.api.model;



import com.trello.rxlifecycle2.LifecycleProvider;
import com.www.http.myokhttputils.okhttp.api.presenter.LoginPresenter;

import java.util.HashMap;

/**
 * Created by AED on 2017/9/28.
 */

public interface LoginModel {
    void LoginCall(HashMap<String,String> map, LifecycleProvider provider, LoginPresenter model);
}
