package com.example.utils;

import com.example.eventbus.retrofittest.MovieService;
import com.example.eventbus.retrofittest.RetrofitServiceManager;
import com.example.eventbus.retrofittest.movieTopReq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpEngine {

    private static MovieService movieService = RetrofitServiceManager.getInstance().create(MovieService.class);

    /*
     * 获取豆瓣电影榜单
     * **/
    public static void getDuoBanTop(int start, int count, Observer<movieTopReq> observer) {
        setSubscribe(movieService.getMovicTop(start, count), observer);
    }
    public static void getTestText(Observer<Map> observer) {
        setSubscribe(movieService.getTest(), observer);
    }
    public static void getLightData(HashMap map, Observer<Map> observer) {
        setSubscribe(movieService.getLightData(map), observer);
    }

    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }
}