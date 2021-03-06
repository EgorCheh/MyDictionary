package com.example.cheho.mydictionary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class AddNewWordsTop5000 extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTranslation;
    private EditText etWord;
    private SQLiteDatabase mDb;
    private Random rand = new Random();
    private TextToSpeech textToSpeech;
    private ContentValues cv;
    private Word currentWord;
    private ArrayList<Word> words = new ArrayList<>();
    private Animation animOut,animIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_words);


        Toolbar toolbar =findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_to_home);

        tvTranslation=findViewById(R.id.tvTranslation);
        Button btnCheck = findViewById(R.id.btnCheck);
        Button btnShowWord = findViewById(R.id.btnShowWord);
        etWord=findViewById(R.id.etWord);
        Button btnAddWord=findViewById(R.id.buttAddWordTop);
        btnAddWord.setOnClickListener(this);
        Button btnNextWord = findViewById(R.id.btnNextWord);
        btnNextWord.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnShowWord.setOnClickListener(this);

         animOut = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
         animIn = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        DatabaseHelper mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        mDb = mDBHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM words WHERE word NOT IN (SELECT word FROM study)", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            currentWord = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2),0, 0);
            words.add(currentWord);
            cursor.moveToNext();
        }
        cursor.close();
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setNewWord();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        setNewWord();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheck:

                if(currentWord.getWord().equals(etWord.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), R.string.toastCorrectly, Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(getApplicationContext(), R.string.toastWrong, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnShowWord:
                Toast.makeText(getApplicationContext(), currentWord.getWord(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnNextWord:

                tvTranslation.startAnimation(animOut);




                break;
            case R.id.buttAddWordTop:
                words.remove(currentWord);
                Intent intent = new Intent(this, SearchImage.class);
                intent.putExtra("translation", currentWord.getTranslation());
                intent.putExtra("word", currentWord.getWord());
                startActivityForResult(intent,1);



                break;
                default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}

        try {
            ImagesUploader imagesUploader = new ImagesUploader(data.getStringExtra("URL"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cv.put("URL",data.getStringExtra("URL"));
        mDb.insert("study", null, cv );
        Toast.makeText(getApplicationContext(),R.string.toastAddWord,Toast.LENGTH_SHORT).show();
        setNewWord();
    }

    private void  setNewWord()
    {
        currentWord = words.get(rand.nextInt(words.size()));
        tvTranslation.setText(currentWord.getTranslation());

        tvTranslation.startAnimation(animIn);
        cv = new ContentValues();
        cv.put("word", currentWord.getWord());
        cv.put("translation", currentWord.getTranslation());
    }

    public void onPause()
    {
        if(textToSpeech!=null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
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
