package com.schigabiga.themoviedb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.schigabiga.themoviedb.adapter.MovieAdapter;
import com.schigabiga.themoviedb.data.model.Movie;
import com.schigabiga.themoviedb.data.model.MovieResponse;
import com.schigabiga.themoviedb.data.repository.MovieRepository;
import com.schigabiga.themoviedb.ui.base.MovieViewModelFactory;
import com.schigabiga.themoviedb.ui.main.viewmodel.MovieViewModel;
import com.schigabiga.themoviedb.utils.Constans;
import com.schigabiga.themoviedb.utils.Resource;
import com.schigabiga.themoviedb.utils.Status;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    TextView textView_notfound;
    ArrayList<Movie> movies = new ArrayList<Movie>();
    MovieAdapter movieAdapter;

    MovieViewModel movieViewModel;

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
                    setupUI(null,Status.LOADING);
                }
                if(movieResource.getStatus() == Status.SUCCES){
                    setupUI(movieResource.getData(),Status.SUCCES);
                }
                if(movieResource.getStatus() == Status.ERROR){
                    setupUI(null,Status.ERROR);
                }
            }
        });
    }

    void setupUI(MovieResponse movieResponse, Status status){
        //todo
        switch (status){
            case ERROR:
                getAlertDialog();
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                textView_notfound.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                break;
            case SUCCES:
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                movieAdapter.setMovies(movieResponse.getResults());
                movieAdapter.notifyDataSetChanged();
                if(movieResponse.getResults().isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    textView_notfound.setVisibility(View.VISIBLE);
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
                movieViewModel.fetchMovies(Constans.API_KEY,query,"1");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpUI(){
        //nestedScrollView = findViewById(R.id.nested_scroll);
        recyclerView = findViewById(R.id.recycleview);
        progressBar = findViewById(R.id.progb);
        textView_notfound = findViewById(R.id.txt_notfound);
        textView_notfound.setVisibility(View.VISIBLE);
        movieAdapter = new MovieAdapter(this,movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);
    }

    private void setupMovieViewModel() {
        MovieRepository movieRepository = new MovieRepository();
        //MovieViewModelFactory movieViewModelFactory = MovieViewModelFactory.createFactory(movieRepository);
        //movieViewModel = new ViewModelProvider(this,movieViewModelFactory).get(MovieViewModel.class);
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