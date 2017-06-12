package com.zzw.essayjokestudy.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:
 */

public interface TestService {
    @GET("weixin/query")
    Observable<Object> getInfo(@Query("key") String key,
                               @Query("pno") int pno,
                               @Query("ps") int ps,
                               @Query("dtype") String dtype);

    @Headers("Accept-Encoding:gzip")
    @GET("http://103.198.194.168:5555/api/public/GetAllGame")
    Observable<Object> test();

}
