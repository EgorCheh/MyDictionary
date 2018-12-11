package com.example.cheho.mydictionary;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class StudyWords extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_words);
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        ArrayList<HashMap<String, Object>> words = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> word;

        Cursor cursor = mDb.rawQuery("SELECT * FROM study", null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            word = new HashMap<String, Object>();
            word.put("ID",  cursor.getString(0));
            word.put("word",  cursor.getString(1));
            word.put("translation",  cursor.getString(2));
            Log.d(LOG_TAG, "_"+word+" "+cursor.getString(3)+" "+cursor.getString(4) );
            words.add(word);
            cursor.moveToNext();
        }
        cursor.close();


        String[] from = {"ID", "word",  "translation"};
        int[] to = {R.id.itemTvID, R.id.itemTvWord, R.id.itemTvTranslation};

        SimpleAdapter adapter = new SimpleAdapter(this, words, R.layout.adapter_item, from, to);
        ListView listView = (ListView) findViewById(R.id.lvStudy);
        listView.setAdapter(adapter);
    }
}
