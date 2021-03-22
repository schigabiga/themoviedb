package com.schigabiga.themoviedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.schigabiga.themoviedb.adapter.MovieAdapter;
import com.schigabiga.themoviedb.model.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NestedScrollView nestedScrollView;
    ArrayList<Movie> movies = new ArrayList<Movie>();
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setList();
        setUpUI();
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
        nestedScrollView = findViewById(R.id.nested_scroll);
        recyclerView = findViewById(R.id.recycleview);
        movieAdapter = new MovieAdapter(this,movies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(movieAdapter);
    }

    private void setList(){
        for (int i=0;i<10;i++) {
            movies.add(new Movie("title"+i, String.valueOf(i), String.valueOf(i), "Description "+i));
        }
    }
}