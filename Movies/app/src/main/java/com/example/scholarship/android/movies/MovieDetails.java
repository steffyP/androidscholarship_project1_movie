package com.example.scholarship.android.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scholarship.android.movies.api.MovieDbApiUtils;
import com.example.scholarship.android.movies.backgroundtasks.DetailsAsyncTask;
import com.example.scholarship.android.movies.data.model.Movie;
import com.example.scholarship.android.movies.data.MovieContract;
import com.example.scholarship.android.movies.data.model.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity implements DetailsAsyncTask.AsyncCallback {

    public static final String MOVIE = "movie";
    private static final String YOU_TUBE_BASE_URL = "https://www.youtube.com/watch?v=";
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

    @BindView(R.id.tv_show_reviews)
    TextView mShowReviews;

    @BindView(R.id.tv_no_reviews)
    TextView mNoReviews;

    @BindView(R.id.pb_trailer)
    ProgressBar pbTrailer;

    @BindView(R.id.trailer_container)
    LinearLayout mTrailerContainer;

    @BindView(R.id.tv_no_trailers_found)
    TextView mNoTrailersFound;

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
        new DetailsAsyncTask(this).execute(movie);

        mMovieTitleTextView.setText(movie.getTitle());
        mRatingTextView.setText(getString(R.string.movie_rating, movie.getVoteAverage()));
        mReleaseDateTextView.setText(getString(R.string.movie_release_date, movie.getReleaseDateString()));
        mPlotTextView.setText(movie.getOverview());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setUpOnClickListenerForStar();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MovieDetails.MOVIE, movie);
    }

    private void setUpOnClickListenerForStar() {
        mImageStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mImageIsStarred) {
                    mImageIsStarred = true;
                    mImageStar.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_filled));
                    // set the drawable to the movie, so it can be stored
                    movie.setBlobImage(mMoviePosterImageView.getDrawable());
                    ContentValues contentValues = movie.getContentValues();
                    Uri uri = getContentResolver().insert(MovieContract.Movie.CONTENT_URI, contentValues);
                    if (uri != null) {
                        if (mToast != null) mToast.cancel();
                        mToast = Toast.makeText(MovieDetails.this, getString(R.string.added_movie), Toast.LENGTH_SHORT);
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
                            mToast = Toast.makeText(MovieDetails.this, getString(R.string.removed_movie), Toast.LENGTH_SHORT);
                            mToast.show();
                        }
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onPostExecute(final Movie movie) {
        if (movie.getReviews() == null) {
            mNoReviews.setVisibility(View.VISIBLE);
        } else {
            mShowReviews.setVisibility(View.VISIBLE);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MovieDetails.this, MovieReviews.class);
                    intent.putExtra(MOVIE, movie);
                    startActivity(intent);
                }
            };
            mShowReviews.setOnClickListener(onClickListener);
            mMoviePosterImageView.setOnClickListener(onClickListener);
        }
        if (movie.getVideos() == null) {
            mNoTrailersFound.setVisibility(View.VISIBLE);
        } else {
            for(final Video video : movie.getVideos()){
                View child = getLayoutInflater().inflate(R.layout.item_trailer, null);

                TextView tv = (TextView) child.findViewById(R.id.tv_trailer_title);
                String title = video.getName();
                if(video.getType() != null && ! video.getType().isEmpty()){
                    title += " (" +video.getType()  +")";
                }
                tv.setText(title);
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent playerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOU_TUBE_BASE_URL + video.getKey()));

                        String title = getResources().getString(R.string.chooser_title);
                        Intent chooser = Intent.createChooser(playerIntent, title);

                        if (playerIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(chooser);
                        }

                    }
                });
                View divider = new View(this);
                divider.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(getResources().getColor(R.color.grey));
                mTrailerContainer.addView(child);
                mTrailerContainer.addView(divider);
            }
        }

        pbTrailer.setVisibility(View.GONE);

    }
}
