package com.example.iot;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.MainLamp;
import com.example.myapplication.MainTem;
import com.example.myapplication.MyCustomAppIntro;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.Map;
class MqttPostPropertyMessageListener implements IMqttMessageListener {
    private MainTem mainTem;
    private MainLamp mainLamp;
    MyCustomAppIntro myCustomAppIntro;
    @Override
    public void messageArrived(String var1, MqttMessage var2) throws Exception {
        System.out.println("reply topic  : " + var1);
        System.out.println("reply payload: " + var2.toString());
        Map map=JSON.parseObject(var2.toString());
        if(map.get("LightSwitch")!=null){
            if(map.get("LightSwitch").equals("1")){
                map.put("state","开");
                try {
                    mainLamp.initChart(50);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if (map.get("LightSwitch").equals("0")){
                map.put("state","关");
                try {
                    mainLamp.initChart(50);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else if(map.get("temp")!=null&&map.get("hum")!=null){
            System.out.println(map.get("temp"));
            System.out.println(map.get("hum"));
            try {
                mainTem.initChart((Float) map.get("hum"));
                mainTem.initChart((Float) map.get("temp"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println(map);
    }
}

public class App
{

    private MqttClient mqttClient;
    String productKey = "a1d9iAQMVo4";
    String deviceName = "send";
    MainLamp mainLamp;
    MainTem mainTem;
    String deviceSecret = "67f5ea6b5945307cb4640ee9819ee82a";
    public App(MainLamp mainLamp){
        this.mainLamp=mainLamp;
        mqttClient=initAliyunIoTClient();
    }
    public App(MainTem mainTem){
        this.mainTem=mainTem;
        mqttClient=initAliyunIoTClient();
    }
    public MqttClient initAliyunIoTClient() {

        try {
            MqttSign sign = new MqttSign();
            sign.calculate(productKey, deviceName, deviceSecret);
            //使用Paho连接阿里云物联网平台
            String port = "443";
            String broker = "tcp://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
            MemoryPersistence persistence = new MemoryPersistence();
            //Paho Mqtt 客户端
            MqttClient sampleClient = new MqttClient(broker, sign.getClientid(), persistence);
            //Paho Mqtt 连接参数
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(180);
            connOpts.setUserName(sign.getUsername());
            connOpts.setPassword(sign.getPassword().toCharArray());
            sampleClient.connect(connOpts);
            System.out.println("broker: " + broker + " Connected");
            //Paho Mqtt 消息订阅
            String topicReply = "/sys/" + productKey + "/" + deviceName + "/thing/service/property/set";
            if(mainTem==null&&mainLamp!=null){
                sampleClient.subscribe(topicReply, mainLamp);
            }
            if(mainTem!=null&&mainLamp==null){
                sampleClient.subscribe(topicReply, mainTem);
            }

            System.out.println("subscribe: " + topicReply);
            return sampleClient;

        }catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void connectMqtt(String url, String clientId, String mqttUsername, final String mqttPassword) throws Exception {

        MemoryPersistence persistence = new MemoryPersistence();
        mqttClient = new MqttClient(url, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        // MQTT 3.1.1
        connOpts.setMqttVersion(4);
        connOpts.setAutomaticReconnect(true);
        connOpts.setCleanSession(true);

        connOpts.setUserName(mqttUsername);
        connOpts.setPassword(mqttPassword.toCharArray());
        connOpts.setKeepAliveInterval(60);
        mqttClient.connect(connOpts);

        final String Sub_topic = "/sys/" + productKey + "/"+deviceName + "/thing/service/property/set";
        if (mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(Sub_topic, 0);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        //设置回调函数
        mqttClient.setCallback(new MqttCallback() {
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                JSONObject theMsg= new JSONObject( new String(message.getPayload()));
                theMsg=theMsg.getJSONObject("items");
                JSONObject json_hum=theMsg.getJSONObject("hum");
                JSONObject json_temp=theMsg.getJSONObject("temp");
                String hum=json_hum.getString("value");
                String temp=json_temp.getString("value");
                System.out.println(hum);
                System.out.println(temp);
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
            }

            public void connectionLost(Throwable throwable) {
                initAliyunIoTClient();
            }
        });
    }

    /**
     * post 数据
     */
    public void postDeviceProperties(String  content) {
        try{

            //Paho Mqtt 消息发布
            String topic = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post";
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(0);
            mqttClient.publish(topic, message);
            System.out.println("publish: " + content);
            Thread.sleep(2000);

        }catch (InterruptedException e ) {
            e.printStackTrace();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


}