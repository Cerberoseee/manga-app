package com.example.mangaapp_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.api.type.Manga.MangaDetailResponse;
import com.example.mangaapp_finalproject.api.type.Manga.MangaResponse;
import com.example.mangaapp_finalproject.browse.BrowseFragment;
import com.example.mangaapp_finalproject.databinding.ActivityMainBinding;
import com.example.mangaapp_finalproject.detail.MangaInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeLayout;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbarMain;
    SharedPreferences darkModeSharePref;
    int darkMode;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment currFragment;
    Fragment active;
    enum Screen {
        Library,
        Browse,
        Search,
        More,
        Filter
    }
    Screen screen = Screen.Library;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setItemBackgroundResource(R.drawable.bottom_nav_bar_item_bg);
        Menu menu = bottomNavigationView.getMenu();

        toolbarMain = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbarMain);

        currFragment = fragmentManager.findFragmentById(R.id.flMain);

        final Fragment libraryFragment = new LibraryFragment();
        final Fragment browseFragment = new BrowseFragment();
        final Fragment searchFragment = new SearchFragment();
        final Fragment moreFragment = new MoreFragment();
        final Fragment filterFragment = new SearchFragment();

        if(currFragment == null){
            addAllFragment(libraryFragment, browseFragment, searchFragment, moreFragment, filterFragment);
            active = fragmentManager.findFragmentByTag("1");
        }

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(MainActivity.this, "Refreshing list...", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);

                //refresh stuff here

                if(screen == Screen.Library) {
                    Fragment fragment = new LibraryFragment();
                    fragmentManager.beginTransaction()
                            .detach(fragmentManager.findFragmentByTag("1"))
                            .add(R.id.flMain, fragment, "1")
                            .commit();
                    active = fragment;
                }

                if(screen == Screen.Browse) {
                    Fragment fragment = new BrowseFragment();
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("2"))
                            .add(R.id.flMain, fragment, "2")
                            .commit();
                    active = fragment;
                }
                if(screen == Screen.Search) {
                    Fragment fragment = new SearchFragment();
                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("3"))
                            .add(R.id.flMain, fragment, "3")
                            .commit();
                    active = fragment;
                }
                if(screen == Screen.Filter) {
                    Fragment fragment = new SearchFragment();
                    String filterId = fragmentManager.findFragmentByTag("5").getArguments().getString("filterId");
                    Bundle bundle = new Bundle();
                    bundle.putString("filterId", filterId);
                    fragment.setArguments(bundle);

                    fragmentManager.beginTransaction()
                            .remove(fragmentManager.findFragmentByTag("5"))
                            .add(R.id.flMain, fragment, "5")
                            .commit();
                    active = fragment;
                }
            }

        });

        darkModeSharePref = getSharedPreferences("DARK_MODE", Context.MODE_PRIVATE);
        darkMode = darkModeSharePref.getInt("darkMode", 2);

        if(darkMode == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if(darkMode == 2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (darkMode == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            menu.findItem(R.id.libraryItem).setIcon(R.drawable.ic_library_nav);
            menu.findItem(R.id.browseItem).setIcon(R.drawable.ic_browser_nav);

//            hideAllFragment(libraryFragment, browseFragment, searchFragment, moreFragment);

            if(item.getItemId() == R.id.libraryItem){
                item.setIcon(R.drawable.ic_library_nav_selected);
                changeFragment(fragmentManager.findFragmentByTag("1"), active);
                active = fragmentManager.findFragmentByTag("1");
                toolbarMain.setTitle("Library");
                screen = Screen.Library;
            }
            else if (item.getItemId() == R.id.browseItem) {
                item.setIcon(R.drawable.ic_browse_nav_selected);
                changeFragment(fragmentManager.findFragmentByTag("2"), active);
                active = fragmentManager.findFragmentByTag("2");
                toolbarMain.setTitle("Browse");
                screen = Screen.Browse;
            } else if (item.getItemId() == R.id.searchItem) {
                changeFragment(fragmentManager.findFragmentByTag("3"), active);
                active = fragmentManager.findFragmentByTag("3");
                toolbarMain.setTitle("Search");
                screen = Screen.Search;
            } else if (item.getItemId() == R.id.moreItem) {
                changeFragment(fragmentManager.findFragmentByTag("4"), active);
                active = fragmentManager.findFragmentByTag("4");
                toolbarMain.setTitle("More");
                screen = Screen.More;
            }

            return true;
        });
    }

    private void changeFragment(Fragment fragment, Fragment active){
        if(fragment.isHidden() && active != null) {
            fragmentManager.beginTransaction()
                    .hide(active)
                    .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                    )
                    .show(fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(fragment.getTag())
                    .commit();
        } else if (active == null) {
            this.active = fragmentManager.findFragmentByTag("4");
            fragmentManager.beginTransaction()
                    .hide(this.active)
                    .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                    )
                    .show(fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(fragment.getTag())
                    .commit();
            screen = Screen.Library;
        }
    }


    public void filterManga(String id, String name) {
        screen = Screen.Filter;
        Bundle bundle = new Bundle();
        bundle.putString("filterId", id);
        Fragment fragment = new SearchFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .hide(active)
                .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                )
                .remove(fragmentManager.findFragmentByTag("5"))
                .add(R.id.flMain, fragment, "5")
                .show(fragment)
                .setReorderingAllowed(true)
                .addToBackStack(fragment.getTag())
                .commit();

        active = fragment;
        toolbarMain.setTitle("Filter: " + name);
    }

    private void addAllFragment(Fragment fragment1, Fragment fragment2, Fragment fragment3, Fragment fragment4, Fragment fragment5){

        fragmentManager.beginTransaction()
                .add(R.id.flMain, fragment5, "5")
                .hide(fragment5)
                .add(R.id.flMain, fragment4, "4")
                .hide(fragment4)
                .add(R.id.flMain, fragment3, "3")
                .hide(fragment3)
                .add(R.id.flMain, fragment2, "2")
                .hide(fragment2)
                .add(R.id.flMain, fragment1, "1")
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintoolbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemRandom) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.mangadex.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<MangaDetailResponse> mangaCall = apiService.getMangaRandom(null, new String[] {"safe", "suggestive"});
            mangaCall.enqueue(new Callback<MangaDetailResponse>() {
                @Override
                public void onResponse(Call<MangaDetailResponse> call, Response<MangaDetailResponse> response) {
                    if (response.isSuccessful()) {
                        MangaDetailResponse res = response.body();
                        Intent intent = new Intent(MainActivity.this, MangaInfoActivity.class);
                        intent.putExtra("mangaId", res.data.id);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<MangaDetailResponse> call, Throwable t) {

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateNavBar();
        fragmentManager.beginTransaction()
                .hide(fragmentManager.findFragmentByTag("5"))
                .hide(fragmentManager.findFragmentByTag("4"))
                .hide(fragmentManager.findFragmentByTag("3"))
                .hide(fragmentManager.findFragmentByTag("2"))
                .show(fragmentManager.findFragmentByTag("1"))
                .commit();
        active = fragmentManager.findFragmentByTag("1");
        screen = Screen.Library;
        bottomNavigationView.setSelectedItemId(R.id.libraryItem);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavBar();
    }

    private void updateNavBar(){
        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);

        if(currFragment instanceof LibraryFragment){
            bottomNavigationView.setSelectedItemId(R.id.libraryItem);
        }
        else if(currFragment instanceof BrowseFragment){
            bottomNavigationView.setSelectedItemId(R.id.browseItem);
        }
        else if(currFragment instanceof SearchFragment){
            bottomNavigationView.setSelectedItemId(R.id.searchItem);
        }
        else if(currFragment instanceof MoreFragment){
            bottomNavigationView.setSelectedItemId(R.id.moreItem);
        }
    }

//    private void filter(String text) {
//        ArrayList<Manga> filteredManga = new ArrayList<>();
//
//        for (Manga item : manga) {
//            if (item.attributes.title.toString().toLowerCase().contains(text.toLowerCase())) {
//                filteredManga.add(item);
//            }
//        }
//        if (filteredManga.isEmpty()) {
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            searchAdapter.filterList(filteredManga);
//        }
//    }
}