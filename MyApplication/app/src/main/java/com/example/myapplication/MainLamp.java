package com.example.myapplication;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.dto.MyResponse;
import com.example.eventbus.service.ResponseEngine;
import com.example.iot.App;
import com.suke.widget.SwitchButton;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import rx.Observer;

public class MainLamp extends AppCompatActivity
        implements View.OnClickListener,IMqttMessageListener
{
    private TextView textView;
    private String USERNAME;
    App app=new App(this);
    LineChartView chart;
    private LineChartView mChartView;
    private List<PointValue> values;
    private List<Line> lines;
    private LineChartData lineChartData;
    private LineChartView lineChartView;
    private List<Line> linesList;
    private List<PointValue> pointValueList;
    private List<PointValue> points;
    private int position = 0;
    private Timer timer;
    private boolean isFinish = true;
    private Axis axisY, axisX;
    private Random random = new Random();
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamp_main);
        Intent getIntent=getIntent();
        String username=getIntent.getStringExtra("username");
        textView=findViewById(R.id.label);
        textView.setText("???????????????\n"+username);
        USERNAME=username;
        mChartView = (LineChartView) findViewById(R.id.chart);
        initView();
        //timer = new Timer();
        initChart(0);
//        final List<PointValue> values = new ArrayList<PointValue>();
//        values.add(new PointValue(0, 2));
//        values.add(new PointValue(1, 4));
//        values.add(new PointValue(2, 3));
//        values.add(new PointValue(3, 4));
//        count=3;
//        //In most cased you can call data model methods in builder-pattern-like manner.
//        chart = new LineChartView(MainLamp.this);
//        chart.setLineChartData(initChart(values));
        final com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)
                findViewById(R.id.switch_button);
        switchButton.setChecked(false);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
               if(view.isChecked()){
                   initData(switchButton,true,values);

               }else {
                   initData(switchButton,false,values);
               }
            }
        });
    }
    private void initView() {
        lineChartView = (LineChartView) findViewById(R.id.chart);
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();

        //??????????????????
        axisY = new Axis();
        //????????????????????????
        axisY.setLineColor(Color.parseColor("#aab2bd"));
        axisY.setTextColor(Color.parseColor("#aab2bd"));
        axisX = new Axis();
        axisX.setLineColor(Color.parseColor("#aab2bd"));
        lineChartData = initDatas(null);
        lineChartView.setLineChartData(lineChartData);

        Viewport port = initViewPort(0, 50);
        lineChartView.setCurrentViewportWithAnimation(port);
        lineChartView.setInteractive(false);
        lineChartView.setScrollEnabled(true);
        lineChartView.setValueTouchEnabled(true);
        lineChartView.setFocusableInTouchMode(true);
        lineChartView.setViewportCalculationEnabled(false);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.startDataAnimation();
        points = new ArrayList<>();
    }
    /**
     * ??????????????????
     *
     * @param right
     * @return
     */
    private Viewport initMaxViewPort(float right) {
        Viewport port = new Viewport();
        port.top = 150;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 50;
        return port;
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //?????????????????????
//                PointValue value1 = new PointValue(position * 5, random.nextInt(100) + 40);
//                value1.setLabel("00:00");
//                pointValueList.add(value1);
//
//                float x = value1.getX();
//                //???????????????????????????????????????
//                Line line = new Line(pointValueList);
//                line.setColor(Color.RED);
//                line.setShape(ValueShape.CIRCLE);
//                line.setCubic(true);//?????????????????????????????????????????????
//
//                linesList.clear();
//                linesList.add(line);
//                lineChartData = initDatas(linesList);
//                lineChartView.setLineChartData(lineChartData);
//                //???????????????????????????????????????????????????
//                Viewport port;
//                if (x > 50) {
//                    port = initViewPort(x - 50, x);
//                } else {
//                    port = initViewPort(0, 50);
//                }
//                lineChartView.setCurrentViewport(port);//????????????
//
//                Viewport maPort = initMaxViewPort(x);
//                lineChartView.setMaximumViewport(maPort);//????????????
//                position++;
//            }
//        }, 300, 300);
//    }

    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }
    /**
     * ??????????????????
     *
     * @param left
     * @param right
     * @return
     */
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 150;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }
    public void  initChart(float num){
        PointValue value1 = new PointValue(position * 5,num);
        value1.setLabel("00:00");
        pointValueList.add(value1);

        float x = value1.getX();
        //???????????????????????????????????????
        Line line = new Line(pointValueList);
        line.setColor(Color.RED);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);//?????????????????????????????????????????????

        linesList.clear();
        linesList.add(line);
        lineChartData = initDatas(linesList);
        lineChartView.setLineChartData(lineChartData);
        //???????????????????????????????????????????????????
        Viewport port;
        if (x > 50) {
            port = initViewPort(x - 50, x);
        } else {
            port = initViewPort(0, 50);
        }
        lineChartView.setCurrentViewport(port);//????????????

        Viewport maPort = initMaxViewPort(x);
        lineChartView.setMaximumViewport(maPort);//????????????
        position++;
    }
    private void initData(final com.suke.widget.SwitchButton switchButton, boolean state, final List<PointValue> values) {
        //??????????????????retrofit????????????
        if(state){
            Map map = new HashMap();
            map.put("username",USERNAME);
            ResponseEngine.open(map, new Observer<MyResponse>() {
                @Override
                public void onCompleted() {
                }
                @Override
                public void onError(Throwable e) {
                    Log.i("retrofit==111=", "???????????????"+e.getMessage());
                }

                @Override
                public void onNext(MyResponse myResponse) {
                    if(myResponse.error==null){
//                        initChart(100);
                        Toasty.success(MainLamp.this, "????????????", Toasty.LENGTH_SHORT, true).show();
                    }else {
                        Toasty.error(MainLamp.this, "????????????", Toasty.LENGTH_SHORT, true).show();
                    }
                }
            });
        }else {
            Map map = new HashMap();
            map.put("username",USERNAME);
            ResponseEngine.close(map, new Observer<MyResponse>() {
                @Override
                public void onCompleted() {
                }
                @Override
                public void onError(Throwable e) {
                    Log.i("retrofit==111=", "???????????????"+e.getMessage());
                }

                @Override
                public void onNext(MyResponse myResponse) {
                    if(myResponse.error==null){
//                        initChart(0);
                        Toasty.success(MainLamp.this, "????????????", Toasty.LENGTH_SHORT, true).show();
                    }else {
                        Toasty.error(MainLamp.this, "????????????", Toasty.LENGTH_SHORT, true).show();


                    }
                }
            });
        }

    }
        @Override
    public void onClick(View v) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage var2) throws Exception {
        Map map= JSON.parseObject(var2.toString());
        Log.i("test",map.toString());
        if(map.get("LightSwitch")!=null){
            if(map.get("LightSwitch").equals("1")){
                map.put("state","???");
                try {
                    initChart(50);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if (map.get("LightSwitch").equals("0")){
                map.put("state","???");
                try {
                    initChart(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}