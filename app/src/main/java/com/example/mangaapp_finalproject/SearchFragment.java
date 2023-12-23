package com.example.mangaapp_finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.api.type.Manga.MangaResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
import com.example.mangaapp_finalproject.browse.NewestRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a list of Items.
 */
public class SearchFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    androidx.appcompat.widget.Toolbar toolbarMain;
    Context context;
    Manga[] manga;
    SearchRecyclerViewAdapter historyAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
//    public SearchFragment(Context context) {
//        this.context = context;
//    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SearchFragment newInstance(int columnCount) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        toolbarMain = getActivity().findViewById(R.id.toolbarMain);
        toolbarMain.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        context = view.getContext();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Relationship.class, new RelationshipDeserializer());
        Gson gson = gsonBuilder.create();

        Retrofit retrofitRelate = new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofitRelate.create(ApiService.class);

        if ( getArguments()!= null) {
            String filterID = getArguments().getString("filterId");
            Call<MangaResponse> mangaApiCall = apiService.getManga(
                    new String[]{"cover_art", "author", "artist"},
                    20,
                    0,
                    null,
                    new String[]{"safe", "suggestive"},
                    null,
                    null,
                    null,
                    "desc",
                    null,
                    new String[]{filterID}
            );
            mangaApiCall.enqueue(new Callback<MangaResponse>() {
                @Override
                public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                    if (response.isSuccessful()) {
                        MangaResponse res = response.body();
                        manga = res.data;
                        historyAdapter = new SearchRecyclerViewAdapter(context, manga);
                        recyclerView.setAdapter(historyAdapter);
                    }
                }

                @Override
                public void onFailure(Call<MangaResponse> call, Throwable t) {
                    Log.e("err", t.toString());
                    Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
                }
            });

            androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Toast.makeText(context, "Manga searching", Toast.LENGTH_SHORT).show();

                    Call<MangaResponse> mangaApiCall = apiService.getManga(
                            new String[]{"cover_art", "author", "artist"},
                            20,
                            0,
                            null,
                            new String[]{"safe", "suggestive"},
                            null,
                            null,
                            null,
                            "desc",
                            s,
                            new String[]{filterID}
                    );
                    mangaApiCall.enqueue(new Callback<MangaResponse>() {
                        @Override
                        public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                            if (response.isSuccessful()) {
                                MangaResponse res = response.body();
                                manga = res.data;
                                historyAdapter = new SearchRecyclerViewAdapter(context, manga);
                                recyclerView.setAdapter(historyAdapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<MangaResponse> call, Throwable t) {
                            Log.e("err", t.toString());
                            Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
                        }
                    });


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        androidx.appcompat.widget.SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(context, "Manga searching", Toast.LENGTH_SHORT).show();

                Call<MangaResponse> mangaApiCall = apiService.getManga(
                        new String[]{"cover_art", "author", "artist"},
                        20,
                        0,
                        null,
                        new String[]{"safe", "suggestive"},
                        null,
                        null,
                        null,
                        "desc",
                        s,
                        null
                );
                mangaApiCall.enqueue(new Callback<MangaResponse>() {
                    @Override
                    public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                        if (response.isSuccessful()) {
                            MangaResponse res = response.body();
                            manga = res.data;
                            historyAdapter = new SearchRecyclerViewAdapter(context, manga);
                            recyclerView.setAdapter(historyAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<MangaResponse> call, Throwable t) {
                        Log.e("err", t.toString());
                        Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
                    }
                });


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        // Set the adapter
//
//        }
        return view;
    }
}