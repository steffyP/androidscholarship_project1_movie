package com.example.scholarship.android.movies.backgroundtasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.scholarship.android.movies.MainActivity;
import com.example.scholarship.android.movies.data.Movie;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;

import java.util.List;

/**
 * Created by stefanie on 04.03.17.
 */

public class AsyncMovieLoader extends AsyncTaskLoader<List<Movie>> {

    private final Bundle mArgs;
    private Context mContext;
    private String savedIdentifier;
    private List<Movie> movies;

    public AsyncMovieLoader(Context context, Bundle args) {
        super(context);
        mContext = context;
        mArgs = args;
    }

    @Override
    public List<Movie> loadInBackground() {
        String sortCriteria = mArgs.getString(LoaderCallbackMovies.SORT_CRITERIA_IDENTIFIER, "");
        if(! sortCriteria.isEmpty()) {
            MainActivity.SortCriteria criteria = MainActivity.SortCriteria.valueOf(sortCriteria);
            if (criteria == MainActivity.SortCriteria.POPULAR) {
                return MovieDbApiUtils.getInstance().queryPopularMovies();
            } else if (criteria == MainActivity.SortCriteria.TOP_RATED){
                return MovieDbApiUtils.getInstance().queryTopRatedMovies();
            } // todo: else  starred
        }
        return null;

    }

    @Override
    protected void onStartLoading() {
        if (mArgs == null) {
            return;
        }
        String loadIdentifier =   mArgs.getString(LoaderCallbackMovies.LOAD_IDENTIFIER, "");
        savedIdentifier = loadIdentifier;
        if(loadIdentifier.equals(savedIdentifier) && movies != null){
            deliverResult(movies);
        } else {
            forceLoad();
        }

    }

    @Override
    public void deliverResult(List<Movie> movies) {
        this.movies = movies;
        super.deliverResult(movies);
    }

}
