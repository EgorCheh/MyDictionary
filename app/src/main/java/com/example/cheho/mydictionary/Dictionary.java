package com.example.cheho.mydictionary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        DatabaseHelper mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();


        ArrayList<HashMap<String, Object>> words = new ArrayList<>();
        HashMap<String, Object> word;

        Cursor cursor = mDb.rawQuery("SELECT * FROM words", null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            word = new HashMap<>();
         //   word.put("ID",  cursor.getString(0));
            word.put("translation",  cursor.getString(2));
            word.put("word",  cursor.getString(1));

            words.add(word);
            cursor.moveToNext();
        }
        cursor.close();

       //___________________________________________________________________________________________ ????????????????????
        String[] from = { "word",  "translation"};
        int[] to = { R.id.itemTvWord, R.id.itemTvTranslation};


        SimpleAdapter adapter = new SimpleAdapter(this, words, R.layout.adapter_item, from, to);
        ListView listView = findViewById(R.id.lvDictionary);
        listView.setAdapter(adapter);
    }
}
