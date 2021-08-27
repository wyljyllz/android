package com.example.eventbus.view;

import android.util.Log;

import com.example.dto.MyResponse;
import com.example.eventbus.service.ResponseEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observer;

public class MainPresenter {



    /**
     * 写法好多种  怎么顺手怎么来
     * @return
     */
    public void getTextApi() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("startIndex", 0);
        params.put("rows", 10);
        ResponseEngine.getLightData(params, new Observer<MyResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("retrofit==111=", "请求错误："+e.getMessage());
            }

            @Override
            public void onNext(MyResponse myResponse) {
                Log.i("retrofit==222=",  myResponse.data.toString());
            }
        });
    }
}