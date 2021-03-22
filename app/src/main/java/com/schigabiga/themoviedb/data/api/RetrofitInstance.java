package com.schigabiga.themoviedb.data.api;
import com.schigabiga.themoviedb.utils.Constans;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    public static MovieApi createRetrofitInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constans.MOVIE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        MovieApi service = retrofit.create(MovieApi.class);
        return service;
    }
}
