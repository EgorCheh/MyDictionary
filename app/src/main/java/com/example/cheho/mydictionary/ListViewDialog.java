package com.example.cheho.mydictionary;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.util.HashMap;
import java.util.Objects;


public class ListViewDialog extends DialogFragment {

    private ContentValues contentValues=new ContentValues();
    private SimpleAdapter simpleAdapter;


    public interface NoticeDialogListener {
         void onDialogPositiveClick(DialogFragment dialog);
    }

    NoticeDialogListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {

            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        return builder
                .setTitle("Хотите добавить слово ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(contentValues.get("word")+" - "+contentValues.get("translation"))
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("Dialog","Positive");
                        if(!checkHasWord())
                        {
                            simpleAdapter.notifyDataSetChanged();
                            mListener.onDialogPositiveClick(ListViewDialog.this);

                        }
                        else Toast.makeText(getContext(), getString(R.string.toast_word_has_already_added),Toast.LENGTH_SHORT).show();
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

    public void setParam(HashMap hashMap,SimpleAdapter adapter)
    {
        contentValues.put("word", Objects.requireNonNull(hashMap.get("word")).toString());
        contentValues.put("translation", Objects.requireNonNull(hashMap.get("translation")).toString());
        simpleAdapter=adapter;

    }


    private boolean checkHasWord(){
        DatabaseHelper mDBHelper = new DatabaseHelper(getContext());
        SQLiteDatabase mDb = mDBHelper.getWritableDatabase();
        Cursor cursorCheck = mDb.rawQuery("SELECT * FROM study", null);
        cursorCheck.moveToFirst();


        while (!cursorCheck.isAfterLast()) {
            if(cursorCheck.getString(1).equals(contentValues.getAsString("word")))
            {
                cursorCheck.close();
                return true;
            }

            cursorCheck.moveToNext();
        }

        cursorCheck.close();
        return false;
    }

}
