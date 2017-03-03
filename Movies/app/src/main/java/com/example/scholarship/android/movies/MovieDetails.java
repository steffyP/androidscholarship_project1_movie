package com.example.scholarship.android.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scholarship.android.movies.api.Movie;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    public static final String MOVIE = "movie";
    private Movie movie;

    @BindView(R.id.tv_movie_title)
    TextView mMovieTitleTextView;

    @BindView(R.id.image_movie_poster)
    ImageView mMoviePosterImageView;

    @BindView(R.id.tv_rating)
    TextView mRatingTextView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_movie_plot_content)
    TextView mPlotTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

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
