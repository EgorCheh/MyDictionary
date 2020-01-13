package com.example.cheho.mydictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ImagesUploader   {

        ImagesUploader(String scr) throws IOException {

            String destinationFile = "image.jpg";
            new DownloadImageTask().execute("http://www.avajava.com/images/avajavalogo.jpg", destinationFile);

        }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap mIcon11 = null;

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        @SuppressLint("WrongThread")
        protected void onPostExecute(Bitmap result) {
           Log.d("bitma", mIcon11.toString());

            try {
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "cat.jpg");
                mIcon11.compress(Bitmap.CompressFormat.JPEG, 75, fos);

                fos.flush();
                fos.close();
            } catch (Exception e) {
                Log.e("MyLog", e.toString());
            }
        }
    }



} 