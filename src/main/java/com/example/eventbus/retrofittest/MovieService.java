package com.example.eventbus.retrofittest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface MovieService {
    //获取豆瓣前20的榜单
    @FormUrlEncoded
    @GET("top250")
    Observable<movieTopReq> getMovicTop(@Query("start") int start, @Query("count") int count);
    @GET("iot/getLightLength.action")
    Observable<Map> getTest();
    @POST("iot/getLightData.action")
    Observable<Map> getLightData(@Body HashMap<String, Object> params);
}