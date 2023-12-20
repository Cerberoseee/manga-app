package com.example.mangaapp_finalproject.api.type.Relationship;

public class CoverArt implements RelationshipAttribute {
    public CoverArt(String fileName) {
        this.fileName = fileName;
    }
    public String description;
    public String volume;
    public String fileName;
    public String createdAt;
    public String updatedAt;
    public Float version;
}
