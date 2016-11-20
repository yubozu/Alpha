package cn.ac.ict.alpha.models;

import cn.ac.ict.alpha.Entities.BaseEntity;
import cn.ac.ict.alpha.Entities.AuthEntity;
import cn.ac.ict.alpha.Entities.UserInfoEntity;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Author: saukymo
 * Date: 11/16/16
 */

public interface ApiService {
    @GET("./")
    Observable<BaseEntity> getStatus();

    @POST("login")
    Observable<UserInfoEntity> login(@Body UserInfoEntity userInfo);

    @POST("register")
    Observable<AuthEntity> register(@Body UserInfoEntity userInfo);

    @PUT("user/{userId}")
    Observable<UserInfoEntity> updateUserInfo(@Path("userId") Integer userId, @Body UserInfoEntity userInfo);

    @GET("user/{userId}")
    Observable<UserInfoEntity> getUserInfo(@Path("userId") Integer userId);
}