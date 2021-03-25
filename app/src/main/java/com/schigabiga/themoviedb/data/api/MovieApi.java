package com.schigabiga.themoviedb.data.api;

import com.schigabiga.themoviedb.data.model.MovieResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("movie?")
    Call<MovieResponse> fetchMovies(@Query("api_key") String key, @Query("query") String query, @Query("page") String page);
}
