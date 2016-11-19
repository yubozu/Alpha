package cn.ac.ict.alpha.models;

import cn.ac.ict.alpha.Entities.UserInfo;
import cn.ac.ict.alpha.Entities.ResultEntity;
import cn.ac.ict.alpha.Entities.StatusEntity;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Author: saukymo
 * Date: 11/16/16
 */

public interface ApiService {
    @GET("./")
    Observable<StatusEntity> getStatus();

    @POST("/login")
    Observable<ResultEntity> login(@Body UserInfo userInfo);

    @POST("/register")
    Observable<ResultEntity> register(@Body UserInfo userInfo);
}