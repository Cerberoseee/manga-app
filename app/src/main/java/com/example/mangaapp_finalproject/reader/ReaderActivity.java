package com.example.mangaapp_finalproject.reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.DirectionalViewPager;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.api.ApiService;
import com.example.mangaapp_finalproject.api.type.Chapter.ChapterDetailResponse;
import com.example.mangaapp_finalproject.api.type.Chapter.ChapterImageResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipAttribute;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
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
    LinearLayout menu1;
    LinearLayout menu2;
    Button btnDirect, btnOrientate, btnMore, btnNextChap, btnNext, btnPrev, btnPrevChap;
    TextView textPageNumber, textManga, textChapter;
    boolean isShowMenu = true;
    int totalPage;
    String[] chapterList;
    String id, mangaId, prevChapId = null, nextChapId = null, mangaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        if (getIntent().getExtras() == null) {
            id = "0a54fd99-2b8d-4b9a-bf2f-9b2c5b3a3ac4";
            mangaId = "5b93fa0f-0640-49b8-974e-954b9959929b";
            chapterList = new String[]{"0a54fd99-2b8d-4b9a-bf2f-9b2c5b3a3ac4"};
            mangaName = "Shimeji Simulation";
        } else {
            id = Objects.requireNonNull(getIntent().getExtras().get("id")).toString();
            mangaId = Objects.requireNonNull(getIntent().getExtras().get("mangaId")).toString();
            chapterList = (String[]) getIntent().getExtras().get("chapterList");
            mangaName = getIntent().getExtras().getString("mangaName");
        }

        reader = findViewById(R.id.reader);

        menu1 = findViewById(R.id.menu_up);
        menu2 = findViewById(R.id.menu_down);

        btnDirect = findViewById(R.id.direct_btn);
        btnOrientate = findViewById(R.id.rotate_btn);
        btnMore = findViewById(R.id.more_btn);
        btnNextChap = findViewById(R.id.next_chap_btn);
        btnPrevChap = findViewById(R.id.prev_chap_btn);
        btnNext = findViewById(R.id.next_btn);
        btnPrev = findViewById(R.id.prev_btn);

        textPageNumber = findViewById(R.id.text_page);
        textManga = findViewById(R.id.manga_name);
        textChapter = findViewById(R.id.chapter_name);

        btnNextChap.setVisibility(View.GONE);

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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RelationshipAttribute.class, new RelationshipDeserializer());
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mangadex.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ChapterImageResponse> imageCall = apiService.getChapterImageUrl(id);
        Call<ChapterDetailResponse> infoCall = apiService.getChapter(id, null);

        imageCall.enqueue(new Callback<ChapterImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChapterImageResponse> call, @NonNull Response<ChapterImageResponse> response) {
                if (response.isSuccessful()) {
                    ChapterImageResponse res = response.body();
                    readerAdapter = new ReaderAdapter(getSupportFragmentManager(), res);
                    reader.setAdapter(readerAdapter);
                    totalPage = res.chapter.data.length;
                    textPageNumber.setText("01/" + Integer.valueOf(totalPage).toString());
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
                    textChapter.setText(res.data.attributes.title);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChapterDetailResponse> call, Throwable t) {
                Toast.makeText(ReaderActivity.this, "Unable to fetch image", Toast.LENGTH_SHORT).show();
            }
        });
        btnDirect.setOnClickListener(new View.OnClickListener() {
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
        btnOrientate.setOnClickListener(new View.OnClickListener() {
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
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu pm = new PopupMenu(ReaderActivity.this, view);
                pm.getMenuInflater().inflate(R.menu.reader_more_menu, pm.getMenu());
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.saveItem) {
                            saveImage();
                        }
                        if (menuItem.getItemId() == R.id.webItem) {
                            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://mangadex.org/title/" + mangaId));
                            startActivity(viewIntent);
                        }
                        return true;
                    }
                });
                pm.show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int page = reader.getCurrentItem();
                if (page < totalPage) {
                    reader.setCurrentItem(page + 1);
                }
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
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

                startActivity(intent);
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

                startActivity(intent);
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

                textPageNumber.setText(String.format("%02d", Integer.valueOf(position + 1)) + "/" + Integer.valueOf(totalPage).toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
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