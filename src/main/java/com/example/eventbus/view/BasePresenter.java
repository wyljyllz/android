package com.example.eventbus.view;

import android.os.FileObserver;

import com.example.dto.MyResponse;
import com.example.eventbus.retrofittest.RetrofitServiceManager;
import com.example.eventbus.service.ResponseServices;

import java.util.HashMap;

import io.reactivex.disposables.CompositeDisposable;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BasePresenter<V extends BaseView> {
    private CompositeDisposable compositeDisposable;
    public V baseView;
    public static ResponseServices responseServices = RetrofitServiceManager.getInstance().create(ResponseServices.class);
    public BasePresenter(V baseView) {
        this.baseView = baseView;
    }
    /**
     * 解除绑定
     */
    /**
     * 返回 view
     *
     * @return
     */
    public V getBaseView() {
        return baseView;
    }

    public static void getTestText(Observer<MyResponse> observer) {
        setSubscribe(responseServices.getTest(), observer);
    }
    public static void getLightData(HashMap map, Observer<MyResponse> observer) {
        setSubscribe(responseServices.getLightData(map), observer);
    }
    private static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }
}