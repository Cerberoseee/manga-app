package com.example.mangaapp_finalproject.browse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaapp_finalproject.R;
import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.detail.MangaInfoActivity;

import java.util.ArrayList;

public class HottestRecyclerViewAdapter extends RecyclerView.Adapter<HottestRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Manga> manga;

    public HottestRecyclerViewAdapter(Context context, ArrayList<Manga> manga) {
        this.context = context;
        this.manga = manga;
    }

    @NonNull
    @Override
    public HottestRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_item_row, parent, false);
        return new HottestRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HottestRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.ivMangaItem.setImageResource(R.drawable.placeholder_manga);
        holder.tvMangaItemTitle.setText(R.string.Manga_title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), MangaInfoActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMangaItem;
        TextView tvMangaItemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMangaItem = itemView.findViewById(R.id.ivManga);
            tvMangaItemTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
