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
import java.util.concurrent.TimeUnit;

public class AddNewWordsTop5000 extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTranslation;
    private EditText etWord;
    private SQLiteDatabase mDb;
    private String word;
    private Cursor cursor;
    private final String LOG_TAG = "myLogs";
    private int randID;
    private final String KEY_INDEX = "randID";
    private TextToSpeech textToSpeech;
    private String toSpeak;
    private ContentValues cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_words);
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
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (savedInstanceState != null) {
            randID = savedInstanceState.getInt(KEY_INDEX,0);
            cursor = mDb.rawQuery("SELECT * FROM words", null);
            cursor.moveToPosition(randID);
            tvTranslation.setText(cursor.getString(2));
            word = cursor.getString(1);
        }else
        setNewWord();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheck:

                if(word.equals(etWord.getText().toString()))
                {textToSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                    Toast.makeText(getApplicationContext(), R.string.toastCorrectly, Toast.LENGTH_SHORT).show();}
                else {
                    Toast.makeText(getApplicationContext(), R.string.toastWrong, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnShowWord:
                Toast.makeText(getApplicationContext(), word, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
                break;
            case R.id.btnNextWord:
                setNewWord();
                break;
            case R.id.buttAddWordTop:
                mDb.insert("study", null, cv);
                Toast.makeText(getApplicationContext(),R.string.toastAddWord,Toast.LENGTH_SHORT).show();
                break;
                default:
                break;
        }
    }


    private void  setNewWord()
    {   randID =(int)( 0 + Math.random()*4999);
        Log.d(LOG_TAG, "_______"+randID+"______________" );

        cursor = mDb.rawQuery("SELECT * FROM words", null);
        cursor.moveToPosition(randID);

        word = cursor.getString(1);

        if(!checkHasWord())
        {
        cv = new ContentValues();
        cv.put("word", cursor.getString(1));
        cv.put("translation", cursor.getString(2));
        toSpeak= word;
        tvTranslation.setText(cursor.getString(2));
        } else
            setNewWord();

    }

   private boolean checkHasWord(){
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, randID);    }

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
