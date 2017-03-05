package com.example.scholarship.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.scholarship.android.movies.data.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stefanie on 05.03.17.
 */

public class MovieReviews extends AppCompatActivity {

    private Movie movie;

    @BindView(R.id.list_reviews)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        if (intent != null) {
            if (intent.hasExtra(MovieDetails.MOVIE)) {
                movie = getIntent().getParcelableExtra(MovieDetails.MOVIE);
            }
        }

        setTitle(movie.getTitle());

        listView.setAdapter(new ReviewArrayAdapter(this, movie.getReviews()));
        listView.setClickable(false);
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
}
