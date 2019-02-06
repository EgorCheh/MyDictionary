package com.example.cheho.mydictionary;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.io.IOException;
import java.util.Locale;

public class Training extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTranslation;
    private Button btnCheck;
    private EditText etWord;
    private SQLiteDatabase mDb;
    private String word;
    private Cursor cursor;
    private int counterCorrectlyAnswer=0;
    private final String KEY_INDEX = "randID";
    private TextToSpeech textToSpeech;
    private String toSpeak;
    private Word currentWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        tvTranslation=findViewById(R.id.tvTranslationTraining);
        btnCheck=findViewById(R.id.btnCheckTraining);
        Button btnShowWord = findViewById(R.id.btnShowWordTraining);
        etWord=findViewById(R.id.etWordTraining);
        String LOG_TAG = "myLogs";
        Log.d(LOG_TAG,"_______ONCREATE________");
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

        cursor = mDb.rawQuery("SELECT * FROM study", null);

        if (savedInstanceState != null)
        {
            cursor.moveToPosition( savedInstanceState.getInt(KEY_INDEX,0));


        }
        else cursor.moveToFirst();
        setNewWord();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckTraining:

                if(word.equals(etWord.getText().toString()))
                {
                    textToSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                    counterCorrectlyAnswer++;
                    Toast.makeText(getApplicationContext(), R.string.toastCorrectly, Toast.LENGTH_SHORT).show();
                    if(counterCorrectlyAnswer==3)
                    {
                        ContentValues cv = new ContentValues();
                        cv.put("studyLVL",currentWord.getLevel()+1);
                        cv.put("counter",currentWord.getLevel()+1);
                        mDb.update("study", cv, "_id=?", new String[]{currentWord.getIDToString()});
                        counterCorrectlyAnswer=0;
                        setNewWord();
                    }
                    else
                    { Toast.makeText(getApplicationContext(), R.string.toastRepeat, Toast.LENGTH_SHORT).show();}
                    etWord.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.toastWrong, Toast.LENGTH_SHORT).show();
                counterCorrectlyAnswer=0;
                }
                break;
            case R.id.btnShowWordTraining:
                Toast.makeText(getApplicationContext(), word, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                counterCorrectlyAnswer=0;
                break;

            default:
                break;
        }
    }


    private void  setNewWord()
    {
        boolean notFoundWord=true;
        while (!cursor.isAfterLast()&&notFoundWord) {

             if(cursor.getInt(4)<1)
            {
              Log.d("Study",cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4));
              word = cursor.getString(1);
              tvTranslation.setText(cursor.getString(2));
              toSpeak= word;
              currentWord= new Word(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
               notFoundWord=false;
            }
            cursor.moveToNext();

        }

        if(notFoundWord)
        {
            Toast.makeText(getApplicationContext(), R.string.toastTrainingEnd, Toast.LENGTH_SHORT).show();
            btnCheck.setEnabled(false);
        }

    }







    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        int ID = cursor.getPosition();
        savedInstanceState.putInt(KEY_INDEX, ID);    }

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
