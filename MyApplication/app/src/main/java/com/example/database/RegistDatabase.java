package com.example.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.entity.Regist;

import java.util.List;

public class RegistDatabase extends SQLiteOpenHelper {

    public RegistDatabase(Context context) {
        super(context, "register_db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(!tabIsExist("register")){
            String sql="create table register(id integer primary key autoincrement,account varchar(20),password varchar(20))"; //数据库执行语句
            db.execSQL(sql);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /**
     * 判断某张表是否存在
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(String tabName){
        boolean result = false;
        if(tabName == null){
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();//此this是继承SQLiteOpenHelper类得到的
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
            cursor = db.rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }


}
