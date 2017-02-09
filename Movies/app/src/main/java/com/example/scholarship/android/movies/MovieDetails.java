package com.example.scholarship.android.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scholarship.android.movies.api.Movie;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;

public class MovieDetails extends AppCompatActivity {

    public static final String MOVIE = "movie";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView mMovieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        ImageView mMoviePosterImageView = (ImageView) findViewById(R.id.image_movie_poster);
        TextView mRatingTextView = (TextView) findViewById(R.id.tv_rating);
        TextView mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        TextView mPlotTextView = (TextView) findViewById(R.id.tv_movie_plot_content);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(MovieDetails.MOVIE)) {
                movie = getIntent().getParcelableExtra(MovieDetails.MOVIE);
            }
        }
        MovieDbApiUtils.getInstance().loadLargerImageToImageView(this, movie.getPosterPath(), mMoviePosterImageView);
        mMovieTitleTextView.setText(movie.getTitle());
        mRatingTextView.setText(getString(R.string.movie_rating, movie.getVoteAverage()));
        mReleaseDateTextView.setText(getString(R.string.movie_release_date, movie.getReleaseDateString()));
        mPlotTextView.setText(movie.getOverview());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MovieDetails.MOVIE, movie);
    }
}
