package com.schigabiga.themoviedb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.schigabiga.themoviedb.adapter.MovieAdapter;
import com.schigabiga.themoviedb.data.model.Movie;
import com.schigabiga.themoviedb.data.model.MovieResponse;
import com.schigabiga.themoviedb.data.repository.MovieRepository;
import com.schigabiga.themoviedb.ui.main.viewmodel.MovieViewModel;
import com.schigabiga.themoviedb.utils.Constans;
import com.schigabiga.themoviedb.utils.Resource;
import com.schigabiga.themoviedb.utils.Status;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //View elmek
    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    ProgressBar progressBarSearch;
    ProgressBar progressBarPagination;
    TextView textView_notfound;

    //listák
    ArrayList<Movie> moviesService = new ArrayList<Movie>();
    ArrayList<Movie> moviesLocal = new ArrayList<Movie>();

    MovieAdapter movieAdapter;
    MovieViewModel movieViewModel;

    //pagination-hoz változók
    int page=0, current = 0, limit = 10, total_page;
    //keresett szöveg
    String filterQuery;
    boolean firstFetchBySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();
        setupMovieViewModel();
        setupMovieObserver();

    }

    private void setupMovieObserver() {
        movieViewModel.getMovies().observe(this, new Observer<Resource<MovieResponse>>() {
            @Override
            public void onChanged(Resource<MovieResponse> movieResource) {
                if(movieResource.getStatus() == Status.LOADING){
                    setupUIByStatus(null,Status.LOADING);
                }
                if(movieResource.getStatus() == Status.SUCCES){
                    setupUIByStatus(movieResource.getData(),Status.SUCCES);
                }
                if(movieResource.getStatus() == Status.ERROR){
                    setupUIByStatus(null,Status.ERROR);
                }
            }
        });
    }

    void setupUIByStatus(MovieResponse movieResponse, Status status){
        switch (status){
            case ERROR:
                getAlertDialog();
                nestedScrollView.setVisibility(View.GONE);
                progressBarSearch.setVisibility(View.GONE);
                textView_notfound.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                if(firstFetchBySearch) {
                    progressBarSearch.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.GONE);
                }else{
                    progressBarPagination.setVisibility(View.VISIBLE);
                }
                break;
            case SUCCES:
                moviesService = movieResponse.getResults();
                total_page = movieResponse.getTotal_pages();
                setMoviesLocal();
                movieAdapter.setMovies(moviesLocal);
                movieAdapter.notifyDataSetChanged();

                if(firstFetchBySearch) {
                    progressBarSearch.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);
                }else {
                    progressBarPagination.setVisibility(View.GONE);
                }

                if(movieResponse.getResults().isEmpty()){
                    nestedScrollView.setVisibility(View.GONE);
                    textView_notfound.setVisibility(View.VISIBLE);
                }
        }
    }

    void setMoviesLocal(){
        for (int i=current*limit;i<(current+1)*limit;i++){
            if(i<moviesService.size()) {
                moviesLocal.add(moviesService.get(i));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.btn_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Find your movie...");

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textView_notfound.setVisibility(View.GONE);
                filterQuery = query;

                clearLocals();
                fetchMoviesFromViewModel();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void clearLocals(){
        firstFetchBySearch = true;
        moviesLocal.clear();
        moviesService.clear();
        page = 0;
        current = 0;
        total_page = 0;
    }

    private void setUpUI(){
        nestedScrollView = findViewById(R.id.main_nested);
        recyclerView = findViewById(R.id.recycleview);
        progressBarSearch = findViewById(R.id.progb);
        progressBarPagination = findViewById(R.id.progb_pagi);
        textView_notfound = findViewById(R.id.txt_notfound);
        textView_notfound.setVisibility(View.VISIBLE);
        movieAdapter = new MovieAdapter(this,moviesLocal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    if(page != total_page-1) {
                        firstFetchBySearch = false;
                        current++;
                        if (current == 2) {
                            current = 0;
                            page++;
                            fetchMoviesFromViewModel();
                        } else {
                            setMoviesLocal();
                        }
                    }
                }
            }
        });
    }

    private void fetchMoviesFromViewModel(){
        movieViewModel.fetchMovies(Constans.API_KEY, filterQuery, String.valueOf(page + 1));
    }

    private void setupMovieViewModel() {
        MovieRepository movieRepository = new MovieRepository();
        movieViewModel = new MovieViewModel(movieRepository);
    }

    private void getAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Hiba a filmek keresése közben!");
        alertDialog.setMessage("A filmek lekérdezése nem sikerült. Kérjük, ellenőrizze internet kapcsolatát és próbálja újra!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}