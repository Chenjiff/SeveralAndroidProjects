package com.code.chenjifff.httpapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ArrayList<Video> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<Video>();
        recyclerView = (RecyclerView) findViewById(R.id.result_list);
        RecycleViewAdapter adapter = new RecycleViewAdapter(this, data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

    }

    private Handler handler = new Handler() {
        @Override
        public  void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.search_button:
                EditText searchBar = (EditText) findViewById(R.id.search_bar);
                String content = searchBar.getText().toString();
                final String regex = "^[0-9]+$";
                if(!content.matches(regex) || Integer.valueOf(content) > 40) {
                    Toast.makeText(this, "输入必须为小于或等于40的正整数", Toast.LENGTH_SHORT).show();
                }
                else if(!isConn(this)) {
                    Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EditText searchBar = (EditText) findViewById(R.id.search_bar);
                                String content = searchBar.getText().toString();
                                URL url = new URL("https://space.bilibili.com/ajax/top/showTop?mid=" + content);
                                final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                                InputStream inputStream = httpURLConnection.getInputStream();
                                byte[] bytes = new byte[2000];
                                int size = inputStream.read(bytes);
                                inputStream.close();
                                String jsonString = new String(bytes).substring(0, size);
                                final RecyclerObj recyclerObj = new Gson().fromJson(jsonString, RecyclerObj.class);
                                final boolean status = recyclerObj.isStatus();
                                final RecyclerObj.Data rdata = recyclerObj.getData();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        data.add(new Video(rdata.getCover(), rdata.getPlay(), rdata.getVideo_preview(), rdata.getDuration(),
                                                rdata.getCreate(), rdata.getTitle(), rdata.getContent()));
                                        recyclerView.getAdapter().notifyDataSetChanged();

                                    }
                                });
                            } catch (Exception e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "数据库不存在此纪录", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }
    public static boolean isConn(Context context){
        boolean isCon = false;
        //获取网络连接的管理对象
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //通过管理者对象拿到网络的信息
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if(network != null){
            //网络状态是否可用的返回值
            isCon=network.isAvailable();
        }
        return isCon;
    }
}
