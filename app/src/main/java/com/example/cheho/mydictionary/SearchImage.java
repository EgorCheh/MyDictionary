package com.example.cheho.mydictionary;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.cheho.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchImage extends AppCompatActivity {
    ArrayList<String> imageUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);
        Intent intent = getIntent();
        loadImages(1,intent.getStringExtra("translation"),"all","ru");
        loadImages(1,intent.getStringExtra("word"),"all","en");

    }
   private void loadImages(int page, String query, String currentOrientation,String lang) {

        PixabayService.createPixabayService().getImageResults(getString(R.string.PIXABAY_API_KEY),query,lang,50).enqueue(new Callback<PixabayImageList>() {

            @Override
            public void onResponse(Call<PixabayImageList> call, Response<PixabayImageList> response) {


                collectURLs(response.body().getHits());

            }

            @Override
            public void onFailure(Call<PixabayImageList> call, Throwable t) {


            }

        });
    }
    private void collectURLs(ArrayList<PixabayImage> list)
    {

        for (PixabayImage item:list)
        imageUrls.add(item.getWebformatURL());



        GridView gridView =  findViewById(R.id.grid_view);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        int width = p.x;
        int height = p.y;

        gridView.setAdapter(new ImageAdapter(this,imageUrls,width,height));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setTransitionName("imageTrans");


                Intent intent = new Intent(SearchImage.this, DetailImageActivity.class);



                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(SearchImage.this,
                                view,
                                ViewCompat.getTransitionName(view));

                intent.putExtra("URL", imageUrls.get(i));
                startActivityForResult(intent,1,options.toBundle());

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        setResult(RESULT_OK, data);
        finish();
    }


    }
