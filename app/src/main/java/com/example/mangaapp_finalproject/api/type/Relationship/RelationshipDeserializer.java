package com.example.mangaapp_finalproject.api.type.Relationship;

import com.example.mangaapp_finalproject.api.type.AuthorArtistInfo.AuthorArtistResponse;
import com.example.mangaapp_finalproject.api.type.CoverArt.CoverArt;
import com.example.mangaapp_finalproject.api.type.ScanlationGroup.ScanlationGroup;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Objects;

public class RelationshipDeserializer implements JsonDeserializer<RelationshipAttribute> {

    @Override
    public RelationshipAttribute deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("state").getAsString();

        if(Objects.equals(type, "cover_art"))
            return new CoverArt();
        else if (Objects.equals(type, "artist"))
            return new AuthorArtistResponse();
        else if (Objects.equals(type, "author"))
            return new AuthorArtistResponse();
        else if (Objects.equals(type, "scanlation_group"))
            return new ScanlationGroup();

        return null;
    }
}