package com.example.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtil {

    private String environment = Environment.getExternalStorageState();

    public boolean WriteOutData(String data, String path) {
        if (Environment.MEDIA_MOUNTED.equals(environment)) {
            //外部设备可以进行读写操作
            File sd_path = Environment.getExternalStorageDirectory();
            System.out.println(sd_path);
            if (!sd_path.exists()) {
                return false;
            }
            File file = new File(sd_path, path);
            String str = data;
            FileOutputStream fos;
            try {
                //写入数据
                fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                osw.write(str);
                osw.flush();
                osw.close();
                fos.close();
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
        else {
            return false;
        }
    }
    public String ReadOutData(String path){
        if(Environment.MEDIA_MOUNTED.equals(environment)) {
            //外部设备可以进行读写操作
            File sd_path=Environment.getExternalStorageDirectory();
            //if (!sd_path.exists()) {return; }
            File file=new File(sd_path,path);
            FileInputStream fis;
            try{
                //读取文件
                fis=new FileInputStream(file);
                InputStreamReader isr=new InputStreamReader(fis,"UTF-8");
                char[] input=new char[fis.available()];
                isr.read(input); String s=new String(input);
                isr.close();    fis.close();
                return s;
            }
            catch(Exception exception){  exception.printStackTrace();
            return "读取数据失败";
            }
        }
        else {
            return "读取数据失败";
        }
    }


}

