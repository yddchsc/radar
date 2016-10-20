package com.example.yddchsc.baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class friendDetailActivity extends Activity {

    private Button delete;
    private Button list;
    private Button radar;

    private TextView name;
    private TextView number;
    private TextView long_lang;

    private friend bean;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail);

        bean = new friend();
        bean= (friend) getIntent().getSerializableExtra("detail");

        name = (TextView) findViewById(R.id.txt_friend_name);
        name.setText(bean.getText("name"));

        number = (TextView) findViewById(R.id.txt_friend_number);
        number.setText(bean.getText("name"));

        long_lang = (TextView) findViewById(R.id.txt_friend_long_lang);
        long_lang.setText(bean.getText("long_lang"));

        radar = (Button) findViewById(R.id.btn_radar);
        radar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(friendDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        list = (Button) findViewById(R.id.btn_friends_list);
        list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(friendDetailActivity.this,friendListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        delete = (Button) findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(friendDetailActivity.this,friendListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}