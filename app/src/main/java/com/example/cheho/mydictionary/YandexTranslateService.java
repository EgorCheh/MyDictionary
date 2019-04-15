package com.example.cheho.mydictionary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class YandexTranslateService {

    static YandexTranslateAPI createYandexTranslateService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://translate.yandex.net");

        return builder.build().create(YandexTranslateAPI.class);

    }
}
