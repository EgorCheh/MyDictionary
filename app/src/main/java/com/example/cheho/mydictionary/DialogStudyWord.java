package com.example.cheho.mydictionary;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DialogStudyWord extends DialogFragment {

    private ContentValues contentValues=new ContentValues();
    private SimpleAdapter simpleAdapter;
    private ArrayList words;
    private int position;


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        return builder
                .setTitle("Хотите удалить слово ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(contentValues.get("word")+" - "+contentValues.get("translation"))
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseHelper mDBHelper = new DatabaseHelper(getContext());
                        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();

                        Log.d("Dialog","Positive");
                        words.remove(position);
                        mDb.delete("study",  "_id=?", new String[]{contentValues.getAsString("id")});
                        simpleAdapter.notifyDataSetChanged();

                        Toast.makeText(getContext(), getString(R.string.toast_delete_word),Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Dialog","Negative");
                    }
                })
                .create();

    }

    public void setParam(HashMap hashMap, SimpleAdapter adapter, ArrayList arrayList,int positionItem)  {
        contentValues.put("id", Objects.requireNonNull(hashMap.get("id")).toString());
        contentValues.put("word", Objects.requireNonNull(hashMap.get("word")).toString());
        contentValues.put("translation", Objects.requireNonNull(hashMap.get("translation")).toString());
        simpleAdapter=adapter;
        words=arrayList;
        position=positionItem;

    }

}
