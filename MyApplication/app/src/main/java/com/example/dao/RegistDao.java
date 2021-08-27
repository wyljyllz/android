package com.example.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.database.RegistDatabase;
import com.example.entity.Regist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistDao extends RegistDatabase {
    public RegistDao(Context context) {
        super(context);
    }
    /*
    *数据的增加
    *增加register表数据
    * */
    public void adddata(SQLiteDatabase sqLiteDatabase,String account,String password){
        ContentValues values=new ContentValues();
        values.put("account",account);
        values.put("password",password);
        sqLiteDatabase.insert("register",null,values);
        sqLiteDatabase.close();
    }
    /*
*数据的删除
* 第一个参数：表名；
* 第二个参数：需要删除的属性名，
* ？代表占位符；
* 第三个参数：属性名的属性值*/
    public void delete(SQLiteDatabase sqLiteDatabase,String[] account) {
        sqLiteDatabase.delete("register","username=?",account );
        sqLiteDatabase.close();
    }
    /*
    * 数据的修改
    * 创建一个ContentValues对象
    * 以键值对的形式插入
    * 最后执行修改的方法
    * */
    public void update(SQLiteDatabase sqLiteDatabase,String[] account,String password){
        ContentValues values=new ContentValues();
        values.put("password",password);
        sqLiteDatabase.update("register",values,"username=?",account);
        sqLiteDatabase.close();
    }
    public boolean selectUser(Regist regist){
        Cursor cursor = getWritableDatabase().query("register",null,"account=? and password=?",new String[]{regist.getAccount(),regist.getPassword()},
                null,null,null);
        if(cursor.getCount()>0){
            return true;
        }
        else {
            return false;
        }

    }
    public List<Regist> showData(SQLiteDatabase sqLiteDatabase){
        Cursor cursor = getWritableDatabase().query("register",null,null,null,null,null,null,null);
        List<Regist> list=new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            //移动到首位
            Map<String,String> map=new HashMap<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                Regist regist=new Regist();
                regist.setAccount(account);
                regist.setPassword(password);
                list.add(regist);
                //移动到下一位
                cursor.moveToNext();
            }
        }
        cursor.close();
        getWritableDatabase().close();
        return list;
    }

}
