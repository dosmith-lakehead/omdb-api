package com.dosmith.omdb_api.models;

import com.google.gson.annotations.SerializedName;

public class MovieDetails {
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("Rated")
    private String rated;
    @SerializedName("Released")
    private String released;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Genre")
    private String genre;
    @SerializedName("Director")
    private String director;
    @SerializedName("Writer")
    private String writer;
    @SerializedName("Actors")
    private String actors;
    @SerializedName("Plot")
    private String plot;
    @SerializedName("Language")
    private String language;
    @SerializedName("Country")
    private String country;
    @SerializedName("Awards")
    private String awards;
    @SerializedName("Poster")
    private String poster;
    @SerializedName("Ratings")
    private String[] ratings;
    @SerializedName("Metascore")
    private String metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    @SerializedName("Type")
    private String type;
    @SerializedName("DVD")
    private String dvd;
    @SerializedName("BoxOffice")
    private String boxOffice;
    @SerializedName("Production")
    private String production;
    @SerializedName("Website")
    private String website;

    public MovieDetails (){

    }

    public String getActors() {
        return actors;
    }

    public String getAwards() {
        return awards;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public String getCountry() {
        return country;
    }

    public String getDirector() {
        return director;
    }

    public String getDvd() {
        return dvd;
    }

    public String getGenre() {
        return genre;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getLanguage() {
        return language;
    }

    public String getMetascore() {
        return metascore;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public String getProduction() {
        return production;
    }

    public String getRated() {
        return rated;
    }

    public String[] getRatings() {
        return ratings;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getWebsite() {
        return website;
    }

    public String getWriter() {
        return writer;
    }

    public String getYear() {
        return year;
    }
}
