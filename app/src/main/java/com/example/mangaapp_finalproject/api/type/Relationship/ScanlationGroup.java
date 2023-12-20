package com.example.mangaapp_finalproject.api.type.Relationship;

public class ScanlationGroup implements RelationshipAttribute {
    public ScanlationGroup(String name) {
        this.name = name;
    }
    public String name;
    public String altNames;
    public Boolean locked;
    public String website;
    public String ircServer;
    public String ircChannel;
    public String discord;
    public String contactEmail;
    public String description;
    public String twitter;
    public String mangaUpdates;
    public String focusedLanguages[];
    public Boolean official;
    public Boolean verified;
    public Boolean inactive;
}
