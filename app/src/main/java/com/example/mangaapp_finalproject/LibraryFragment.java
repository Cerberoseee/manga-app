package com.example.mangaapp_finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.api.type.Manga.MangaDetailResponse;
import com.example.mangaapp_finalproject.api.type.Manga.MangaResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A fragment representing a list of Items.
 */
public class LibraryFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    androidx.appcompat.widget.Toolbar toolbarMain;

    Manga[] manga;
    LibraryRecyclerViewAdapter libraryAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LibraryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LibraryFragment newInstance(int columnCount) {
        LibraryFragment fragment = new LibraryFragment();
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
        View view = inflater.inflate(R.layout.fragment_library_list, container, false);

        toolbarMain = getActivity().findViewById(R.id.toolbarMain);
        toolbarMain.setVisibility(View.VISIBLE);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            getManga(context, recyclerView);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getManga(Context context, RecyclerView recyclerView) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Relationship.class, new RelationshipDeserializer());
        Gson gson = gsonBuilder.create();

        Retrofit retrofitRelate = new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofitRelate.create(ApiService.class);

        SharedPreferences prefs = context.getSharedPreferences("library",Context.MODE_PRIVATE);

        Set<String> set = prefs.getStringSet("library", null);
        List<String> libraryList = new ArrayList<>();
        if (set.toArray().length != 0) {
            libraryList = new ArrayList<String>(set);
            Call<MangaResponse> mangaApiCall = apiService.getManga(
                    new String[]{"author", "artist", "cover_art"},
                    100,
                    0,
                    null,
                    null,
                    libraryList.toArray(new String[0]),
                    null,
                    null,
                    null,
                    null
            );

            mangaApiCall.enqueue(new Callback<MangaResponse>() {
                @Override
                public void onResponse(Call<MangaResponse> call, Response<MangaResponse> response) {
                    if (response.isSuccessful()) {
                        MangaResponse res = response.body();
                        manga = res.data;
                        libraryAdapter = new LibraryRecyclerViewAdapter(context, manga, LibraryFragment.this, recyclerView);
                        recyclerView.setAdapter(libraryAdapter);
                    }
                }

                @Override
                public void onFailure(Call<MangaResponse> call, Throwable t) {
                    Log.e("err", t.toString());
                    Toast.makeText(context, "Unable to fetch manga", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

//    @Override
//    public void onRefresh() {
//        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(false);
//            }
//        }, 2000);
//    }
}