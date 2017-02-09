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

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener{
   public enum SortCriteria{
        TOP_RATED, POPULAR
   }

    private RecyclerView mRecyclerView;
    private View mErrorMessageView;
    private View mProgressBarView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_posters);
        mErrorMessageView = findViewById(R.id.tv_error_message_display);
        mProgressBarView = findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        new MovieLoader().execute(SortCriteria.TOP_RATED);

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
                new MovieLoader().execute(SortCriteria.POPULAR);
                return true;
            case R.id.menu_sort_top_rated:
                new MovieLoader().execute(SortCriteria.TOP_RATED);
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

    /**
     * Loads the selected movies in background, by calling {@link MovieDbApiUtils}
     */
    private class MovieLoader extends AsyncTask<SortCriteria, Void, List<Movie>> {


        @Override
        protected void onPreExecute() {
            mRecyclerView.setVisibility(View.GONE);
            mErrorMessageView.setVisibility(View.GONE);
            mProgressBarView.setVisibility(View.VISIBLE);
        }


        @Override
        protected List<Movie> doInBackground(SortCriteria... params) {
            SortCriteria criteria = params[0];
            if(criteria == SortCriteria.POPULAR){
               return MovieDbApiUtils.getInstance().queryPopularMovies();
            } else {
               return MovieDbApiUtils.getInstance().queryTopRatedMovies();
            }
        }


        @Override
        protected void onPostExecute(List<Movie> movies) {
            mProgressBarView.setVisibility(View.GONE);
           if(movies == null){
               mErrorMessageView.setVisibility(View.VISIBLE);
           } else {
               mMovieAdapter.setMovies(movies);
               mRecyclerView.setVisibility(View.VISIBLE);
           }
        }
    }
}
