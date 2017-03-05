package com.example.scholarship.android.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scholarship.android.movies.api.MovieDbApiUtils;
import com.example.scholarship.android.movies.data.Movie;
import com.example.scholarship.android.movies.data.MovieContract;

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

    @BindView(R.id.star)
    ImageView mImageStar;

    boolean mImageIsStarred = false;
    private Toast mToast;

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
        Cursor cursor = getContentResolver().query(MovieContract.Movie.CONTENT_URI.buildUpon()
                        .appendPath(movie.getId() + "")
                        .build(),
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            // movie has been stared:
            mImageIsStarred = true;
            mImageStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            movie = new Movie(cursor);
            mMoviePosterImageView.setImageDrawable(movie.getBlobImage(this));
        } else {
            MovieDbApiUtils.getInstance().loadThumbnailImageToImageView(this,
                    movie.getPosterPath(),
                    mMoviePosterImageView);
        }

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

    public void addToLocalDatabase(View view) {
        if (!mImageIsStarred) {
            mImageIsStarred = true;
            mImageStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
            // set the drawable to the movie, so it can be stored
            movie.setBlobImage(mMoviePosterImageView.getDrawable());
            ContentValues contentValues = movie.getContentValues();
            Uri uri = getContentResolver().insert(MovieContract.Movie.CONTENT_URI, contentValues);
            if (uri != null) {
                if (mToast != null) mToast.cancel();
                mToast = Toast.makeText(this, getString(R.string.added_movie), Toast.LENGTH_SHORT);
                mToast.show();
                movie.setDatabaseId(Integer.parseInt(uri.getLastPathSegment()));
            }

        } else {
            mImageIsStarred = false;
            mImageStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
            if (movie.getDatabaseId() != -1) {
                int deleted = getContentResolver().delete(MovieContract.Movie.CONTENT_URI
                                .buildUpon()
                                .appendEncodedPath(movie.getDatabaseId() + "").build(),
                        null,
                        null);
                if (deleted > 0) {
                    if (mToast != null) mToast.cancel();
                    mToast = Toast.makeText(this, getString(R.string.removed_movie), Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
           this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
