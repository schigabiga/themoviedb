package com.schigabiga.themoviedb.model;

public class Movie {
    private String title,fav,like,desc;

    public Movie(String title, String fav, String like, String desc) {
        this.title = title;
        this.fav = fav;
        this.like = like;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
