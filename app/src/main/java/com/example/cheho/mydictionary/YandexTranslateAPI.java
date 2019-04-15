package com.example.cheho.mydictionary;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslateAPI {


     @GET("/api/v1.5/tr.json/translate")
     Call<YandexResponse> translate (@Query("key") String key, @Query("text") String text, @Query("lang") String lang );
}
