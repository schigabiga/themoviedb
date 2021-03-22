package com.schigabiga.themoviedb.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schigabiga.themoviedb.R;
import com.schigabiga.themoviedb.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    private Activity activity;

    public MovieAdapter(Activity activity, ArrayList<Movie> movies){
        this.activity = activity;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie_current = movies.get(position);

        holder.title.setText(movie_current.getTitle());
        holder.fav.setText(movie_current.getFav());
        holder.like.setText(movie_current.getLike());
        holder.desc.setText(movie_current.getDesc());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView fav;
        TextView like;
        TextView desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_item_title);
            fav = itemView.findViewById(R.id.movie_item_fav);
            like = itemView.findViewById(R.id.movie_item_like);
            desc = itemView.findViewById(R.id.movie_item_desc);
        }
    }
}
