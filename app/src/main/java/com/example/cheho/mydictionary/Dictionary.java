package com.example.cheho.mydictionary;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Dictionary extends AppCompatActivity implements ListViewDialog.NoticeDialogListener {

    private SQLiteDatabase mDb;
    private ProgressBar progressBar;
    private ContentValues cv = new ContentValues();
    private ArrayList<HashMap<String, Object>> words = new ArrayList<>();
    private HashMap<String, Object> word;
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

        Toolbar toolbar =findViewById(R.id.toolbarDict);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_to_home);

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

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(this, SearchImage.class);
        intent.putExtra("translation", Objects.requireNonNull(word.get("translation")).toString());
        intent.putExtra("word", Objects.requireNonNull(word.get("word")).toString());
        startActivityForResult(intent,1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}


        cv.put("URL",data.getStringExtra("URL"));
        mDb.insert("study", null, cv );
        Toast.makeText(getApplicationContext(),R.string.toastAddWord,Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("StaticFieldLeak")
    class DictionaryTask extends AsyncTask<Void, Void, Void>{
        int imageDone = R.drawable.ic_action_check_word;
        SimpleAdapter adapter;

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);

            String[] from = { "word",  "translation","image"};
            int[] to = { R.id.itemTvWord, R.id.itemTvTranslation,R.id.itemImg};

            adapter= new SimpleAdapter(getApplicationContext(), words, R.layout.adapter_item, from, to);
            final ListView listView = findViewById(R.id.lvDictionary);

            listView.setAdapter(adapter);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Listener", "itemClick: position = " + position + ", id = "+id);
                    word = new HashMap<>();

                    word.put("translation",  words.get(position).get("translation"));
                    word.put("word",  words.get(position).get("word"));
                    word.put("image",  imageDone);

                    words.set(position,word);
                    cv.put("word", Objects.requireNonNull(word.get("word")).toString());
                    cv.put("translation", Objects.requireNonNull(word.get("translation")).toString());
                    ListViewDialog listViewDialog = new ListViewDialog();
                    listViewDialog.setParam(word,adapter);
                    listViewDialog.show(getSupportFragmentManager(),"dialog");


                    return false;
                }
            });


            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }



        @Override
        protected Void doInBackground(Void... voids) {



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
            return null;
        }
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
