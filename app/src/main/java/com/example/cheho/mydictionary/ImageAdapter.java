package com.example.cheho.mydictionary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cheho.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String> imageUrls;
    private int width,height;

    public ImageAdapter(Context context, ArrayList<String> images, int widthF, int heightF) {
        this.context = context;
        this.imageUrls=images;
        this.height=heightF;
        this.width=widthF;
//
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int i) {
        return imageUrls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        Picasso.get().load(imageUrls.get(position)).resize(width/2,width/2).into(imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(width/2, width/2));
        return imageView;
    }
}
