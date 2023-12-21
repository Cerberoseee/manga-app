package com.example.mangaapp_finalproject.browse;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaapp_finalproject.HistoryRecyclerViewAdapter;
import com.example.mangaapp_finalproject.LibraryRecyclerViewAdapter;
import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.api.type.Manga.Manga;

import java.util.ArrayList;

public class BrowseFragment extends Fragment {

    androidx.appcompat.widget.Toolbar toolbarMain;

    ArrayList<Manga> manga = new ArrayList<>();
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

        newestAdapter = new NewestRecyclerViewAdapter(context, manga);
        hottestAdapter = new HottestRecyclerViewAdapter(context, manga);
        mostRatedAdapter = new MostRatedRecyclerViewAdapter(context, manga);

        newestMangaList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        hottestMangaList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mostRatedMangaList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        newestMangaList.setAdapter(newestAdapter);
        hottestMangaList.setAdapter(hottestAdapter);
        mostRatedMangaList.setAdapter(mostRatedAdapter);

        return view;
    }
}