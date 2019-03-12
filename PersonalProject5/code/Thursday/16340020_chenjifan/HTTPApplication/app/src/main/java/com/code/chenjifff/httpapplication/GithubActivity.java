package com.code.chenjifff.httpapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action1;


public class GithubActivity extends AppCompatActivity implements View.OnClickListener, githubReposAdapter.OnItemClickListener {
    private ArrayList<Repos> data;
    private ArrayList<Issue> issueData;
    private EditText searchBar;
    private Button searchButton;
    private RecyclerView recyclerView;
    private RecyclerView issueRecyclerView;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        data = new ArrayList<Repos>();
        recyclerView = (RecyclerView) findViewById(R.id.git_result_list);
        githubReposAdapter adapter = new githubReposAdapter(this, data);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        issueData = new ArrayList<Issue>();
        issueRecyclerView = (RecyclerView) findViewById(R.id.git_issue_list);
        IssueAdapter issueAdapter = new IssueAdapter(this, issueData);
        issueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        issueRecyclerView.setAdapter(issueAdapter);
        searchBar = (EditText) findViewById(R.id.git_search_bar);
        searchButton = (Button) findViewById(R.id.git_search_button);
        searchButton.setOnClickListener(this);
        issueRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.git_search_button:
                EditText text = (EditText) findViewById(R.id.git_search_bar);
                String content = text.getText().toString();
                if(content.isEmpty()) {
                    Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!isConn(this)) {
                    Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
                    break;
                }
                currentUser = content;
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetRequestInterface request = retrofit.create(GetRequestInterface.class);
                Call<Repos[]> call = request.getRepos(currentUser);
                call.enqueue(new Callback<Repos[]>() {
                    //请求成功时
                    @Override
                    public void onResponse(Call<Repos[]> call, Response<Repos[]> response) {
                        try {
                            List<Repos> list = Arrays.asList(response.body());
                            data.clear();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isHas_issues()) {
                                    data.add(list.get(i));
                                }
                            }
                            if (data.isEmpty()) {
                                Toast.makeText(GithubActivity.this, "该用户没有任何仓库", Toast.LENGTH_SHORT).show();
                            }
                            Observable.create(new Observable.OnSubscribe<Object>() {
                                @Override
                                public void call(Subscriber<? super Object> subscriber) {
                                    subscriber.onNext("");
                                    subscriber.onCompleted();
                                }
                            }).subscribe(new Action1<Object>() {
                                @Override
                                public void call(Object obj) {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                }
                            });
                        }
                        catch (NullPointerException e) {
                            Toast.makeText(GithubActivity.this, "404错误，找不到该用户", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //请求失败时
                    @Override
                    public void onFailure(Call<Repos[]> call, Throwable throwable) {
                        Toast.makeText(GithubActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int i) {
        Repos repos = data.get(i);
        if(repos.getOpen_issues_count() == 0) {
            Toast.makeText(this, "该仓库下没有任何开放的问题", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isConn(this)) {
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequestInterface request = retrofit.create(GetRequestInterface.class);
        Call<Issue[]> call = request.getReposIssues(currentUser, repos.getName());
        call.enqueue(new Callback<Issue[]>() {
            //请求成功时
            @Override
            public void onResponse(Call<Issue[]> call, Response<Issue[]> response) {
                List<Issue> list = Arrays.asList(response.body());
                issueData.clear();
                for (int i = 0; i < list.size(); i++) {
                    issueData.add(list.get(i));
                }
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext("");
                        subscriber.onCompleted();
                    }
                }).subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object obj) {
                        searchBar.setVisibility(View.INVISIBLE);
                        searchButton.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        issueRecyclerView.setVisibility(View.VISIBLE);
                        issueRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }

            //请求失败时
            @Override
            public void onFailure(Call<Issue[]> call, Throwable throwable) {
                Toast.makeText(GithubActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isConn(Context context) {
        boolean isCon = false;
        //获取网络连接的管理对象
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //通过管理者对象拿到网络的信息
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if(network != null){
            //网络状态是否可用的返回值
            isCon=network.isAvailable();
        }
        return isCon;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if(recyclerView.getVisibility() == View.INVISIBLE) {
                searchBar.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                issueRecyclerView.setVisibility(View.INVISIBLE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
