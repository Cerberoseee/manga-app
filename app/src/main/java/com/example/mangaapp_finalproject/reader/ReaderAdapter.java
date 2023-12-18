package com.example.mangaapp_finalproject.reader;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.example.mangaapp_finalproject.api.type.Chapter.ChapterImageResponse;

public class ReaderAdapter extends FragmentStatePagerAdapter {
    private ChapterImageResponse chapterPathList;

    public ReaderAdapter(@NonNull FragmentManager fm, ChapterImageResponse chapterPathList) {
        super(fm);
        this.chapterPathList = chapterPathList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("baseUrl", chapterPathList.baseUrl);
        bundle.putString("hash", chapterPathList.chapter.hash);
        bundle.putString("id", chapterPathList.chapter.data[position]);

        ChapterImageFragment fragmentObj = new ChapterImageFragment();
        fragmentObj.setArguments(bundle);
        return fragmentObj;
    }

    @Override
    public int getCount() {
        return chapterPathList.chapter.data.length;
    }
}
