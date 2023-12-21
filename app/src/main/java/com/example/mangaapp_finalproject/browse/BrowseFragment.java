package com.example.mangaapp_finalproject.browse;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaapp_finalproject.HistoryRecyclerViewAdapter;
import com.example.mangaapp_finalproject.LibraryFragment;
import com.example.mangaapp_finalproject.LibraryRecyclerViewAdapter;
import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.api.type.Manga.MangaResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrowseFragment extends Fragment {

    androidx.appcompat.widget.Toolbar toolbarMain;
    Manga[] manga;

    NewestRecyclerViewAdapter newestAdapter;
    HottestRecyclerViewAdapter hottestAdapter;
    MostRatedRecyclerViewAdapter mostRatedAdapter;
    RecyclerView newestMangaList, hottestMangaList, mostRatedMangaList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        toolbarMain = getActivity().findViewById(R.id.toolbarMain);
        toolbarMain.setVisibility(View.VISIBLE);

        Context context = view.getContext();

        newestMangaList = view.findViewById(R.id.newestMangaList);
        hottestMangaList = view.findViewById(R.id.hottestMangaList);
        mostRatedMangaList = view.findViewById(R.id.mostRatedMangaList);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Relationship.class, new RelationshipDeserializer());
        Gson gson = gsonBuilder.create();

        Retrofit retrofitRelate = new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofitRelate.create(ApiService.class);

        Call<MangaResponse> mangaApiCall = apiService.getManga(
                new String[]{"cover_art"},
                20,
                0,
                null,
                new String[]{"safe", "suggestive"},
                null,
                null,
                null,
                "desc"
        );

        mangaApiCall.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                if (response.isSuccessful()) {
                    MangaResponse res = response.body();
                    manga = res.data;
                    newestAdapter = new NewestRecyclerViewAdapter(context, manga);
                    newestMangaList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    newestMangaList.setAdapter(newestAdapter);
                }
            }

            @Override
            public void onFailure(Call<MangaResponse> call, Throwable t) {
                Log.e("err", t.toString());
                Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
            }
        });

        mangaApiCall = apiService.getManga(
                new String[]{"cover_art"},
                20,
                0,
                null,
                new String[]{"safe", "suggestive"},
                null,
                "desc",
                null,
                null
        );

        mangaApiCall.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                if (response.isSuccessful()) {
                    MangaResponse res = response.body();
                    manga = res.data;
                    hottestAdapter = new HottestRecyclerViewAdapter(context, manga);
                    hottestMangaList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    hottestMangaList.setAdapter(hottestAdapter);
                }
            }

            @Override
            public void onFailure(Call<MangaResponse> call, Throwable t) {
                Log.e("err", t.toString());
                Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
            }
        });

        mangaApiCall = apiService.getManga(
                new String[]{"cover_art"},
                20,
                0,
                null,
                new String[]{"safe", "suggestive"},
                null,
                null,
                "desc",
                null
        );

        mangaApiCall.enqueue(new Callback<MangaResponse>() {
            @Override
            public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                if (response.isSuccessful()) {
                    MangaResponse res = response.body();
                    manga = res.data;

                    mostRatedAdapter = new MostRatedRecyclerViewAdapter(context, manga);
                    mostRatedMangaList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    mostRatedMangaList.setAdapter(mostRatedAdapter);
                }
            }

            @Override
            public void onFailure(Call<MangaResponse> call, Throwable t) {
                Log.e("err", t.toString());
                Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}