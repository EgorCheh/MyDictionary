package com.example.cheho.mydictionary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StudyWords extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    private HashMap<String, Object> word;
    private ArrayList<HashMap<String, Object>> words = new ArrayList<>();
    private SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_words);
        DatabaseHelper mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        SQLiteDatabase mDb;
        mDb = mDBHelper.getWritableDatabase();





        Cursor cursor = mDb.rawQuery("SELECT * FROM study", null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            word = new HashMap<>();
            word.put("id",  cursor.getString(0));
            word.put("word",  cursor.getString(1));
            word.put("translation",  cursor.getString(2));
            if(cursor.getInt(4)<1)
            word.put("day",  "Today");
            else word.put("day",  cursor.getString(4)+" Days");
            Log.d(LOG_TAG, "_"+word+" "+cursor.getString(3)+" "+cursor.getString(4) );
            words.add(word);
            cursor.moveToNext();
        }
        cursor.close();


        String[] from = { "word",  "translation","day"};
        int[] to = { R.id.itemTvWord, R.id.itemTvTranslation,R.id.itemTvDay};

        adapter = new SimpleAdapter(this, words, R.layout.adapter_item, from, to);
        ListView listView = findViewById(R.id.lvStudy);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Listener", "itemClick: position = " + position + ", id = "+id);
                word = new HashMap<>();

                word.put("translation",  words.get(position).get("translation"));
                word.put("word",  words.get(position).get("word"));
                word.put("id",  words.get(position).get("id"));

                words.set(position,word);

                DialogStudyWord listViewDialog = new DialogStudyWord();
                listViewDialog.setParam(word,adapter,words,position);
                listViewDialog.show(getSupportFragmentManager(),"dialog");



                return false;
            }
        });
    }
}
