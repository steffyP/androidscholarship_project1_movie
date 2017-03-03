package com.example.scholarship.android.movies.backgroundtasks;

/**
 * Created by steffy on 03/03/17.
 */

import android.os.AsyncTask;

import com.example.scholarship.android.movies.MainActivity;
import com.example.scholarship.android.movies.api.Movie;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;

import java.util.List;

/**
 * Loads the selected movies in background, by calling {@link MovieDbApiUtils}
 */
public class MovieLoader extends AsyncTask<MainActivity.SortCriteria, Void, List<Movie>> {
    public interface MovieLoaderCallback{
        void onPreExecute();
        void onPostExecute(List<Movie> movies);
    }


    private final MovieLoaderCallback mCallback;


    public MovieLoader(MovieLoaderCallback callback){
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
      if(mCallback != null){
          mCallback.onPreExecute();
      }
    }


    @Override
    protected List<Movie> doInBackground(MainActivity.SortCriteria... params) {
        MainActivity.SortCriteria criteria = params[0];
        if(criteria == MainActivity.SortCriteria.POPULAR){
            return MovieDbApiUtils.getInstance().queryPopularMovies();
        } else {
            return MovieDbApiUtils.getInstance().queryTopRatedMovies();
        }
    }


    @Override
    protected void onPostExecute(List<Movie> movies) {
       if(mCallback != null)
           mCallback.onPostExecute(movies);
    }
}