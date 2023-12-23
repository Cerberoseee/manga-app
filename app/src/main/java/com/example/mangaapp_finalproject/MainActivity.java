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


    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment currFragment;
    Fragment active;


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

        if(currFragment == null){
            addAllFragment(libraryFragment, browseFragment, searchFragment, moreFragment);
            active = fragmentManager.findFragmentByTag("1");
//            showFragment(libraryFragment);
//            changeFragment(new LibraryFragment());
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

//            hideAllFragment(libraryFragment, browseFragment, searchFragment, moreFragment);

            if(item.getItemId() == R.id.libraryItem){
                item.setIcon(R.drawable.ic_library_nav_selected);
                changeFragment(fragmentManager.findFragmentByTag("1"), active);
                active = fragmentManager.findFragmentByTag("1");
                toolbarMain.setTitle("Library");

            }
            else if (item.getItemId() == R.id.browseItem) {
                item.setIcon(R.drawable.ic_browse_nav_selected);
                changeFragment(fragmentManager.findFragmentByTag("2"), active);
                active = fragmentManager.findFragmentByTag("2");
                toolbarMain.setTitle("Browse");

            } else if (item.getItemId() == R.id.searchItem) {
                changeFragment(fragmentManager.findFragmentByTag("3"), active);
                active = fragmentManager.findFragmentByTag("3");
                toolbarMain.setTitle("Search");

            } else if (item.getItemId() == R.id.moreItem) {
                changeFragment(fragmentManager.findFragmentByTag("4"), active);
                active = fragmentManager.findFragmentByTag("4");
                toolbarMain.setTitle("More");
            }

            return true;
        });
    }

    private void changeFragment(Fragment fragment, Fragment active){
        if(fragment.isHidden()) {
            fragmentManager.beginTransaction()
                    .hide(active)
                    .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                    )
                    .show(fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void addAllFragment(Fragment fragment1, Fragment fragment2, Fragment fragment3, Fragment fragment4){

        fragmentManager.beginTransaction()
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

//        MenuItem menuItem = menu.findItem(R.id.actionSearch);
//        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Search for manga...");
//
//        Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.flMain);
//
//        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Toast.makeText(MainActivity.this, "Search for manga" + s, Toast.LENGTH_SHORT).show();
//
//                if(!(currFragment instanceof SearchFragment)){
//                    changeFragment(new SearchFragment());
//                    bottomNavigationView.setSelectedItemId(R.id.searchItem);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                if(!(currFragment instanceof SearchFragment)){
//                    changeFragment(new SearchFragment());
//                    bottomNavigationView.setSelectedItemId(R.id.searchItem);
//                }
//
//                return false;
//            }
//        });

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateNavBar();
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