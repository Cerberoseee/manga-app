package com.example.mangaapp_finalproject.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Chapter.Chapter;
import com.example.mangaapp_finalproject.api.type.Chapter.ChapterResponse;
import com.example.mangaapp_finalproject.api.type.Manga.MangaDetailResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.AuthorArtist;
import com.example.mangaapp_finalproject.api.type.Relationship.CoverArt;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
import com.example.mangaapp_finalproject.api.type.Relationship.ScanlationGroup;
import com.example.mangaapp_finalproject.api.type.Statistic.StatisticResponse;
import com.example.mangaapp_finalproject.reader.ReaderActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MangaInfoActivity extends AppCompatActivity {

    Chapter[] data;
    ArrayAdapter adapter;
    ListView listView;
    androidx.appcompat.widget.Toolbar toolbarMangaInfo;
    TextView tvMangaTitle, tvMangaAuthor, tvMangaArtist, tvMangaStatus, tvMangaRating, tvChapterList;
    ImageView ivMangaView;
    String mangaId = "5b93fa0f-0640-49b8-974e-954b9959929b", mangaName = "";
    Button btnMangaWebview, btnLangFilter, btnMangaAdd;
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);
        resources = getResources();

        tvMangaTitle = findViewById(R.id.tvMangaTitle);
        tvMangaAuthor = findViewById(R.id.tvMangaAuthor);
        tvMangaArtist = findViewById(R.id.tvMangaArtist);
        tvMangaRating = findViewById(R.id.tvMangaRating);
        tvChapterList = findViewById(R.id.tvChapterList);
        tvMangaStatus = findViewById(R.id.tvMangaStatus);

        btnMangaWebview = findViewById(R.id.btnMangaWebview);
        btnLangFilter = findViewById(R.id.btnLangFilter);
        btnMangaAdd = findViewById(R.id.btnMangaAdd);

        ivMangaView = findViewById(R.id.ivMangaView);

        toolbarMangaInfo = findViewById(R.id.toolbarMangaInfo);
        setSupportActionBar(toolbarMangaInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // sample code snippet to set the text content on the ExpandableTextView
        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);

        listView = findViewById(R.id.listMangaChapter);
        listView.setClickable(true);
        listView.setNestedScrollingEnabled(true);

        if (getIntent().getStringExtra("mangaId") != null) {
            mangaId = getIntent().getStringExtra("mangaId");
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Relationship.class, new RelationshipDeserializer());
        Gson gson = gsonBuilder.create();

        SharedPreferences prefs= MangaInfoActivity.this.getSharedPreferences("library",Context.MODE_PRIVATE);

        Set<String> set = prefs.getStringSet("library", null);
        List<String> libraryList = new ArrayList<>();
        if (set != null) {
            libraryList = new ArrayList<String>(set);
        }

        if (libraryList.contains(mangaId)) {
            btnMangaAdd.setText(R.string.Manga_save_btn_selected);
            btnMangaAdd.setTextColor(getColor(R.color.main_blue));
            btnMangaAdd.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_book_saved_selected), null, null, null);
            btnMangaAdd.setBackground(AppCompatResources.getDrawable(this, R.drawable.button_primary_selected));
        } else {
            btnMangaAdd.setText(R.string.Manga_save_btn);
            btnMangaAdd.setTextColor(getColor(R.color.white));
            btnMangaAdd.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(this, R.drawable.ic_book_saved), null, null, null);
            btnMangaAdd.setBackground(AppCompatResources.getDrawable(this, R.drawable.button_primary_shape));
        }

        Retrofit retrofitRelate = new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiRelateService = retrofitRelate.create(ApiService.class);
        ApiService apiService = retrofit.create(ApiService.class);

        Call<MangaDetailResponse> mangaApiCall = apiRelateService.getMangaDetail(mangaId, new String[]{"author", "artist", "cover_art"});
        Call<StatisticResponse> ratingCall = apiService.getStatistic(mangaId);
        mangaApiCall.enqueue(new Callback<MangaDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MangaDetailResponse> call,@NonNull Response<MangaDetailResponse> response) {
                if (response.isSuccessful()) {
                    MangaDetailResponse res = response.body();
                    mangaName = res.data.attributes.title.en;
                    tvMangaTitle.setText(mangaName);
                    expTv1.setText(res.data.attributes.description.en);
                    tvMangaStatus.setText("Status: " + res.data.attributes.status.substring(0, 1).toUpperCase() + res.data.attributes.status.substring(1));
                    Relationship[] relationship = res.data.relationships;
                    for (int i = 0; i < relationship.length; i++) {
                        switch (relationship[i].type) {
                            case "author":
                                tvMangaAuthor.setText(resources.getString(R.string.Manga_author, ((AuthorArtist)relationship[i].attribute).name));
                                break;
                            case "artist":
                                tvMangaArtist.setText(resources.getString(R.string.Manga_artist, ((AuthorArtist)relationship[i].attribute).name));
                                break;
                            case "cover_art":
                                Picasso.get().load("https://uploads.mangadex.org/covers/" + mangaId + "/" + ((CoverArt)relationship[i].attribute).fileName).into(ivMangaView);
                                break;
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<MangaDetailResponse> call, Throwable t) {
                Log.e("err", t.toString());
                Toast.makeText(MangaInfoActivity.this, "Unable to get manga info", Toast.LENGTH_SHORT).show();
            }
        });
        getChapter(apiRelateService, new String[]{"en"});
        ratingCall.enqueue(new Callback<StatisticResponse>() {
            @Override
            public void onResponse(Call<StatisticResponse> call, Response<StatisticResponse> response) {
                if (response.isSuccessful()) {
                    StatisticResponse res = response.body();
                    tvMangaRating.setText(String.format("%.2f", res.statistics.get(mangaId).rating.average));
                }
            }

            @Override
            public void onFailure(Call<StatisticResponse> call, Throwable t) {
                Log.e("err", t.toString());
                Toast.makeText(MangaInfoActivity.this, "Unable to get manga rating", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MangaInfoActivity.this, ReaderActivity.class);
                ArrayList<String> chapterList = new ArrayList<String>();
                for (int j = 0; j < data.length; j++) {
                    chapterList.add(data[j].id);
                }

                intent.putExtra("id", data[i].id);
                intent.putExtra("mangaId", mangaId);
                intent.putExtra("chapterList", chapterList.toArray(new String[0]));
                intent.putExtra("mangaName", mangaName);

                startActivity(intent);
            }
        });

        btnMangaWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://mangadex.org/title/" + mangaId));
                startActivity(viewIntent);
            }
        });

        btnLangFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MangaInfoActivity.this);

                String[] items = new String[]{"English", "Vietnamese", "Japanese"};
                builder.setTitle("Filtered by Language");

                builder.setSingleChoiceItems(items,  -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0) {
                            getChapter(apiRelateService, new String[]{"en"});
                        } else if (i == 1) {
                            getChapter(apiRelateService, new String[]{"vi"});
                        } else if (i == 2) {
                            getChapter(apiRelateService, new String[]{"ja", "ja-ro"});
                        }
                    }
                });

                builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                builder.create();

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnMangaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnMangaAdd.getText().equals("ADD TO LIBRARY")) {
                    SharedPreferences prefs= MangaInfoActivity.this.getSharedPreferences("library",Context.MODE_PRIVATE);

                    Set<String> set = prefs.getStringSet("library", null);
                    List<String> libraryList = new ArrayList<>();
                    if (set != null) {
                        libraryList = new ArrayList<String>(set);
                    }

                    SharedPreferences.Editor edit = prefs.edit();
                    libraryList.add(mangaId);

                    set = new HashSet<String>();
                    set.addAll(libraryList);
                    edit.putStringSet("library", set);
                    edit.commit();
                    Toast.makeText(MangaInfoActivity.this, "Manga added to library!", Toast.LENGTH_SHORT).show();

                    btnMangaAdd.setText(R.string.Manga_save_btn_selected);
                    btnMangaAdd.setTextColor(getColor(R.color.main_blue));
                    btnMangaAdd.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(MangaInfoActivity.this, R.drawable.ic_book_saved_selected), null, null, null);
                    btnMangaAdd.setBackground(AppCompatResources.getDrawable(MangaInfoActivity.this, R.drawable.button_primary_selected));

                } else {
                    SharedPreferences prefs= MangaInfoActivity.this.getSharedPreferences("library",Context.MODE_PRIVATE);

                    Set<String> set = prefs.getStringSet("library", null);
                    List<String> libraryList = new ArrayList<>();
                    if (set != null) {
                        libraryList = new ArrayList<String>(set);
                    }

                    SharedPreferences.Editor edit = prefs.edit();
                    libraryList.remove(mangaId);

                    set = new HashSet<String>();
                    set.addAll(libraryList);
                    edit.putStringSet("library", set);
                    edit.commit();
                    Toast.makeText(MangaInfoActivity.this, "Manga removed from library!", Toast.LENGTH_SHORT).show();

                    btnMangaAdd.setText(R.string.Manga_save_btn);
                    btnMangaAdd.setTextColor(getColor(R.color.white));
                    btnMangaAdd.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources.getDrawable(MangaInfoActivity.this, R.drawable.ic_book_saved), null, null, null);
                    btnMangaAdd.setBackground(AppCompatResources.getDrawable(MangaInfoActivity.this, R.drawable.button_primary_shape));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mangainfo_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.shareManga) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://mangadex.org/title/" + mangaId);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        } else if (itemID == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayAdapter getChapterAdapter() {
        return new ArrayAdapter(MangaInfoActivity.this,
                R.layout.chapter_list_item, R.id.textName, data) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View item = super.getView(position, convertView, parent);
                ((TextView) item.findViewById(R.id.textName)).setText("Chapter " + data[position].attributes.chapter + " - " + data[position].attributes.title);

                String scanlationGroupName = "";
                for (int i = 0; i < data[position].relationships.length; i++) {
                    if (data[position].relationships[i].type.equals("scanlation_group")) {
                        scanlationGroupName = ((ScanlationGroup) data[position].relationships[i].attribute).name;
                    }
                }
                ((TextView) item.findViewById(R.id.textScanlation)).setText(scanlationGroupName);

                return item;
            }
        };
    }

    private void getChapter(ApiService apiService, String[] language) {
        Call<ChapterResponse> chapterCall = apiService.getMangaChapters(mangaId, language, new String[]{"scanlation_group"}, 500, 0, "asc");

        chapterCall.enqueue(new Callback<ChapterResponse>() {
            @Override
            public void onResponse(Call<ChapterResponse> call, Response<ChapterResponse> response) {
                Log.i("call", response.toString());
                if (response.isSuccessful()) {
                    ChapterResponse res = response.body();
                    data = res.data;
                    tvChapterList.setText(data.length + " Chapters");
                    adapter = getChapterAdapter();
                    listView.setAdapter(adapter);
                    listView.invalidateViews();
                } else {
                    Toast.makeText(MangaInfoActivity.this, "Unable to get manga chapters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChapterResponse> call, Throwable t) {
                Log.e("err", t.toString());
                Toast.makeText(MangaInfoActivity.this, "Unable to get manga chapters", Toast.LENGTH_SHORT).show();
            }
        });
    }
}