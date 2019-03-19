package com.example.cheho.mydictionary;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {

    @GET("/api/")
    Call<PixabayImageList> getImageResults(@Query("key") String key,@Query("q") String query, @Query("lang ") String lang ,@Query("per_page") int perPage);
}
