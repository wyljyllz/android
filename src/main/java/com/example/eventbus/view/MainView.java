package com.example.eventbus.view;

import com.example.dto.MyResponse;

public interface MainView extends BaseView {
    void onTextSuccess(MyResponse o);
}