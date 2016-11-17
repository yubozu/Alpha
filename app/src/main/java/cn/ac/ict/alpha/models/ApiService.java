package cn.ac.ict.alpha.models;

import cn.ac.ict.alpha.Entities.StatusEntity;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Author: saukymo
 * Date: 11/16/16
 */

public interface ApiService {
    @GET("./")
    Observable<StatusEntity> getStatus();
}