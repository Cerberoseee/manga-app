package com.example.mangaapp_finalproject.api;

import com.example.mangaapp_finalproject.api.type.Chapter.ChapterResponse;
import com.example.mangaapp_finalproject.api.type.Manga.MangaResponse;
import com.example.mangaapp_finalproject.api.type.Statistic.Statistic;
import com.example.mangaapp_finalproject.api.type.Statistic.StatisticResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/manga")
    Call<MangaResponse> getManga(
        @Query("includes[]") String[] includes,
        @Query("limit") Integer limit,
        @Query("offset") Integer offset,
        @Query("includedTags[]") String[] includeTagId,
        @Query("contentRating[]") String[] contentRate
    );
    @GET("/manga/{id}")
    Call<MangaResponse> getMangaDetail(
        @Path(value = "id", encoded = true) String id,
        @Query("includes[]") String[] query
    );
    @GET("/manga/tag")
    Call<MangaResponse> getMangaTag(
        @Query("limit") Integer limit,
        @Query("offset") Integer offset
    );
    @GET("/manga/{id}/feed")
    Call<ChapterResponse> getMangaChapters(
        @Path(value = "id", encoded = true) String id,
        @Query("translatedLanguage[]") String[] languages,
        @Query("includes[]") String[] includes,
        @Query("limit") Integer limit,
        @Query("offset") Integer offset
    );
    @GET("/manga/random")
    Call<MangaResponse> getMangaRandom(
            @Query("includes[]") String[] includes,
            @Query("contentRating[]") String[] contentRate
    );
    @GET("/at-home/server/{id}")
    Call<ChapterResponse> getChapterImageUrl(
            @Path(value = "id", encoded = true) String id
    );
    @GET("/statistics/manga/{id}")
    Call<StatisticResponse> getStatistic(@Path(value = "id", encoded = true) String id);

}

