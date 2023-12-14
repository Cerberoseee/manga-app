package com.example.mangaapp_finalproject.api;

import com.example.mangaapp_finalproject.api.type.Statistic.Statistic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("current.json")
    Call<MangaApiResponse> getManga(
        @Query("key") String apiKey,
        @Query("q") String location
    );

    @GET("/statistics/manga/{id}")
    Call<Statistic> getStatistic(@Path(value = "id", encoded = true) String id);
}


