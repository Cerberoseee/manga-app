package com.example.mangaapp_finalproject.reader;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mangaapp_finalproject.R;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

public class ChapterImageFragment extends Fragment {
    private static final String IMAGE_URL = "imageUrl";
    private String imageUrl;
    public ChapterImageFragment() {}

    public static ChapterImageFragment newInstance(String imageUrl) {
        ChapterImageFragment fragment = new ChapterImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        PhotoView image = (PhotoView) getView().findViewById(R.id.photo_view);
        Picasso.get().load("https://picsum.photos/200/300").into(image);
        PhotoViewAttacher mAttach = new PhotoViewAttacher(image);
        mAttach.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                Log.i("tap", "tap");
                ((ReaderActivity)getActivity()).toggleMenu();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chapter_image, container, false);
    }
}
