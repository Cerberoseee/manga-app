package com.example.mangaapp_finalproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.api.type.Relationship.AuthorArtist;
import com.example.mangaapp_finalproject.api.type.Relationship.CoverArt;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.detail.MangaInfoActivity;
import com.squareup.picasso.Picasso;
//import com.example.mangaapp_finalproject.placeholder.MangaListContent.PlaceholderItem;
//import com.example.mangaapp_finalproject.databinding.FragmentHistoryBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link RecyclerView.Adapter} that can display a {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private Manga[] manga;

    public SearchRecyclerViewAdapter(Context context, Manga[] manga) {
        this.context = context;
        this.manga = manga;
    }

//    public void filterList(ArrayList<Manga> filterlist) {
//        manga = filterlist;
//
//        notifyDataSetChanged();
//    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout(Giving a look to our rows)

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_item_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String coverLink = "";
        String artist = "";
        String author = "";

        Relationship[] relationships = manga[position].relationships;
        for (int i = 0; i < relationships.length; i++) {
            if (relationships[i].type.equals("cover_art")) {
                coverLink = "https://uploads.mangadex.org/covers/" + manga[position].id + "/" + ((CoverArt)relationships[i].attribute).fileName;
            }
            if (relationships[i].type.equals("artist")) {
                artist = "Artist: " +  ((AuthorArtist)relationships[i].attribute).name;
            }
            if (relationships[i].type.equals("author")) {
                author = "Author: " + ((AuthorArtist)relationships[i].attribute).name;
            }
        }

        if (coverLink != "") {
            Picasso.get().load(coverLink).into(holder.ivMangaItem);
        } else {
            holder.ivMangaItem.setImageResource(R.drawable.solid_grey_svg);
        }
        if (manga[position].attributes.title.en != null)
            holder.tvMangaItemTitle.setText(manga[position].attributes.title.en);
        else if (manga[position].attributes.title.ja != null) {
            holder.tvMangaItemTitle.setText(manga[position].attributes.title.ja);
        } else {
            holder.tvMangaItemTitle.setText(manga[position].attributes.title.ja_ro);
        }
        holder.tvMangaItemAuthor.setText(author);
        holder.tvMangaItemArtist.setText(artist);

        holder.ibtnDelete.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (view.getContext(), MangaInfoActivity.class);
                intent.putExtra("mangaId", manga[position].id);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return manga.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMangaItem;
        TextView tvMangaItemTitle, tvMangaItemArtist, tvMangaItemAuthor;
        ImageButton ibtnDelete;

        public ViewHolder(View binding) {
            super(binding);
            ivMangaItem = binding.findViewById(R.id.ivMangaItem);
            tvMangaItemTitle = binding.findViewById(R.id.tvMangaItemTitle);
            ibtnDelete = binding.findViewById(R.id.ibtnDelete);
            tvMangaItemArtist = binding.findViewById(R.id.tvMangaItemArtist);
            tvMangaItemAuthor = binding.findViewById(R.id.tvMangaItemAuthor);

        }
    }
}