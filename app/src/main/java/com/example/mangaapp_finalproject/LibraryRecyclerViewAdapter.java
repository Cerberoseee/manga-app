package com.example.mangaapp_finalproject;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaapp_finalproject.api.type.Manga.Manga;
import com.example.mangaapp_finalproject.api.type.Manga.MangaDetailResponse;
import com.example.mangaapp_finalproject.api.type.Relationship.AuthorArtist;
import com.example.mangaapp_finalproject.api.type.Relationship.CoverArt;
import com.example.mangaapp_finalproject.api.type.Relationship.Relationship;
import com.example.mangaapp_finalproject.api.type.Relationship.RelationshipDeserializer;
import com.example.mangaapp_finalproject.detail.MangaInfoActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
//import com.example.mangaapp_finalproject.placeholder.MangaListContent.PlaceholderItem;
//import com.example.mangaapp_finalproject.databinding.FragmentLibraryBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;

public class LibraryRecyclerViewAdapter extends RecyclerView.Adapter<LibraryRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final Manga[] manga;
    private LibraryFragment fragment;
    private RecyclerView recyclerView;

    public LibraryRecyclerViewAdapter(Context context, Manga[] manga, LibraryFragment fragment, RecyclerView recyclerView) {
        this.context = context;
        this.manga = manga;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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

        holder.ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs= context.getSharedPreferences("library",Context.MODE_PRIVATE);

                Set<String> set = prefs.getStringSet("library", null);
                List<String> libraryList = new ArrayList<>();
                if (set != null) {
                    libraryList = new ArrayList<String>(set);
                }

                SharedPreferences.Editor edit = prefs.edit();
                libraryList.remove(manga[position].id);

                set = new HashSet<String>();
                set.addAll(libraryList);
                edit.putStringSet("library", set);
                edit.commit();

                Toast.makeText(context, "Manga removed from library!", Toast.LENGTH_SHORT).show();
                fragment.getManga(context, recyclerView);
            }
        });
        
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