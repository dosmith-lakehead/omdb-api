package com.dosmith.omdb_api.models;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

    // annotations allow GSON to deserialize capitalized JSON into my uncapitalized fields.
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    private String imdbID;
    @SerializedName("Type")
    private String type;
    @SerializedName("Poster")
    private String poster;

    public SearchResult(){
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }
}
