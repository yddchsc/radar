package com.example.yddchsc.baidumap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class friendListActivity extends Activity {

    private List<friend> datas;
    private Button add;
    private Button radar;
    private ListView mylist;
    private File file = new File(friendListActivity.this);
    private ListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        mylist = (ListView) findViewById(R.id.lvw_friends_list);

        datas = getdata();
        adapter = new ListViewAdapter(this, datas);
        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(friendListActivity.this, friendDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("detail", getdata().get(position));
                intent.putExtras(mBundle);
                startActivity(intent);
                finish();
            }
        });

        add = (Button) findViewById(R.id.btn_friends_list_add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final addFriendDialog.Builder builder = new addFriendDialog.Builder(friendListActivity.this);
                builder.setPositiveButton("",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText text = (EditText) builder.layout.findViewById(R.id.txt_friend_name);    //注意findViewById前面有特定的View
                        String name = text.getText().toString();
                        text = (EditText) builder.layout.findViewById(R.id.txt_friend_number);
                        String number = text.getText().toString();

                        List<friend> data =   new ArrayList<friend>();
                        if (null != file.getObject("friends.dat")) {
                            data = (List<friend>) file.getObject("friends.dat");
                        }
                        friend bean = new friend();
                        bean.setType(0);
                        bean.setText("name", name);
                        bean.setText("number",number);
                        data.add(bean);
                        file.saveObject("friends.dat",data);

                        dialog.dismiss();
                        mylist.setAdapter(new ListViewAdapter(friendListActivity.this, getdata()));
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
        radar = (Button) findViewById(R.id.btn_friends_list_radar);
        radar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(friendListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private List<friend> getdata() {
        List<friend> data =   new ArrayList<friend>();

        List<friend> s  = (List<friend>) file.getObject("friends.dat");
        if (null != s) {
            data = s;
            return data;
        }
        return data;
    }
}