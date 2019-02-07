package com.example.cheho.mydictionary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary extends AppCompatActivity {

    private SQLiteDatabase mDb;
    private ProgressBar progressBar;
    private ArrayList<HashMap<String, Object>> words = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        progressBar=findViewById(R.id.progressBarDictionary);

        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        mDb = mDBHelper.getWritableDatabase();
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        DictionaryTask dictionaryTask = new DictionaryTask();
        dictionaryTask.execute();
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
    class DictionaryTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String[] from = { "word",  "translation","image"};
            int[] to = { R.id.itemTvWord, R.id.itemTvTranslation,R.id.itemImg};

            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), words, R.layout.adapter_item, from, to);
            ListView listView = findViewById(R.id.lvDictionary);
            listView.setAdapter(adapter);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {


            HashMap<String, Object> word;
            Cursor cursor = mDb.rawQuery("SELECT * FROM words", null);
            cursor.moveToFirst();
            int img;
            while (!cursor.isAfterLast()) {
                word = new HashMap<>();
                int imageDone = R.drawable.ic_action_check_word;
                word.put("translation",  cursor.getString(2));
                word.put("word",  cursor.getString(1));

                img =(haveWord(cursor.getString(1)))? imageDone : 0;
                word.put("image",  img);
                words.add(word);
                cursor.moveToNext();
            }
            cursor.close();
            return null;
        }
    }
}
