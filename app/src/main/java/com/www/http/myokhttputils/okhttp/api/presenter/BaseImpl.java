package com.www.http.myokhttputils.okhttp.api.presenter;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleProvider;


/**
 * Created by AED on 2017/10/9.
 */

public class BaseImpl {

    protected Context context;

    public BaseImpl(Context context) {
        this.context = context;
    }

    /**
     * 对 ACTIVITY 生命周期进行管理
     *
     * @return
     */
    protected LifecycleProvider
    getActivityLifecycleProvider() {

        LifecycleProvider provider = null;
        if (null != context &&
                context instanceof LifecycleProvider) {
            provider = (LifecycleProvider) context;
        }
        return provider;
    }

    public void doDestroy() {
        this.context = null;
    }
}
