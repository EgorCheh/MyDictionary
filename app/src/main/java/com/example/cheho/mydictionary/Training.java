package com.example.cheho.mydictionary;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Training extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTranslation;
    private EditText etWord;
    private SQLiteDatabase mDb;
    private TextToSpeech textToSpeech;
    private ArrayList<Word> words = new ArrayList<>();
    private Word currentWord;
    private Random rand = new Random();
    private String checkWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        tvTranslation=findViewById(R.id.tvTranslationTraining);
        Button btnCheck = findViewById(R.id.btnCheckTraining);
        Button btnShowWord = findViewById(R.id.btnShowWordTraining);
        etWord=findViewById(R.id.etWordTraining);
        ImageButton imaButHearing = findViewById(R.id.imButtHearing);
        imaButHearing.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnShowWord.setOnClickListener(this);


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
        Word currentWord;
        Cursor cursor = mDb.rawQuery("SELECT * FROM study", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getInt(4)<1)
            { currentWord = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));
            words.add(currentWord);}
            cursor.moveToNext();
        }
        cursor.close();

        setNewWord();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckTraining:

               if(checkWord.equals(etWord.getText().toString())) {
                   etWord.setText("");
                   currentWord.successfulRepetition();
                   if (currentWord.repetitionCompleted()){
                       words.remove(currentWord);
                       ContentValues cv = new ContentValues();
                       cv.put("studyLVL",currentWord.getLevel()+1);
                       cv.put("counter",currentWord.getLevel()+1);
                       mDb.update("study", cv, "_id=?", new String[]{currentWord.getIDToString()});
                   }
                   else {
                       Toast.makeText(getApplicationContext(),R.string.toastRepeat,Toast.LENGTH_LONG).show();
                   }
                   setNewWord();
               }else {
                   currentWord.unSuccessfulRepetition();
                   Toast.makeText(getApplicationContext(),R.string.toastWrong,Toast.LENGTH_LONG).show();
               }


                break;
            case R.id.btnShowWordTraining:
                Toast.makeText(getApplicationContext(), checkWord, Toast.LENGTH_SHORT).show();
                currentWord.unSuccessfulRepetition();
                break;
            case R.id.imButtHearing:
                textToSpeech.speak(currentWord.getWord(),TextToSpeech.QUEUE_FLUSH,null);
                break;

            default:
                break;
        }
    }


    private void  setNewWord()
    {
        if(!words.isEmpty())
        {
            String [] allTranslation = {"rus","eng"};
            String currentTranslation = allTranslation[rand.nextInt(allTranslation.length)];
            currentWord = words.get(rand.nextInt(words.size()));

            if (currentTranslation.equals("rus")) {
                tvTranslation.setText(currentWord.getTranslation());
                checkWord = currentWord.getWord();
            }else {
                tvTranslation.setText(currentWord.getWord());
                checkWord=currentWord.getTranslation();
            }

          //  Log.d("Training",currentWord.toString());

        }else {
            Toast.makeText(getApplicationContext(),R.string.toastTrainingEnd,Toast.LENGTH_LONG).show();
            etWord.setText("");
            tvTranslation.setText("");
            etWord.setEnabled(false);
        }

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


}
