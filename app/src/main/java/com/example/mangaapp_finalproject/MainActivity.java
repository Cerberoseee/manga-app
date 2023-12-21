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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Statistic.StatisticResponse;
import com.example.mangaapp_finalproject.browse.BrowseFragment;
import com.example.mangaapp_finalproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL="https://api.mangadex.org/";
    SwipeRefreshLayout swipeLayout;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    androidx.appcompat.widget.Toolbar toolbarMain;

    SharedPreferences darkModeSharePref;
    int darkMode;

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

        changeFragment(new LibraryFragment());

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

            } else if (item.getItemId() == R.id.historyItem) {
                changeFragment(new HistoryFragment());
                toolbarMain.setTitle("History");

            } else if (item.getItemId() == R.id.moreItem) {
                changeFragment(new MoreFragment());
            }

            return true;
        });


        callApi();
    }

    private void changeFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                )
                .replace(R.id.flMain, fragment);

        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintoolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.actionSearch);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for manga...");

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, "Search for manga" + s, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID =item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    void callApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<StatisticResponse> call = apiService.getStatistic("43848429-00b1-4215-bb0c-31973fe705cf");

        call.enqueue(new Callback<StatisticResponse>() {
            @Override
            public void onResponse(Call<StatisticResponse> call, Response<StatisticResponse> response) {
                if (response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                StatisticResponse stat = response.body();
                String statDetail = stat.statistics.get("43848429-00b1-4215-bb0c-31973fe705cf").follows.toString();
                Log.i("res", statDetail);
            }

            @Override
            public void onFailure(Call<StatisticResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Unable to connect to network", Toast.LENGTH_SHORT).show();
                Log.i("err", t.toString());

            }
        });
    }
}