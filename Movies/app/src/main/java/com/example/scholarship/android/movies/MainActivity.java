package com.example.scholarship.android.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.scholarship.android.movies.backgroundtasks.LoaderCallbackMovies;
import com.example.scholarship.android.movies.data.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieOnClickListener, LoaderCallbackMovies.MovieLoaderCallback {


    private static final String SELECTED_CRITERIA = "selected_criteria";

    public enum SortCriteria {
        TOP_RATED, POPULAR, LOCALLY
    }

    @BindView(R.id.recyclerview_movie_posters)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageView;

    @BindView(R.id.pb_loading_indicator)
    View mProgressBarView;

    private MovieAdapter mMovieAdapter;
    private static final int LOADER_MOVIE_ID = 1122;
    private LoaderCallbackMovies mLoaderCallback;
    private SortCriteria mSelectedSortCriteria = SortCriteria.TOP_RATED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this, 3);
        }
        else{
            layoutManager = new GridLayoutManager(this, 5);
        }

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        setupLoaderManager(mSelectedSortCriteria);
    }


    private void setupLoaderManager(SortCriteria sortCritera) {
        Bundle bundle = new Bundle();
        bundle.putString(LoaderCallbackMovies.LOAD_IDENTIFIER, LoaderCallbackMovies.LOAD_TOP_RATED);
        bundle.putString(LoaderCallbackMovies.SORT_CRITERIA_IDENTIFIER, sortCritera.toString());
        Loader loader = getSupportLoaderManager().getLoader(LOADER_MOVIE_ID);
        if (mLoaderCallback == null) {
            mLoaderCallback = new LoaderCallbackMovies(this, this);
        }
        if (loader != null) {
            getSupportLoaderManager().restartLoader(LOADER_MOVIE_ID, bundle, mLoaderCallback);
        } else {
            getSupportLoaderManager().initLoader(LOADER_MOVIE_ID, bundle, mLoaderCallback);
        }
        switch(sortCritera){

            case TOP_RATED:
                setTitle(getString(R.string.title_top_rated));
                break;
            case POPULAR:
                setTitle(getString(R.string.title_popular));
                break;
            case LOCALLY:
                setTitle(getString(R.string.title_local_collection));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(LOADER_MOVIE_ID);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                mSelectedSortCriteria = SortCriteria.POPULAR;
                break;
            case R.id.menu_sort_top_rated:
                mSelectedSortCriteria = SortCriteria.TOP_RATED;
                break;
            case R.id.menu_local_collection:
                mSelectedSortCriteria = SortCriteria.LOCALLY;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        setupLoaderManager(mSelectedSortCriteria);
        return true;
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra(MovieDetails.MOVIE, movie);
        startActivity(intent);
    }


    @Override
    public void onPreExecute() {
        if (isFinishing()) return;

        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageView.setVisibility(View.GONE);
        mProgressBarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(List<Movie> movies) {
        if (isFinishing()) return;

        mProgressBarView.setVisibility(View.GONE);
        if (movies == null || movies.isEmpty()) {
            if(mSelectedSortCriteria == SortCriteria.LOCALLY){
                mErrorMessageView.setText(getString(R.string.no_local_movies));
            } else {
                mErrorMessageView.setText(getString(R.string.error_message));

            }
            mErrorMessageView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mMovieAdapter.setMovies(movies);
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorMessageView.setVisibility(View.GONE);
        }
    }


}
