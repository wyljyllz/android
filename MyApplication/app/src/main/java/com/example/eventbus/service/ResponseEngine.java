package com.example.eventbus.service;

import com.example.dto.MyResponse;
import com.example.eventbus.retrofittest.MovieService;
import com.example.eventbus.retrofittest.RetrofitServiceManager;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ResponseEngine {
    private static ResponseServices responseServices = RetrofitServiceManager.getInstance().create(ResponseServices.class);
    protected static void getTestText(Observer<MyResponse> observer) {
        setSubscribe(responseServices.getTest(), observer);
    }
    public static void selectOneTempHum(Observer<MyResponse> observer) {
        setSubscribe(responseServices.selectOneTempHum(), observer);
    }
    public static void selectOneLight(Observer<MyResponse> observer) {
        setSubscribe(responseServices.selectOneLight(), observer);
    }
    public static void getLightData(HashMap map, Observer<MyResponse> observer) {
        setSubscribe(responseServices.getLightData(map), observer);
    }
    public static void userLogin(Map map, Observer<MyResponse> observer) {
        setSubscribe(responseServices.userLogin(map), observer);
    }
    public static void addUser(Map map, Observer<MyResponse> observer) {
        setSubscribe(responseServices.addUser(map), observer);
    }
    public static void open(Map map, Observer<MyResponse> observer) {
        setSubscribe(responseServices.open(map), observer);
    }
    public static void close(Map map, Observer<MyResponse> observer) {
        setSubscribe(responseServices.close(map), observer);
    }
    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}
