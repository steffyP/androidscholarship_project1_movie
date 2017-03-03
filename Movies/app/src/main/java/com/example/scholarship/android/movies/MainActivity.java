package com.example.scholarship.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.scholarship.android.movies.api.Movie;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;
import com.example.scholarship.android.movies.backgroundtasks.MovieLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener, MovieLoader.MovieLoaderCallback{

    public enum SortCriteria{
        TOP_RATED, POPULAR
   }
    @BindView(R.id.recyclerview_movie_posters)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    View mErrorMessageView;

    @BindView(R.id.pb_loading_indicator)
    View mProgressBarView;

    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        new MovieLoader(this).execute(SortCriteria.TOP_RATED);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sort_popular:
                new MovieLoader(this).execute(SortCriteria.POPULAR);
                return true;
            case R.id.menu_sort_top_rated:
                new MovieLoader(this).execute(SortCriteria.TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra(MovieDetails.MOVIE, movie);
        startActivity(intent);
    }


    @Override
    public void onPreExecute() {
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        mProgressBarView.setVisibility(View.GONE);
        if(movies == null){
            mErrorMessageView.setVisibility(View.VISIBLE);
        } else {
            mMovieAdapter.setMovies(movies);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


}
