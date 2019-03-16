package com.example.cheho.mydictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheho.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewWordsYandex extends AppCompatActivity  {

    final String LOG_TAG = "myLogs";
    private Button btnAddNewWord;
    private EditText etEngWord,etRusWord;
    private Gson gson = new GsonBuilder().create();
    private RequestAPI req;
    private String text,lang;
    private Map<String, String> map;
    private TextView tvSetText;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_words_yandex);

        Toolbar toolbar =findViewById(R.id.toolbarYandex);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_to_home);

        tvSetText=findViewById(R.id.tvSetText);
        etEngWord = findViewById(R.id.etEngWord);
        etRusWord=findViewById(R.id.etRusWord);

        etRusWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (!etRusWord.getText().toString().equals(""))
                    etEngWord.setEnabled(false);
                else etEngWord.setEnabled(true);
                btnAddNewWord.setEnabled(false);
                tvSetText.setText("");
            }
        });

        etEngWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (!etEngWord.getText().toString().equals(""))
                    etRusWord.setEnabled(false);
                else etRusWord.setEnabled(true);
                btnAddNewWord.setEnabled(false);
                tvSetText.setText("");
            }
        });

        btnAddNewWord=findViewById(R.id.btnAddWordOnStudy);
        btnAddNewWord.setEnabled(false);
        btnAddNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
               if(etEngWord.getText().toString().equals(""))
               {
                cv.put("word",tvSetText.getText().toString() );
                cv.put("translation", etRusWord.getText().toString());
               }
                else
                    {
                        cv.put("word",etEngWord.getText().toString() );
                        cv.put("translation",tvSetText.getText().toString() );
                    }
                if(!checkHasWord(cv.get("word").toString()))
                {
                    mDb.insert("study", null, cv);
                    Toast.makeText(getApplicationContext(),R.string.toastAddWord,Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(),getString(R.string.ToastCheckForInsert),Toast.LENGTH_SHORT).show();

            }
        });
        Button btnAdd = findViewById(R.id.buttAddYandex);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etEngWord.getText().toString().equals("")) {
                    text = etEngWord.getText().toString();
                    lang="en-ru";
                }else if (!etRusWord.getText().toString().equals("")) {
                    text = etRusWord.getText().toString();
                    lang="ru-en";
                }else {Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                    return;
                }
                MyTask mt = new MyTask();
                mt.execute();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        req = retrofit.create(RequestAPI.class);

        DatabaseHelper mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                Map<String, String> mapJson = new HashMap<>();
                String KEY = "trnsl.1.1.20180723T103924Z.4e5559e0a3b45ee4.800a777f9ceb4003f58fd2bc387d15bddeffd2fe";
                mapJson.put("key", KEY);
                mapJson.put("text", text);
                mapJson.put("lang", lang);

                final Call<Object> call = req.RUS(mapJson);

                Response<Object> response = call.execute();


                map = gson.fromJson(response.body().toString(), Map.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Map.Entry e : map.entrySet()) {
                Log.d("Yandex", " Key " + e.getKey() + "  Value " + e.getValue());
                if(e.getKey().toString().equals("text"))
                {
                    tvSetText.setText(e.getValue().toString().substring(1,e.getValue().toString().length()-1));
                    btnAddNewWord.setEnabled(true);
                }
            }

        }


    }

    private boolean checkHasWord(String word){
        Cursor cursorCheck = mDb.rawQuery("SELECT * FROM study", null);
        cursorCheck.moveToFirst();


        while (!cursorCheck.isAfterLast()) {
            if(cursorCheck.getString(1).equals(word))
            {
                Log.d(LOG_TAG,"_________Has word");
                cursorCheck.close();
                return true;
            }

            cursorCheck.moveToNext();
        }
        Log.d(LOG_TAG,"_________No word");
        cursorCheck.close();
        return false;
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}