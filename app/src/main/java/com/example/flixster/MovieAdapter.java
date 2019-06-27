package com.example.flixster;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    //list of movies
    ArrayList<Movie> movies;


    //config for urls
    Config config;
    //context for render
    Context context;


    //init with list


    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }


    //creates new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //get the context and create inflator
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create view using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new viewHolder
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a item on list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get movie data at position i
        Movie movie = movies.get(i);
        //populate view with movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //check if in portrait or landscape
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


        if (i%2 == 0) {
            viewHolder.view.setBackgroundColor(Color.parseColor("#272727"));
        }
        else{
            viewHolder.view.setBackgroundColor(Color.parseColor("#444444"));
        }

        // build url for poster image
        String imageUrl = null;

        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else{
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        //get correct placeholder
        int placeholderId = isPortrait ? R.drawable.loading : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;


        //load image with glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }

    //returns size of dataset
    @Override
    public int getItemCount() {
        return movies.size();
    }

//create the viewholder as static inner class

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //lookup view objects using id
            view = itemView;
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdrop);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
