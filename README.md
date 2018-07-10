# MVP架构 基于 retrofit 2.0 + okhttp3.0 + gson + rxjava2+rxlifecycle2 的网络请求框架，绑定生命周期，避免内存泄露；
 
# app 目录下 buil.gradle

dependencies {

    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.okhttp3:okhttp:3.4.1'
    compile 'com.google.code.gson:gson:2.7'
    compile 'com.squareup.retrofit2:converter-gson:2.0.2';
    compile 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'io.reactivex.rxjava2:rxjava:2.0.1'
    compile 'com.trello.rxlifecycle2:rxlifecycle:2.1.0'
    compile 'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
}


 
# okhttp 配置  构建请求类

public class RetrofitWrapper {

    private static RetrofitWrapper instance;
    private OkHttpClient okHttpClient;
    private Retrofit mRetrofit;

    public RetrofitWrapper() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static RetrofitWrapper getInstance() {

        if (instance == null) {
            synchronized (RetrofitWrapper.class) {
                if (instance == null) {
                    instance = new RetrofitWrapper();
                }
            }
        }

        return instance;
    }

    /**
     * 转换为对象的Service
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }

    /**
     * 常量类 基本的URL
     */
    public class Constant {
        //BASE_URL 可以自行替换
        public static final String BASE_URL = "******************************";
    }
}
 
# 创建 Httpservice

public interface HttpService {

    //登录
    @Headers("client: CLIENT")
    @POST("account/login")
    Observable<LoginData> login(@QueryMap HashMap<String, String> map);
}
 
# 编写 VIEW 层 接口

public interface LoginView {

        //api调用回调
        void onNetworkDisable();//网络未连接
        
        void onPre(); //加载中显示
        
        void onSuccess(LoginData ret);        //ret= 200 时返回
        
        void onError(String err_code,String err_msg);   // ret 不为200 时返回错误信息
        
        void onFailure(String message);   //网络请求失败
        
        void onFinish();
}

 
# 编写 Presenter 接口 以及 实现类

public interface LoginPresenter {

    void onReadyLogin(HashMap<String,String> map);
    
    void onNetworkDisable();
    
    void onPre();
    
    void onFailure(String msg);
    
    void onSuccess(LoginData ret);
    
    void onError(String code, String message);
    
    void onFinish();
}


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
        doDestroy();  // 回收 context
    }
}



 
# 编写 Model 接口 以及实现类

public interface LoginModel {

    void LoginCall(HashMap<String,String> map, LifecycleProvider provider, LoginPresenter model);
    
}



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
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))  ** 这里 与activity 生命周期相绑定   注意 View 继承 fragment 这里则为FragmentEvent.DESTROY
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


# 贴出 baseImpl 

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
 
# 最后页面调用   这里注意  使用 rxlifecycle2 绑定 activity生命周期 必须继承 RxActivity/RxAppCompatActivity/RxFragment、
# /RxFragmentActivity等；也就是说 使用封装的 父类

LoginPresenterImpl loginPresenter = new LoginPresenterImpl(this, new LoginView() {

            @Override
            public void onNetworkDisable() {
                Log.d("Bruce","----------无网络-----------");
            }

            @Override
            public void onPre() {
                Log.d("Bruce","----------开始加载-----------");
            }

            @Override
            public void onSuccess(LoginData ret) {
                Log.d("Bruce","----------接口/数据成功-----------");
            }

            @Override
            public void onError(String err_code, String err_msg) {
                Log.d("Bruce","----------接口成功 数据不成功-----------");
            }

            @Override
            public void onFailure(String message) {
                Log.d("Bruce","----------接口请求失败-----------");
            }

            @Override
            public void onFinish() {
                Log.d("Bruce","----------网络连接结束-----------");
            }
        });
        loginPresenter.onReadyLogin(new HashMap<String, String>());
    }
  
# OVER；
