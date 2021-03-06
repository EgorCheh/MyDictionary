package com.example.cheho.mydictionary;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class PixabayService {

    static PixabayApi createPixabayService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://pixabay.com/");

        return builder.build().create(PixabayApi.class);


    }
}
