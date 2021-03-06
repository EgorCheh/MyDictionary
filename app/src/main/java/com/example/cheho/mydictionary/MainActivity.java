package com.example.cheho.mydictionary;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cheho.myapplication.R;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private SharedPreferences sharedPreferences;
    private final String Time="getTime";
    private Date currDate =new Date();
    private int daysDiff;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnOpenDictionary = findViewById(R.id.btnOpenDictionary);
        btnOpenDictionary.setOnClickListener(this);
        Button btnOpenNewWords = findViewById(R.id.btnOpenNewWords);
        btnOpenNewWords.setOnClickListener(this);
        Button btnOpenStudy = findViewById(R.id.btnOpenStudy);
        btnOpenStudy.setOnClickListener(this);
        Button btnOpenTraining = findViewById(R.id.btnOpenTraining);
        btnOpenTraining.setOnClickListener(this);
        Button btnOpenYandex = findViewById(R.id.btnOpenYandex);
        btnOpenYandex.setOnClickListener(this);
        DatabaseHelper mDBHelper = new DatabaseHelper(this);



        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();


        //currDate=new Date(currDate.getTime()+1000000000);
        if(!identicalDates())

        {
            Log.d("Datt", "upDating");
        Cursor cursor = mDb.rawQuery("SELECT * FROM study", null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            ContentValues cv = new ContentValues();
            cv.put("counter",cursor.getInt(4)-daysDiff);
            mDb.update("study", cv, "_id=?", new String[]{cursor.getString(0)});
            cursor.moveToNext();
        }
        cursor.close();
        }

        saveDate();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenDictionary:

                Intent intent = new Intent(this, Dictionary.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_new_activity_from_main,R.anim.close_main_activity);
                break;
            case R.id.btnOpenNewWords:
                intent = new Intent(this, AddNewWordsTop5000.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_new_activity_from_main,R.anim.close_main_activity);
                break;
            case R.id.btnOpenStudy:
                intent = new Intent(this, StudyWords.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_new_activity_from_main,R.anim.close_main_activity);
                break;
            case R.id.btnOpenTraining:
                intent = new Intent(this, Training.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_new_activity_from_main,R.anim.close_main_activity);
                break;
            case R.id.btnOpenYandex:
                intent = new Intent(this, AddNewWordsYandex.class);
                startActivity(intent);
                overridePendingTransition(R.anim.start_new_activity_from_main,R.anim.close_main_activity);
                break;
            default:
                break;
        }
    }
    void saveDate() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(Time,String.valueOf(currDate.getTime()));

        ed.apply();

    }

   private boolean identicalDates() {
        sharedPreferences = getPreferences(MODE_PRIVATE);

       if(sharedPreferences.getString(Time,"-1").equals("-1"))
       {
           Log.d("Datt","First start");
           return true;
       }
       Date sharDate = new Date(Long.parseLong(sharedPreferences.getString(Time,"-1")));

       Log.d("Datt","shar  "+sharedPreferences.getString(Time,"null")+"   date"+sharDate.toString());
       Log.d("Datt","curr  "+currDate.getTime()+"  date"+currDate.toString());


       daysDiff = Days.daysBetween(new DateTime(sharDate), new DateTime(currDate)).getDays();

       Log.d("Datt","diff  "+daysDiff);

       if(daysDiff==0)
       { return true;}
        else {return false;}

    }


}
