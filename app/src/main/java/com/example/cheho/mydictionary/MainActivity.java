package com.example.cheho.mydictionary;

import android.app.ActivityManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.example.cheho.myapplication.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnOpenDictionary, btnOpenNewWords,btnOpenStudy,btnOpenTraining,btnOpenYandex;
    private Intent intent;
    final String LOG_TAG = "myLogs";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOpenDictionary = findViewById(R.id.btnOpenDictionary);
        btnOpenDictionary.setOnClickListener(this);
        btnOpenNewWords = findViewById(R.id.btnOpenNewWords);
        btnOpenNewWords.setOnClickListener(this);
        btnOpenStudy=findViewById(R.id.btnOpenStudy);
        btnOpenStudy.setOnClickListener(this);
        btnOpenTraining=findViewById(R.id.btnOpenTraining);
        btnOpenTraining.setOnClickListener(this);
        btnOpenYandex = findViewById(R.id.btnOpenYandex);
        btnOpenYandex.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenDictionary:

                intent = new Intent(this, Dictionary.class);
                startActivity(intent);
                break;
            case R.id.btnOpenNewWords:
                intent = new Intent(this, AddNewWordsTop5000.class);
                startActivity(intent);
                break;
            case R.id.btnOpenStudy:
                intent = new Intent(this, StudyWords.class);
                startActivity(intent);
                break;
            case R.id.btnOpenTraining:
                intent = new Intent(this, Training.class);
                startActivity(intent);
                break;
            case R.id.btnOpenYandex:
                intent = new Intent(this, AddNewWordsYandex.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
