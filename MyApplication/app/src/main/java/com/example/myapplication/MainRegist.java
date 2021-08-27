package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dao.RegistDao;
import com.example.database.RegistDatabase;
import com.example.dto.MyResponse;
import com.example.entity.Regist;
import com.example.eventbus.service.ResponseEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import rx.Observer;

public class MainRegist extends AppCompatActivity
        implements View.OnClickListener {
    private EditText account;
    private EditText password;
    private EditText passwordAgain;
    private Button save;
    private Button read;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private CheckBox selector_password;
    private CheckBox selector_password1;
    RegistDatabase registDatabase=new RegistDatabase(MainRegist.this);
    SQLiteDatabase db=null;
    RegistDao registDao=new RegistDao(MainRegist.this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_main);
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(this);
        read=(Button)findViewById(R.id.read);
        read.setOnClickListener(this);
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
        selector_password1=(CheckBox)findViewById(R.id.cbDisplayPassword1);
        selector_password1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    passwordAgain=(EditText)findViewById(R.id.passwordAgain);
                    passwordAgain.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    //默认状态显示密码--设置文本 要一起写才能起作用  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD

                    passwordAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
    private void initData(String username,String passwd) {
        //调用封装好的retrofit请求方法
        Map map = new HashMap();
        map.put("username", username);
        map.put("password", passwd);
        Map map1=new HashMap();
        map1.put("params",map);
        ResponseEngine.addUser(map1, new Observer<MyResponse>() {
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
                    Toasty.success(MainRegist.this,"用户注册成功", Toasty.LENGTH_SHORT, true).show();
                    account.setText("");
                    password.setText("");
                    passwordAgain.setText("");
                }else {
                    Toasty.error(MainRegist.this,"注册失败", Toasty.LENGTH_SHORT, true).show();
                }

                Log.i("retrofit==222=", String.valueOf(myResponse.code));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                {
                    account=(EditText)findViewById(R.id.account);
                    String username=account.getText().toString();
                    password=(EditText)findViewById(R.id.password);
                    passwordAgain=(EditText)findViewById(R.id.passwordAgain);
                    if(password.getText().toString().equals(passwordAgain.getText().toString())){
                        String passwd=password.getText().toString();
                        boolean success=save_userMes(MainRegist.this,username,passwd);
                        if(success){
                            SQLiteDatabase db=registDatabase.getReadableDatabase();
                            registDao.adddata(db,username,passwd);
                            initData(username,passwd);

                    }else {
                            Toasty.error(MainRegist.this,"用户注册失败", Toasty.LENGTH_SHORT, true).show();
                        }

                }else {
                        Toasty.error(MainRegist.this,"两次输入密码不一致", Toasty.LENGTH_SHORT, true).show();
                    }
                }
                break;
            case R.id.read:{
                Intent intent = new Intent();
//(当前Activity，目标Activity)
                intent.setClass(MainRegist.this, MainLogin.class);
                startActivity(intent);
//                Map<String, String> map=getuser_mes(MainRegist.this);
//            account=(EditText)findViewById(R.id.account);
//            account.setText(map.get("account"));
//            password=(EditText)findViewById(R.id.password);
//            password.setText(map.get("password"));
//            SQLiteDatabase db=registDatabase.getReadableDatabase();
//            List<Regist> list=registDao.showData(db);
//            for (Regist regist : list){
//                System.out.println(
//                        "账号："+
//                                regist.getAccount()+
//                                "密码："+
//                                regist.getPassword()
//                );
//            }
                break;
        }

        }
    }
    private boolean save_userMes(Context context, String account, String password){
        SharedPreferences sharedPreferences=
                context.getSharedPreferences("user_mes",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("account",account);
        editor.putString("password",password);
        editor.commit();
        return true;
    }
    private Map<String,String> getuser_mes(Context context){
        SharedPreferences sharedPreferences=
                context.getSharedPreferences("user_mes",MODE_PRIVATE);
        String account=sharedPreferences.getString("account",null);//第2个参数表示默认值
        String password=sharedPreferences.getString("password",null);
        Map<String,String> user=new HashMap<String,String>();
        user.put("account",account);
        user.put("password",password);
        return user;
    }


}
