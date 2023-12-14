package com.example.mangaapp_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Statistic.Statistic;
import com.example.mangaapp_finalproject.api.type.Statistic.StatisticResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL="https://api.mangadex.org/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callApi();
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