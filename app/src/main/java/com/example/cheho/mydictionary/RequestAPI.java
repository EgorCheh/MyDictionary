package com.example.cheho.mydictionary;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestAPI {

    @FormUrlEncoded
     @POST("/api/v1.5/tr.json/translate")
     Call<Object> RUS(@FieldMap Map<String,String>map);
}
