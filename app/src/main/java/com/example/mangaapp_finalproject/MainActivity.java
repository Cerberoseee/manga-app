package com.example.mangaapp_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.browse.BrowseFragment;
import com.example.mangaapp_finalproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeLayout;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbarMain;
    SharedPreferences darkModeSharePref;
    int darkMode;

    Manga[] manga;
    SearchRecyclerViewAdapter searchAdapter;

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

        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);

        if(currFragment == null){
            changeFragment(new LibraryFragment());
        }


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "refreshing...", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);

                //refresh stuff here

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeLayout.setRefreshing(false);
//                    }
//                }, 2000);
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

            if(item.getItemId() == R.id.libraryItem){
                item.setIcon(R.drawable.ic_library_nav_selected);
                changeFragment(new LibraryFragment());
                toolbarMain.setTitle("Library");

            } else if (item.getItemId() == R.id.browseItem) {
                item.setIcon(R.drawable.ic_browse_nav_selected);
                changeFragment(new BrowseFragment());
                toolbarMain.setTitle("Browse");

            } else if (item.getItemId() == R.id.searchItem) {
                changeFragment(new SearchFragment());
                toolbarMain.setTitle("Search");

            } else if (item.getItemId() == R.id.moreItem) {
                changeFragment(new MoreFragment());
            }

            return true;
        });
    }

    private void changeFragment(Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);

        if(!fragmentPopped){
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                    )
                    .replace(R.id.flMain, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(backStateName);

            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintoolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionSearch);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for manga...");

        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, "Search for manga" + s, Toast.LENGTH_SHORT).show();

                if(!(currFragment instanceof SearchFragment)){
                    changeFragment(new SearchFragment());
                    bottomNavigationView.setSelectedItemId(R.id.searchItem);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!(currFragment instanceof SearchFragment)){
                    changeFragment(new SearchFragment());
                    bottomNavigationView.setSelectedItemId(R.id.searchItem);
                }

                return false;
            }
        });

        return true;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
//
//        if(currFragment instanceof LibraryFragment){
//            bottomNavigationView.setSelectedItemId(R.id.libraryItem);
//        }
//        if(currFragment instanceof BrowseFragment){
//            bottomNavigationView.setSelectedItemId(R.id.browseItem);
//        }
//        if(currFragment instanceof SearchFragment){
//            bottomNavigationView.setSelectedItemId(R.id.searchItem);
//        }
//        if(currFragment instanceof MoreFragment){
//            bottomNavigationView.setSelectedItemId(R.id.moreItem);
//        }
//
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
//
//        if(currFragment instanceof LibraryFragment){
//            bottomNavigationView.setSelectedItemId(R.id.libraryItem);
//        }
//        else if(currFragment instanceof BrowseFragment){
//            bottomNavigationView.setSelectedItemId(R.id.browseItem);
//        }
//        else if(currFragment instanceof SearchFragment){
//            bottomNavigationView.setSelectedItemId(R.id.searchItem);
//        }
//        else if(currFragment instanceof MoreFragment){
//            bottomNavigationView.setSelectedItemId(R.id.moreItem);
//        }
//    }

    private void filter(String text) {
        ArrayList<Manga> filteredManga = new ArrayList<>();

        for (Manga item : manga) {
            if (item.attributes.title.toString().toLowerCase().contains(text.toLowerCase())) {
                filteredManga.add(item);
            }
        }
        if (filteredManga.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            searchAdapter.filterList(filteredManga);
        }
    }
}