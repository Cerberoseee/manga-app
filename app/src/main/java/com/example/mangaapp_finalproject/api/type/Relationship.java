package com.example.mangaapp_finalproject.api.type;

import com.example.mangaapp_finalproject.api.type.AuthorArtistInfo.AuthorArtistInfo;
import com.example.mangaapp_finalproject.api.type.CoverArt.CoverArt;
import com.example.mangaapp_finalproject.api.type.ScanlationGroup.ScanlationGroup;
import com.google.gson.annotations.SerializedName;

public class Relationship {
    public String id;
    public String type;
    @SerializedName("attributes")
    public CoverArt coverArtInfo;
    @SerializedName("attributes")
    public AuthorArtistInfo authorInfo;
    @SerializedName("attributes")
    public AuthorArtistInfo artistInfo;
    @SerializedName("attributes")
    public ScanlationGroup scanlationGroupInfo;
}
