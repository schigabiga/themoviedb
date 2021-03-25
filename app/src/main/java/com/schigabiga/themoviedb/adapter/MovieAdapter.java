package com.schigabiga.themoviedb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.schigabiga.themoviedb.R;
import com.schigabiga.themoviedb.data.model.Movie;
import com.schigabiga.themoviedb.utils.Constans;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> movies;

    public MovieAdapter(ArrayList<Movie> movies){
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
        holder.fav.setText(String.valueOf(movie_current.getFav()));
        holder.like.setText(String.valueOf(movie_current.getLike()));
        holder.desc.setText(movie_current.getDesc());
        Picasso.get().load(Constans.IMAGE_URL+movie_current.getImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(ArrayList<Movie> movies){
        this.movies=movies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView fav;
        TextView like;
        TextView desc;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_item_title);
            fav = itemView.findViewById(R.id.movie_item_fav);
            like = itemView.findViewById(R.id.movie_item_like);
            desc = itemView.findViewById(R.id.movie_item_desc);
            img = itemView.findViewById(R.id.movie_item_img);
        }
    }
}
