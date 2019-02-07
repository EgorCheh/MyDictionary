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

    private SQLiteDatabase mDb;

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

        int imageDone = R.drawable.ic_action_check_word;
         mDb = mDBHelper.getWritableDatabase();

        ArrayList<HashMap<String, Object>> words = new ArrayList<>();
        HashMap<String, Object> word;

        Cursor cursor = mDb.rawQuery("SELECT * FROM words", null);
        cursor.moveToFirst();

        int img;
        while (!cursor.isAfterLast()) {
            word = new HashMap<>();

            word.put("translation",  cursor.getString(2));
            word.put("word",  cursor.getString(1));

            img =(haveWord(cursor.getString(1)))? imageDone : 0;
            word.put("image",  img);
            words.add(word);
            cursor.moveToNext();
        }
        cursor.close();


        String[] from = { "word",  "translation","image"};
        int[] to = { R.id.itemTvWord, R.id.itemTvTranslation,R.id.itemImg};


        SimpleAdapter adapter = new SimpleAdapter(this, words, R.layout.adapter_item, from, to);
        ListView listView = findViewById(R.id.lvDictionary);
        listView.setAdapter(adapter);
    }
    boolean haveWord (String word)
    {
        Cursor cursorStudy = mDb.rawQuery("SELECT * FROM study", null);
        cursorStudy.moveToFirst();


        while (!cursorStudy.isAfterLast()) {
            if(word.equals(cursorStudy.getString(1)))
                return true;
            cursorStudy.moveToNext();
        }
        cursorStudy.close();
        return false;

    }
}
