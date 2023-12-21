package com.example.mangaapp_finalproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.detail.MangaInfoActivity;
//import com.example.mangaapp_finalproject.placeholder.MangaListContent.PlaceholderItem;
//import com.example.mangaapp_finalproject.databinding.FragmentHistoryBinding;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Manga> manga;

    public SearchRecyclerViewAdapter(Context context, ArrayList<Manga> manga) {
        this.context = context;
        this.manga = manga;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout(Giving a look to our rows)

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_item_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ivMangaItem.setImageResource(R.drawable.placeholder_manga);
        holder.tvMangaItemTitle.setText(R.string.Manga_title);

        holder.ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete manga
                Toast.makeText(context, "Delete a manga", Toast.LENGTH_SHORT).show();
            }
        });

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
//        return manga.size();
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMangaItem;
        TextView tvMangaItemTitle;
        ImageButton ibtnDelete;

        public ViewHolder(View binding) {
            super(binding);
            ivMangaItem = binding.findViewById(R.id.ivMangaItem);
            tvMangaItemTitle = binding.findViewById(R.id.tvMangaItemTitle);
            ibtnDelete = binding.findViewById(R.id.ibtnDelete);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}