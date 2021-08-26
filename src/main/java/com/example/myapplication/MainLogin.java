package com.example.myapplication;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.RegistDao;
import com.example.database.RegistDatabase;
import com.example.dto.MyResponse;
import com.example.entity.Regist;
import com.example.eventbus.retrofittest.Constant;
import com.example.eventbus.retrofittest.RequestServices;
import com.example.eventbus.retrofittest.movieTopReq;
import com.example.eventbus.service.ResponseEngine;
import com.example.eventbus.view.MainPresenter;
import com.example.eventbus.view.MainView;
import com.example.iot.App;
import com.example.utils.HttpEngine;
import com.example.utils.UserMes;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import rx.Observer;

public class MainLogin extends AppCompatActivity
implements View.OnClickListener{
    private MainRegist mainRegist;
    private EditText account;
    private Button login;
    private Button register;
    private EditText password;
    public String STATE;
    public String TEMP;
    public String HUM;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private CheckBox selector_password;
    RegistDatabase registDatabase=new RegistDatabase(MainLogin.this);
    SQLiteDatabase db=null;
    RegistDao registDao=new RegistDao(MainLogin.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
       // initData();
        getData();
        selector_password=(CheckBox)findViewById(R.id.cbDisplayPassword);
        selector_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    password=(EditText)findViewById(R.id.password);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD

                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    private void getData(){
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
                TEMP=map.get("hum").toString();
                HUM=map.get("temp").toString();
            }
        });
        ResponseEngine.selectOneLight( new Observer<MyResponse>() {
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
                STATE= (String) map.get("state");
            }
        });
    }
    private void initData(String username,String password) {
        //调用封装好的retrofit请求方法
        Map map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        ResponseEngine.userLogin(map, new Observer<MyResponse>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.i("retrofit==111=", "请求错误："+e.getMessage());
            }

            @Override
            public void onNext(MyResponse myResponse) {
                if(myResponse.error==null){
                    Toasty.success(MainLogin.this, "登录成功", Toasty.LENGTH_SHORT, true).show();
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    //Intent传递参数
                    intent.putExtra("username", account.getText().toString());
                    intent.putExtra("state",STATE);
                    intent.putExtra("temp",TEMP);
                    intent.putExtra("hum",HUM);
                    intent.setClass(MainLogin.this, MyCustomAppIntro.class);
                    MainLogin.this.startActivity(intent);
                }else {
                    Toasty.error(MainLogin.this,"登录失败", Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                account=findViewById(R.id.account);
                password=findViewById(R.id.password);
                Regist regist=new Regist();
                regist.setPassword(password.getText().toString());
                regist.setAccount(account.getText().toString());

                boolean success=registDao.selectUser(regist);
                if(success) {
                    initData(regist.getAccount(),regist.getPassword());

                }
                else {
                    Toasty.error(MainLogin.this,"密码错误", Toasty.LENGTH_SHORT, true).show();

                }
                break;
            case R.id.register:
                // 给bnt1添加点击响应事件
                Intent intent = new Intent();
//(当前Activity，目标Activity)
                intent.setClass(MainLogin.this, MainRegist.class);
                startActivity(intent);
                break;
    }
}
}
