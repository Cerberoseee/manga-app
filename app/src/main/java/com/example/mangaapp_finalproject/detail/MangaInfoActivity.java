package com.example.mangaapp_finalproject.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.reader.ReaderActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

public class MangaInfoActivity extends AppCompatActivity {

    ArrayList<String> data;
    ArrayAdapter<String> adapter;
    ListView listView;
    androidx.appcompat.widget.Toolbar toolbarMangaInfo;
    TextView tvMangaTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_info);

        toolbarMangaInfo = findViewById(R.id.toolbarMangaInfo);
        setSupportActionBar(toolbarMangaInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvMangaTitle = findViewById(R.id.tvMangaTitle);

        // sample code snippet to set the text content on the ExpandableTextView
        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view)
                .findViewById(R.id.expand_text_view);

// IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText(getString(R.string.Manga_desc));

        listView = findViewById(R.id.listMangaChapter);
        listView.setClickable(true);

        data = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            data.add("item" + i);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        Toast toast = Toast.makeText(MangaInfoActivity.this, "", Toast.LENGTH_SHORT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                toast.setText(data.get(i).toString());
//                toast.show();
                String chapterName = adapter.getItem(i).toString();
                String mangaName = tvMangaTitle.toString();

                String[] dataList = new String[data.size()];

                for (int index = 0; index < data.size(); index++) {
                    dataList[index] = data.get(i);
                }

                Intent intent = new Intent(MangaInfoActivity.this, ReaderActivity.class);

//                intent.putExtra("id", "id");
//                intent.putExtra("mangaId", "mangaId");
//                intent.putExtra("chapterList", dataList);
//                intent.putExtra("mangaName", mangaName);
//                intent.putExtra("chapterName", chapterName);

                startActivity(intent);
            }
        });
    }
}