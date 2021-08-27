package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.dto.MyResponse;
import com.example.eventbus.service.ResponseEngine;
import com.example.iot.App;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import rx.Observer;

public class MainTem extends AppCompatActivity
        implements View.OnClickListener, IMqttMessageListener {
    private TextView textView;
    private Button setTem;
    private TextView temp;
    App app=new App(this);
    private TextView hum;
    private LineChartView mChartView;
    private List<PointValue> values;
    private List<Line> lines;
    private LineChartData lineChartData;
    private LineChartData lineChartData1;
    private LineChartView lineChartView;
    private LineChartView lineChartView1;
    private List<Line> linesList;
    private List<Line> linesList1;
    private List<PointValue> pointValueList;
    private List<PointValue> pointValueList1;
    private List<PointValue> points;
    private int position = 0;
    private int position1 = 0;
    private boolean isFinish = true;
    private Axis axisY, axisX;
    private Axis axisY1, axisX1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tem_main);
        Intent getIntent=getIntent();
        String username=getIntent.getStringExtra("username");
        textView=findViewById(R.id.label1);
        textView.setText("当前用户：\n"+username);
        temp=findViewById(R.id.temp);
        hum=findViewById(R.id.hum);
        initData();
        mChartView = (LineChartView) findViewById(R.id.tempChart);
        initView();
        initChart((float) 24.5);
        initView1();
        initChart1((float) 35.2);
    }
    public void  initChart(float num){
        PointValue value1 = new PointValue(position * 5,num);
        value1.setLabel("00:00");
        pointValueList.add(value1);

        float x = value1.getX();
        //根据新的点的集合画出新的线
        Line line = new Line(pointValueList);
        line.setColor(Color.RED);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线

        linesList.clear();
        linesList.add(line);
        lineChartData = initDatas(linesList);
        lineChartView.setLineChartData(lineChartData);
        //根据点的横坐实时变幻坐标的视图范围
        Viewport port;
        if (x > 50) {
            port = initViewPort(x - 50, x);
        } else {
            port = initViewPort(0, 50);
        }
        lineChartView.setCurrentViewport(port);//当前窗口

        Viewport maPort = initMaxViewPort(x);
        lineChartView.setMaximumViewport(maPort);//最大窗口
        position++;
    }
    public void  initChart1(float num){
        PointValue value1 = new PointValue(position1 * 5,num);
        value1.setLabel("00:00");
        pointValueList1.add(value1);

        float x = value1.getX();
        //根据新的点的集合画出新的线
        Line line = new Line(pointValueList1);
        line.setColor(Color.RED);
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线

        linesList1.clear();
        linesList1.add(line);
        lineChartData1 = initDatas(linesList1);
        lineChartView1.setLineChartData(lineChartData1);
        //根据点的横坐实时变幻坐标的视图范围
        Viewport port;
        if (x > 50) {
            port = initViewPort(x - 50, x);
        } else {
            port = initViewPort(0, 50);
        }
        lineChartView1.setCurrentViewport(port);//当前窗口

        Viewport maPort = initMaxViewPort(x);
        lineChartView1.setMaximumViewport(maPort);//最大窗口
        position1++;
    }
        @Override
    public void onClick(View v) {

        }
    private void initView() {
        lineChartView = (LineChartView) findViewById(R.id.tempChart);
        pointValueList = new ArrayList<>();
        linesList = new ArrayList<>();

        //初始化坐标轴
        axisY = new Axis();
        //添加坐标轴的名称
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
    private void initData() {
        ResponseEngine.selectOneTempHum( new Observer<MyResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("retrofit==111=", "请求错误："+e.getMessage());
            }

            @Override
            public void onNext(MyResponse myResponse) {
                Log.i("retrofit==222=", "请求成功："+myResponse.data.toString());
                Map map= (Map) myResponse.data;
                temp.setText(map.get("hum").toString());
                hum.setText(map.get("temp").toString());
            }
        });
    }
    private void initView1() {
        lineChartView1 = (LineChartView) findViewById(R.id.humChart);
        pointValueList1 = new ArrayList<>();
        linesList1 = new ArrayList<>();

        //初始化坐标轴
        axisY1 = new Axis();
        //添加坐标轴的名称
        axisY1.setLineColor(Color.parseColor("#aab2bd"));
        axisY1.setTextColor(Color.parseColor("#aab2bd"));
        axisX1 = new Axis();
        axisX1.setLineColor(Color.parseColor("#aab2bd"));
        lineChartData1 = initDatas(null);
        lineChartView1.setLineChartData(lineChartData);

        Viewport port = initViewPort(0, 50);
        lineChartView1.setCurrentViewportWithAnimation(port);
        lineChartView1.setInteractive(false);
        lineChartView1.setScrollEnabled(true);
        lineChartView1.setValueTouchEnabled(true);
        lineChartView1.setFocusableInTouchMode(true);
        lineChartView1.setViewportCalculationEnabled(false);
        lineChartView1.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView1.startDataAnimation();
        points = new ArrayList<>();
    }


    private LineChartData initDatas(List<Line> lines) {
        LineChartData data = new LineChartData(lines);
        data.setAxisYLeft(axisY);
        data.setAxisXBottom(axisX);
        return data;
    }

    /**
     * 当前显示区域
     *
     * @param left
     * @param right
     * @return
     */
    private Viewport initViewPort(float left, float right) {
        Viewport port = new Viewport();
        port.top = 50;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }

    /**
     * 最大显示区域
     *
     * @param right
     * @return
     */
    private Viewport initMaxViewPort(float right) {
        Viewport port = new Viewport();
        port.top = 50;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 50;
        return port;
    }

    @Override
    public void messageArrived(String topic, MqttMessage var2) throws Exception {
        Map map= JSON.parseObject(var2.toString());
        if(map.get("items")!=null){
            Map map1= (Map) map.get("items");
            Map tempMap= (Map) map1.get("temp");
            Map humMap= (Map) map1.get("hum");
            System.out.println(tempMap.get("value"));
            System.out.println(humMap.get("value"));
            try {
                initChart(Float.parseFloat((String) tempMap.get("value")) );
                initChart1(Float.parseFloat((String) humMap.get("value")));
                temp.setText((String) tempMap.get("value"));
                hum.setText((String) humMap.get("value"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(map);
    }
}
