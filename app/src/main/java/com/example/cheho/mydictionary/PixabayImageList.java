package com.example.cheho.mydictionary;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class PixabayImageList {

    private int total;
    private int totalHits;
    private ArrayList<PixabayImage> hits;

    public PixabayImageList(int total, int totalHits, ArrayList<PixabayImage> hits) {
        this.total = total;
        this.totalHits = totalHits;
        this.hits = hits;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public ArrayList<PixabayImage> getHits() {
        return hits;
    }

    public int getTotalOfPages() {
        return (int) Math.ceil(total / 20.0);
    }
}