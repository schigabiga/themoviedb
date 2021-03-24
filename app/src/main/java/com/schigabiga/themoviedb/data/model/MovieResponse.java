package com.schigabiga.themoviedb.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<Movie> results;
    @SerializedName("total_pages")
    private int total_pages;

    public ArrayList<Movie> getResults(){
        return results;
    }

    public int getTotal_pages(){
        return total_pages;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", results=" + results +
                ", total_pages=" + total_pages +
                '}';
    }
}
