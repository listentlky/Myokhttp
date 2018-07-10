# 当前潮流的 retrofit 2.0 + okhttp3.0 + gson + rxjava2+ ，基于MVP模式封装的网络请求框架；
 
# app 目录下 buil.gradle

dependencies {

    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.okhttp3:okhttp:3.4.1'
    compile 'com.google.code.gson:gson:2.7'
    compile 'com.squareup.retrofit2:converter-gson:2.0.2';
    compile 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'io.reactivex.rxjava2:rxjava:2.0.1'

}


 
# 建立工厂类 构建请求

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


public class LoginPresenterImpl implements LoginPresenter {

    private LoginView loginView;
    private LoginModel loginApi;

    public LoginPresenterImpl(LoginView Callback) {
        this.loginView = Callback;
        this.loginApi = new LoginModelImpl();
    }

    @Override
    public void onReadyLogin(HashMap<String, String> map) {
        loginApi.LoginCall(map,this);
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
    }
}



 
# 编写 Model 接口类
