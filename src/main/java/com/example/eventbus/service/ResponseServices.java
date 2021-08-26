package com.example.eventbus.service;

import com.example.dto.MyResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface ResponseServices {
    @GET("iot/getLightLength.action")
    Observable<MyResponse> getTest();
    @POST("iot/getLightData.action")
    Observable<MyResponse> getLightData(@Body HashMap<String, Object> params);
    @POST("register/userLogin.action")
    Observable<MyResponse> userLogin(@Body Map<String, Object> params);
    @POST("register/addUser.action")
    Observable<MyResponse> addUser(@Body Map<String, Object> params);
    @POST("iot/open.action")
    Observable<MyResponse> open(@Body Map<String, Object> params);
    @POST("iot/close.action")
    Observable<MyResponse> close(@Body Map<String, Object> params);
    @GET("iot/selectOneTempHum.action")
    Observable<MyResponse> selectOneTempHum();
    @GET("iot/selectOneLight.action")
    Observable<MyResponse> selectOneLight();

}
