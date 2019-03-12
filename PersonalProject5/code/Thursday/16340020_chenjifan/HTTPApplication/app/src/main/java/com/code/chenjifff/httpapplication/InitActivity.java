package com.code.chenjifff.httpapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;

public class InitActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        Button bilibiliBut = (Button) findViewById(R.id.bilibiliApi);
        Button githubBut = (Button) findViewById(R.id.githubApi);
        bilibiliBut.setOnClickListener(this);
        githubBut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bilibiliApi:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.githubApi:
                Intent intent1 = new Intent(this, GithubActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
