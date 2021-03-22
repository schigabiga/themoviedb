package com.schigabiga.themoviedb.data.api;

import com.schigabiga.themoviedb.data.model.Movie;
import com.schigabiga.themoviedb.data.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
//https://api.themoviedb.org/3/search/movie?api_key=1b2ebef24b42da2c2eb14b613afaaae7&query=Pirates&page=2&limit=10
public interface MovieApi {
    @GET("movie?")
    Call<MovieResponse> fetchMovies(@Query("api_key") String key, @Query("query") String query, @Query("page") String page);
}
