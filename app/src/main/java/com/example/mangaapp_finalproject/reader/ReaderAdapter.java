package com.example.mangaapp_finalproject.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.example.mangaapp_finalproject.api.type.Chapter.ChapterImageResponse;

public class ReaderAdapter extends FragmentStatePagerAdapter {
    private ChapterImageResponse chapterPathList;
    private  Context context;

    public ReaderAdapter(@NonNull FragmentManager fm, ChapterImageResponse chapterPathList, Context context) {
        super(fm);
        this.context = context;
        this.chapterPathList = chapterPathList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        SharedPreferences dataSaverPref =  context.getSharedPreferences("DATA_SAVER", Context.MODE_PRIVATE);
        boolean dataSaver = dataSaverPref.getBoolean("dataSaver", false);

        Bundle bundle = new Bundle();
        bundle.putString("baseUrl", chapterPathList.baseUrl);
        bundle.putString("hash", chapterPathList.chapter.hash);

        if (!dataSaver) {
            bundle.putString("id", chapterPathList.chapter.data[position]);
        } else {
            bundle.putString("id", chapterPathList.chapter.dataSaver[position]);
        }
        bundle.putBoolean("dataSaver", dataSaver);

        ChapterImageFragment fragmentObj = new ChapterImageFragment();
        fragmentObj.setArguments(bundle);
        return fragmentObj;
    }

    @Override
    public int getCount() {
        return chapterPathList.chapter.data.length;
    }
}
