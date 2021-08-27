package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utils.FileUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity
{
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private String path="test.txt";
    private EditText inputData;
    private Button writeOutData;
    private Button writeInnerData;
    private Button readInnerData;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        inputData=(EditText) findViewById(R.id.inputData);
        writeOutData=(Button) findViewById(R.id.writeOutData);
        writeOutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=inputData.getText().toString();
                FileUtil fileUtil=new FileUtil();
                boolean success=fileUtil.WriteOutData(data,path);
                if(success){
                    alert = null;
                    builder = new AlertDialog.Builder(mContext);
                    alert=builder.setTitle("系统提示:")
                            .setMessage("写入外部文件成功").create();
                    alert.show();
                }
                else {
                    alert = null;
                    builder = new AlertDialog.Builder(mContext);
                    alert=builder.setTitle("系统提示:")
                            .setMessage("写入外部文件失败")
                            .create();
                    alert.show();
                }
            }
        });
        writeInnerData=(Button)findViewById(R.id.writeInnerData);
        writeInnerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=inputData.getText().toString();
                boolean success=WriteInnerData(data,path);
                if(success){
                    alert = null;
                    builder = new AlertDialog.Builder(mContext);
                    alert=builder.setTitle("系统提示:")
                            .setMessage("写入内部文件成功").create();
                    alert.show();
                }
                else {
                    alert = null;
                    builder = new AlertDialog.Builder(mContext);
                    alert=builder.setTitle("系统提示:")
                            .setMessage("写入内部文件失败")
                            .create();
                    alert.show();
                }
            }
        });
        readInnerData=(Button)findViewById(R.id.readInnerData);
        readInnerData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data=ReadInnerData(path);
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert=builder.setTitle("读取内部文件:")
                        .setMessage(data)
                        .create();
                alert.show();
            }
        });
    }
    public boolean WriteInnerData(String data,String path){
        String file_name=path;
        //写入文件的数据
        String str=data;
        FileOutputStream fi_out;
        try{
            fi_out=openFileOutput (file_name, MODE_PRIVATE);
            fi_out.write(str.getBytes());
            fi_out.close();
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
    public String  ReadInnerData(String path){
        String file_name=path;
        //保存读取的数据
        String str="";
        FileInputStream fi_in;
        try{
            fi_in=openFileInput(file_name);
            //fi_in.available()返回的实际可读字节数
            byte[] buffer=new byte[fi_in.available()];
            fi_in.read(buffer);
            str=new String(buffer);
            return str;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return "读取数据失败";
        }
    }
}