package com.example.cheho.mydictionary;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.cheho.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchImage extends AppCompatActivity {
    private ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);
        loadImages(1,"","all");


    }
   private void loadImages(int page, String query, String currentOrientation) {

        PixabayService.createPixabayService().getImageResults(getString(R.string.PIXABAY_API_KEY),query,"ru",50).enqueue(new Callback<PixabayImageList>() {

            @Override
            public void onResponse(Call<PixabayImageList> call, Response<PixabayImageList> response) {

                Log.d("adsasdasd","Successful "+response.body().toString());
                setPixabayImageList(response.body().getHits());

            }

            @Override
            public void onFailure(Call<PixabayImageList> call, Throwable t) {
                Log.d("adsasdasd","throwable "+t.getMessage());

            }

        });
    }
    private void setPixabayImageList(List<PixabayImage> list)
    {


        String[] imageUrls = new String[list.size()];
        for (int i = 0; i < list.size();i++)
        imageUrls[i]=list.get(i).getWebformatURL();

        GridView gridView =  findViewById(R.id.grid_view);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int width = p.x;
        int height = p.y;
        Log.d("adsasdasd","width = "+width+"  height = "+height );
        gridView.setAdapter(new ImageAdapter(this,imageUrls,width,height));
    }




    }
