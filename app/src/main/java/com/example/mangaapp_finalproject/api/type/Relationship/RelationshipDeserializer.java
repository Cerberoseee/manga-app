package com.example.mangaapp_finalproject.api.type.Relationship;

import android.util.Log;

import com.example.mangaapp_finalproject.R;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Objects;

public class RelationshipDeserializer implements JsonDeserializer<Relationship> {

    @Override
    public Relationship deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        Relationship relationship = new Relationship();
        JsonObject obj = json.getAsJsonObject();

        relationship.id = obj.get("id").getAsString();

        String type = obj.get("type").getAsString();
        relationship.type = type;

        if(Objects.equals(type, "cover_art"))
            relationship.attribute = new CoverArt(obj.get("attributes").getAsJsonObject().get("fileName").getAsString());

        else if (Objects.equals(type, "artist"))
            relationship.attribute = new AuthorArtist(obj.get("attributes").getAsJsonObject().get("name").getAsString());

        else if (Objects.equals(type, "author"))
            relationship.attribute = new AuthorArtist(obj.get("attributes").getAsJsonObject().get("name").getAsString());

        else if (Objects.equals(type, "scanlation_group"))
            relationship.attribute = new ScanlationGroup(obj.get("attributes").getAsJsonObject().get("name").getAsString());

        return relationship;
    }
}