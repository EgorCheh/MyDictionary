package com.example.cheho.mydictionary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewWordsYandex extends AppCompatActivity  {

    final String LOG_TAG = "myLogs";
    private Button btnAddNewWord;
    private EditText etEngWord,etRusWord;
    private String text,lang;
    private TextView tvSetText;
    private SQLiteDatabase mDb;
    private ContentValues cv = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_words_yandex);

        Toolbar toolbar =findViewById(R.id.toolbarYandex);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_to_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                    Intent intent = new Intent(getApplicationContext(),SearchImage.class);
                    intent.putExtra("translation", cv.get("translation").toString());
                    intent.putExtra("word", cv.get("word").toString());
                    startActivityForResult(intent,1);

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
            translation();

            }
        });



        DatabaseHelper mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();






    }

    void translation(){
        YandexTranslateService.createYandexTranslateService().translate(getString(R.string.Yandex_API_KEY),text,lang).enqueue(new Callback<YandexResponse>(){
            @Override
            public void onResponse(@NonNull Call<YandexResponse> call, @NonNull Response<YandexResponse> response) {

                assert response.body() != null;
                tvSetText.setText(response.body().getText().toString().substring(1,response.body().getText().toString().length()-1));
                btnAddNewWord.setEnabled(true);
            }

            @Override
            public void onFailure(@NonNull Call<YandexResponse> call, @NonNull Throwable t) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}


        cv.put("URL",data.getStringExtra("URL"));
        mDb.insert("study", null, cv );
        Toast.makeText(getApplicationContext(),R.string.toastAddWord,Toast.LENGTH_SHORT).show();
        etEngWord.setText("");
        etRusWord.setText("");
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
        overridePendingTransition(R.anim.open_main_activity,R.anim.back_main_activity);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.open_main_activity,R.anim.back_main_activity);
    }
}