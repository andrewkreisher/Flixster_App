package com.example.flixster;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.flixster.MainActivity.MOVIE_IMAGE;
import static com.example.flixster.MainActivity.MOVIE_OVERVIEW;
import static com.example.flixster.MainActivity.MOVIE_RATING;
import static com.example.flixster.MainActivity.MOVIE_TITLE;





public class moreInfo extends AppCompatActivity {
    TextView tvTitle;
    TextView tvOverview;
    ImageView ivBackdrop;
    TextView tvRatings;
    RatingBar rbVoteAverage;
    float rating;
    String url;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        tvTitle = findViewById(R.id.tvTitle);
        tvRatings = findViewById(R.id.tvRatings);
        tvOverview = findViewById(R.id.tvOverview);
        ivBackdrop = findViewById(R.id.ivBackdrop);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        LayerDrawable stars = (LayerDrawable) rbVoteAverage.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        tvTitle.setText(getIntent().getStringExtra(MOVIE_TITLE));
        tvOverview.setText(getIntent().getStringExtra(MOVIE_OVERVIEW));
        rating = (float)getIntent().getDoubleExtra(MOVIE_RATING, 0);
        tvRatings.setText("Ratings:  " + Float.toString(rating) + "/10");
        rbVoteAverage.setRating(rating = rating > 0 ? rating / 2.0f : rating);
        url = getIntent().getStringExtra(MOVIE_IMAGE);

        //url2 = config.getImageUrl(config.getBackdropSize(), url);

        Glide.with(this)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
                .into(ivBackdrop);

    }
}
