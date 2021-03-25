package com.schigabiga.themoviedb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.schigabiga.themoviedb.adapter.MovieAdapter;
import com.schigabiga.themoviedb.data.model.Movie;
import com.schigabiga.themoviedb.data.model.MovieResponse;
import com.schigabiga.themoviedb.data.repository.MovieRepository;
import com.schigabiga.themoviedb.ui.main.MovieViewModel;
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
    EditText toolbar_edit;
    ImageButton toolbar_search;
    TextView toolbar_title;

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

    //search click
    boolean search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI elemek, pagination, keresésgomb beállítása
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
                getAlertDialog("Hiba a filmek keresése közben!","A filmek lekérdezése nem sikerült. Kérjük, ellenőrizze internet kapcsolatát és próbálja újra!");
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
        toolbar_edit = findViewById(R.id.toolbar_edit);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_search = findViewById(R.id.toolbar_search);

        movieAdapter = new MovieAdapter(moviesLocal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);

        filterQuery = "";
        search = false;
        setUpToolBarClick();
        setUpNestedPagination();
    }

    private void setUpNestedPagination() {
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

    private void setUpToolBarClick() {
        toolbar_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!search) {
                    toolbar_title.setVisibility(View.INVISIBLE);
                    toolbar_edit.setVisibility(View.VISIBLE);
                    toolbar_search.setImageDrawable(getDrawable(R.drawable.ic_search_on));
                    toolbar_edit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            filterQuery = s.toString();
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }});
                    search = true;
                }else{
                    if(!filterQuery.equals("")) {
                        toolbar_title.setVisibility(View.VISIBLE);
                        toolbar_edit.setVisibility(View.INVISIBLE);
                        toolbar_search.setImageDrawable(getDrawable(R.drawable.ic_search));
                        search = false;
                        textView_notfound.setVisibility(View.GONE);

                        clearLocals();
                        fetchMoviesFromViewModel();
                    }else{
                        getAlertDialog("A keresés mező nem lehet üres!","Kérjük töltse ki milyen keresési feltétel alapján szeretne lekérni filmeket.");
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

    private void getAlertDialog(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}