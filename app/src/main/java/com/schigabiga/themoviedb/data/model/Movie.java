package com.schigabiga.themoviedb.data.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("original_title")
    private String title;
    @SerializedName("vote_average")
    private double fav;
    @SerializedName("vote_count")
    private double like;
    @SerializedName("overview")
    private String desc;
    @SerializedName("poster_path")
    private String img;

    public Movie(String title, double fav, double like, String desc, String img) {
        this.title = title;
        this.fav = fav;
        this.like = like;
        this.desc = desc;
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public double getFav() {
        return fav;
    }

    public double getLike() {
        return like;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", fav=" + fav +
                ", like=" + like +
                ", desc='" + desc + '\'' +
                '}';
    }

    public String getImg() {
        return img;
    }
}
