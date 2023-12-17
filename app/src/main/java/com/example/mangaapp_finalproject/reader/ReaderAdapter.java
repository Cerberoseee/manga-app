package com.example.mangaapp_finalproject.reader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReaderAdapter extends FragmentStatePagerAdapter {

    public ReaderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChapterImageFragment();
            case 1:
                return new ChapterImageFragment();
            case 2:
                return new ChapterImageFragment();
            case 3:
                return new ChapterImageFragment();
            default:
                return new ChapterImageFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
