package cn.ac.ict.alpha.models;

import cn.ac.ict.alpha.Entities.ResultEntity;
import cn.ac.ict.alpha.Entities.StatusEntity;
import cn.ac.ict.alpha.Entities.UserInfo;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: saukymo
 * Date: 11/16/16
 */

public class ApiClient {
    private String baseUrl = "http://0a9d1f2e.ngrok.io/cana-api/";
//    private static final int DEFAULT_TIMEOUT = 5;

    private static ApiClient mApiClient;
    private Retrofit mRetrofit;
    private ApiService mApiService;

    private ApiClient() {
//        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
//        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    public static ApiClient getInstance(){
        if (mApiClient == null) {
            synchronized(ApiClient.class) {
                if (mApiClient == null) {
                    mApiClient = new ApiClient();
                }
            }
        }
        return mApiClient;
    }

    public void getStatus(Subscriber<StatusEntity> subscriber) {
        mApiService.getStatus()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void login(Subscriber<ResultEntity> subscriber, UserInfo userInfo) {
        mApiService.login(userInfo)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void register(Subscriber<ResultEntity> subscriber, UserInfo userInfo) {
        mApiService.login(userInfo)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
