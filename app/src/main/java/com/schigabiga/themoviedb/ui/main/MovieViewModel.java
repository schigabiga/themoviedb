package com.schigabiga.themoviedb.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.schigabiga.themoviedb.data.model.MovieResponse;
import com.schigabiga.themoviedb.data.repository.MovieRepository;
import com.schigabiga.themoviedb.utils.Resource;
import com.schigabiga.themoviedb.utils.Status;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<Resource<MovieResponse>> movieLiveData = new MutableLiveData<Resource<MovieResponse>>();
    private Call<MovieResponse> movieCall;

    public MovieViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void fetchMovies(String key, String query, String page){
        movieLiveData.setValue(new Resource(Status.LOADING,null,null));
        movieCall = movieRepository.fetchMovies(key,query,page);
        movieCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movies = response.body();
                movieLiveData.setValue(new Resource(Status.SUCCES,movies, null));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                movieLiveData.setValue(new Resource(Status.ERROR,null, "A lekérdezés nem sikerült."));
            }
        });
    }

    public MutableLiveData<Resource<MovieResponse>> getMovies(){
        return movieLiveData;
    }

}
