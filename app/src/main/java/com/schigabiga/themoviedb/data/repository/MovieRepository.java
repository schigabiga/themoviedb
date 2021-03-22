package com.schigabiga.themoviedb.data.repository;

import com.schigabiga.themoviedb.data.api.RetrofitInstance;
import com.schigabiga.themoviedb.data.model.MovieResponse;

import retrofit2.Call;

public class MovieRepository {
    public Call<MovieResponse> fetchMovies(String key, String query, String page){
        return RetrofitInstance.createRetrofitInstance().fetchMovies(key,query,page);
    }
}

