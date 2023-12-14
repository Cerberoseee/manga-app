package com.example.mangaapp_finalproject.api.type.Manga;

import com.example.mangaapp_finalproject.api.type.LanguageCollection;
import com.example.mangaapp_finalproject.api.type.Tag.Tag;

class MangaAttributes {
    public String title;
    public LanguageCollection alTitles;

    public LanguageCollection description;
    public boolean isLocked;
    public String originalLanguage;
    public String status;
    public Integer year;
    public Tag tags[];
    public String createdAt;
    public String updatedAt;
}
