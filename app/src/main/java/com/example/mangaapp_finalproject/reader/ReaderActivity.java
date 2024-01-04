package com.example.mangaapp_finalproject.reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.DirectionalViewPager;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Chapter.ChapterDetailResponse;
import com.example.mangaapp_finalproject.api.type.Chapter.ChapterImageResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
import com.example.mangaapp_finalproject.detail.MangaInfoActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReaderActivity extends AppCompatActivity {
    DirectionalViewPager reader;
    ReaderAdapter readerAdapter;
    androidx.appcompat.widget.Toolbar toolbarReader;
    LinearLayout bottomMenu;
    Button btnDirect, btnOrientate, btnMore, btnNextChap, btnNext, btnPrev, btnPrevChap;
    ImageButton ibtnDirect, ibtnRotate, ibtnNext, ibtnPrev;
    TextView textPageNumber, textManga, textChapter;
    boolean isShowMenu = true;
    int totalPage;
    String[] chapterList;
    String id, mangaId, prevChapId = null, nextChapId = null, mangaName, chapterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        toolbarReader = findViewById(R.id.toolbarReader);
        setSupportActionBar(toolbarReader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() == null) {
            id = "0a54fd99-2b8d-4b9a-bf2f-9b2c5b3a3ac4";
            mangaId = "5b93fa0f-0640-49b8-974e-954b9959929b";
            chapterList = new String[]{"0a54fd99-2b8d-4b9a-bf2f-9b2c5b3a3ac4"};
            mangaName = "Bocchi the Rock";
        } else {
            id = Objects.requireNonNull(getIntent().getExtras().get("id")).toString();
            mangaId = Objects.requireNonNull(getIntent().getExtras().get("mangaId")).toString();
            chapterList = getIntent().getStringArrayExtra("chapterList");
            mangaName = getIntent().getExtras().getString("mangaName");
        }

        reader = findViewById(R.id.reader);

        toolbarReader = findViewById(R.id.toolbarReader);
        bottomMenu = findViewById(R.id.menu_down);

//        btnDirect = findViewById(R.id.direct_btn);
//        btnOrientate = findViewById(R.id.rotate_btn);
//        btnMore = findViewById(R.id.more_btn);
        btnNextChap = findViewById(R.id.next_chap_btn);
        btnPrevChap = findViewById(R.id.prev_chap_btn);
//        btnNext = findViewById(R.id.next_btn);
//        btnPrev = findViewById(R.id.prev_btn);

        ibtnDirect = findViewById(R.id.ibtnDirect);
        ibtnRotate = findViewById(R.id.ibtnRotate);
        ibtnNext = findViewById(R.id.ibtnNext);
        ibtnPrev = findViewById(R.id.ibtnPrev);

        textPageNumber = findViewById(R.id.text_page);
//        textManga = findViewById(R.id.manga_name);
//        textChapter = findViewById(R.id.chapter_name);

        toolbarReader.setTitle(mangaName);
        toolbarReader.setSubtitle(chapterName);

        btnNextChap.setVisibility(View.GONE);

        SharedPreferences prefs = ReaderActivity.this.getSharedPreferences("chapter", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("savedChapter" + mangaId, id);
        edit.commit();

        int pos = Arrays.asList(chapterList).indexOf(id);
        if (pos > 0) {
            prevChapId = chapterList[pos-1];
        } else {
            btnPrevChap.setVisibility(View.GONE);
        }
        if (pos < chapterList.length - 1) {
            nextChapId = chapterList[pos+1];
        } else {
            btnNextChap.setVisibility(View.GONE);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ChapterImageResponse> imageCall = apiService.getChapterImageUrl(id);
        Call<ChapterDetailResponse> infoCall = apiService.getChapter(id, null);

        imageCall.enqueue(new Callback<ChapterImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChapterImageResponse> call, @NonNull Response<ChapterImageResponse> response) {
                if (response.isSuccessful()) {
                    ChapterImageResponse res = response.body();
                    readerAdapter = new ReaderAdapter(getSupportFragmentManager(), res, ReaderActivity.this);
                    reader.setAdapter(readerAdapter);
                    totalPage = res.chapter.data.length;
                    textPageNumber.setText("01/" + String.format("%02d", Integer.valueOf(totalPage)));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChapterImageResponse> call, Throwable t) {
                Toast.makeText(ReaderActivity.this, "Unable to fetch image", Toast.LENGTH_SHORT).show();
            }
        });
        infoCall.enqueue(new Callback<ChapterDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChapterDetailResponse> call, @NonNull Response<ChapterDetailResponse> response) {
                if (response.isSuccessful()) {
                    ChapterDetailResponse res = response.body();
                    toolbarReader.setSubtitle("Chapter " + res.data.attributes.chapter + " - " + res.data.attributes.title);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChapterDetailResponse> call, Throwable t) {
                Toast.makeText(ReaderActivity.this, "Unable to fetch image", Toast.LENGTH_SHORT).show();
            }
        });
        ibtnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pm = new PopupMenu(ReaderActivity.this, view);
                pm.getMenuInflater().inflate(R.menu.reader_direct_menu, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.verticalItem) {
                            reader.setHorizontal(false);
                            Toast.makeText(ReaderActivity.this, "Direction changed!", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId() == R.id.horizonItem) {
                            reader.setHorizontal(true);
                            Toast.makeText(ReaderActivity.this, "Direction changed!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                pm.show();
            }
        });
        ibtnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pm = new PopupMenu(ReaderActivity.this, view);
                pm.getMenuInflater().inflate(R.menu.reader_orientation_menu, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.landscapeItem) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            Toast.makeText(ReaderActivity.this, "Orientation changed!", Toast.LENGTH_SHORT).show();
                        }
                        if (menuItem.getItemId() == R.id.portraitItem) {
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            Toast.makeText(ReaderActivity.this, "Orientation changed!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                pm.show();
            }
        });
//        btnMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu pm = new PopupMenu(ReaderActivity.this, view);
//                pm.getMenuInflater().inflate(R.menu.reader_more_menu, pm.getMenu());
//                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        if (menuItem.getItemId() == R.id.saveItem) {
//                            saveImage();
//                        }
//                        if (menuItem.getItemId() == R.id.webItem) {
//                            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://mangadex.org/title/" + mangaId));
//                            startActivity(viewIntent);
//                        }
//                        return true;
//                    }
//                });
//                pm.show();
//            }
//        });

        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = reader.getCurrentItem();
                if (page < totalPage) {
                    reader.setCurrentItem(page + 1);
                }
            }
        });
        ibtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = reader.getCurrentItem();
                if (page > 0) {
                    reader.setCurrentItem(page - 1);
                }
            }
        });
        btnNextChap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReaderActivity.this, ReaderActivity.class);
                intent.putExtra("id", nextChapId);
                intent.putExtra("chapterList", chapterList);
                intent.putExtra("mangaId", mangaId);
                intent.putExtra("mangaName", mangaName);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ReaderActivity.this.finish();
            }
        });
        btnPrevChap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReaderActivity.this, ReaderActivity.class);
                intent.putExtra("id", prevChapId);
                intent.putExtra("chapterList", chapterList);
                intent.putExtra("mangaId", mangaId);
                intent.putExtra("mangaName", mangaName);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ReaderActivity.this.finish();
            }
        });

        reader.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (nextChapId != null) {
                    if (position == totalPage - 1) {
                        showNextBtn();
                    } else if (position == totalPage - 2) {
                        hideNextBtn();
                    }
                }

                if (prevChapId != null) {
                    if (position == 0) {
                        showPrevBtn();
                    } else if (position == 1) {
                        hidePrevBtn();
                    }
                }

                textPageNumber.setText(String.format("%02d", Integer.valueOf(position + 1)) + "/" + String.format("%02d", Integer.valueOf(totalPage)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reader_more_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
            if (itemID == R.id.saveItem) {
                saveImage();
            } else if (itemID == R.id.webItem) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://mangadex.org/title/" + mangaId));
                startActivity(viewIntent);
            } else if (itemID == android.R.id.home) {
                finish();
            }

        return super.onOptionsItemSelected(item);
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
        toolbarReader.setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.VISIBLE);

        toolbarReader.animate()
            .translationY(0)
            .setDuration(500)
            .setListener(null);
        bottomMenu.animate()
            .translationY(0)
            .setDuration(500)
            .setListener(null);
    }

    private void menuOffAnimation() {
        toolbarReader.animate()
            .translationY(-500)
            .setDuration(500)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    toolbarReader.setVisibility(View.GONE);
                }
            });
        bottomMenu.animate()
            .translationY(500)
            .setDuration(500)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    bottomMenu.setVisibility(View.GONE);
                }
            });
    }

    private void showNextBtn() {
        btnNextChap.setVisibility(View.VISIBLE);
        btnNextChap.animate()
            .translationX(0)
            .setDuration(200)
            .setListener(null);
    }

    private void hideNextBtn() {
        btnNextChap.animate()
            .translationX(500)
            .setDuration(200)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    btnNextChap.setVisibility(View.GONE);
                }
            });
    }

    private void showPrevBtn() {
        btnPrevChap.setVisibility(View.VISIBLE);
        btnPrevChap.animate()
            .translationX(0)
            .setDuration(200)
            .setListener(null);
    }

    private void hidePrevBtn() {
        btnPrevChap.animate()
            .translationX(-200)
            .setDuration(200)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(@NonNull Animator animator) {
                    btnPrevChap.setVisibility(View.GONE);
                }
            });
    }

    private void saveImage() {
        BitmapDrawable draw = (BitmapDrawable) ((PhotoView)reader.getChildAt(1).findViewById(R.id.photo_view)).getDrawable();
        Bitmap bitmap = draw.getBitmap();

        MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, Long.valueOf(System.currentTimeMillis()).toString() ,"This is an image saved from manga app");
        Toast.makeText(this, "Image saved!", Toast.LENGTH_SHORT).show();
    }
}