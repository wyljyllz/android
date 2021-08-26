package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class UserMes {
    private static final int MODE_PRIVATE = 0x0000;

    public boolean save_userMes(Context context, String account, String password){
        SharedPreferences sharedPreferences=
                context.getSharedPreferences("user_mes",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("account",account);
        editor.putString("password",password);
        editor.commit();
        return true;
    }
    public Map<String,String> getuser_mes(Context context){
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
