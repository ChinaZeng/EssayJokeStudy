package com.zzw.essayjokestudy.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zzw on 2017/6/5.
 * Version:
 * Des:
 */

public interface WXService {
    @GET("weixin/query")
    Observable<Object> getInfo(@Query("key") String key,
                               @Query("pno") int pno,
                               @Query("ps") int ps,
                               @Query("dtype") String dtype);

}
