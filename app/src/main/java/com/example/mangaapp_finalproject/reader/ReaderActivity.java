package com.example.mangaapp_finalproject.reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.DirectionalViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mangaapp_finalproject.R;

public class ReaderActivity extends AppCompatActivity {
    DirectionalViewPager reader;
    ReaderAdapter readerAdapter;
    FrameLayout readerContainer;
    LinearLayout menu1;
    LinearLayout menu2;
    boolean isShowMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        reader = findViewById(R.id.reader);
        menu1 = findViewById(R.id.menu_up);
        menu2 = findViewById(R.id.menu_down);
        readerContainer = findViewById(R.id.reader_container);

        menu1.setVisibility(View.GONE);
        menu2.setVisibility(View.GONE);

        readerAdapter = new ReaderAdapter(getSupportFragmentManager());
        reader.setAdapter(readerAdapter);
    }

    public void toggleMenu() {
        if (isShowMenu) {
            isShowMenu = false;
            menuOffAnimation();
        } else {
            isShowMenu = true;
            menuOnAnimation();
        }
    }

    private void menuOnAnimation() {
        menu1.setTranslationY(-500);
        menu2.setTranslationY(500);

        menu1.setVisibility(View.VISIBLE);
        menu2.setVisibility(View.VISIBLE);

        menu1.animate()
                .translationY(0)
                .setDuration(500)
                .setListener(null);
        menu2.animate()
                .translationY(0)
                .setDuration(500)
                .setListener(null);
    }

    private void menuOffAnimation() {
        menu1.animate()
                .translationY(-500)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        menu1.setVisibility(View.GONE);
                    }
                });
        menu2.animate()
                .translationY(500)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        menu2.setVisibility(View.GONE);

                    }
                });
    }

}