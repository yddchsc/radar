package com.example.yddchsc.baidumap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class friendListActivity extends Activity {

    private List<friend> datas;
    private ListViewAdapter listViewAdapter;
    private Button add;
    private ListView mylist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        mylist = (ListView) findViewById(R.id.lvw_friends_list);

        datas = getdata();
        mylist.setAdapter(new ListViewAdapter(this, getdata()));
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(friendListActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("edit", datas.get(position));
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });

        add = (Button) findViewById(R.id.btn_friends_list_add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(friendListActivity.this);
                builder.setPositiveButton("",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                    }
                });

                builder.setNegativeButton("",new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }
    private List<friend> getdata() {
        List<friend> data =   new ArrayList<friend>();
        //friend bean = new friend();

        List<friend> s  = (List<friend>) getObject("friends.dat");
        if (null != s) {
            data = s;
            return data;
            //      System.out.println("姓名：" + s.getText("day") + ", 年龄：" + s.getText("content"));
        }/*else{
            bean = new friend();
            bean.setType(0);
            bean.setText("name", "");
            bean.setText("number","");
            bean.setText("week", "");
            bean.setText("content","");
            data.add(bean);
            saveObject("friends.dat",data);
        }*/
        return data;
    }
    private void saveObject(String name,List<friend> d){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = this.openFileOutput(name, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(d);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
    private Object getObject(String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }
}