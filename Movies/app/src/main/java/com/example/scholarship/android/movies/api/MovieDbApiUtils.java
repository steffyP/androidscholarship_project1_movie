package com.example.scholarship.android.movies.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.example.scholarship.android.movies.BuildConfig;
import com.example.scholarship.android.movies.data.Movie;
import com.example.scholarship.android.movies.data.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Handles and abstracts all calls to <a href="https://www.themoviedb.org/">The Movie DB</a>
 */

public class MovieDbApiUtils {

    private static final String POSTER_SIZE_THUMBNAIL = "w185";
    private static final String POSTER_SIZE_DETAILS = "w342";

    private static final String TAG = MovieDbApiUtils.class.getSimpleName();


    private static final String BASE_URL = "https://api.themoviedb.org/3";

    private static final String MOVIE = "movie";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";


    private static final String QUERY_API_KEY = "api_key";
    private static final String MOVIE_DB_API_KEY = BuildConfig.MOVIE_DB_API_KEY; // TODO: (note for instructors) - replace this with the actual API key

    private static final String QUERY_APPEND_TO_RESPONSE = "append_to_response";
    private static final String PARAM_APPEND = "reviews,videos";

    private static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";

    private static MovieDbApiUtils instance;

    private MovieDbApiUtils(){

    }
    public static MovieDbApiUtils getInstance(){
        if(instance == null){
            instance = new MovieDbApiUtils();
        }
        return instance;
    }

    /**
     * Handles the actual call to the api
     *
     * @param url the URL to be called
     * @return response of the URL call, represented as a String
     */
    private String handleApiCall(URL url) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            // check the response code of the connection
            if (urlConnection.getResponseCode() == 200) {

                // read the response
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // append the stream to the stringBuilder
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // close reader + stream
                reader.close();
                in.close();

            } else {
                Log.e(TAG, "unexpected response code: " + urlConnection.getResponseCode());
            }
            // disconnect connection
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();

    }

    /**
     * Builds the URL for querying movies
     *
     * @param path the GET call: either {@link MovieDbApiUtils#POPULAR} or {@link MovieDbApiUtils#TOP_RATED}
     * @return URL with api key, ready to be called; or null if Uri could not be built
     */
    private URL buildURLToQueryMovies(String path) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(MOVIE).appendPath(path)
                .appendQueryParameter(QUERY_API_KEY, MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * Creates a list of movies out of the json-response
     * Requires the jsonString to have an attribute "results" (JSONArray) where each movie is
     * a JSONObject (as described on the <a href="https://developers.themoviedb.org/3/movies">website</a>)
     *
     * @param jsonString
     * @return list with all movies
     */
    private List<Movie> extractMoviesFromJsonResponse(String jsonString) {

        ArrayList<Movie> movies = null;

        JSONObject json;
        try {
            json = new JSONObject(jsonString);
            JSONArray movieArray = json.optJSONArray("results");
            movies = new ArrayList<>(movieArray.length());

            for (int i = 0; i < movieArray.length(); i++) {
                movies.add(new Movie(movieArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return movies;
    }

    /**
     * Queries the top rated movies (with standard params, e.g. only one page will be returned).
     * See also: <a href="https://developers.themoviedb.org/3/movies/get-top-rated-movies">description on website</a>
     *
     * @return list of movies
     */
    public List<Movie> queryTopRatedMovies() {
        URL url = buildURLToQueryMovies(TOP_RATED);
        if (url == null) {
            return null;
        }
        return extractMoviesFromJsonResponse(handleApiCall(url));

    }

    /**
     * Queries the most popular movies (with standard params, e.g. only one page will be returned).
     * See also: <a href="https://developers.themoviedb.org/3/movies/get-popular-movies">description on website</a>
     *
     * @return list of movies
     */
    public List<Movie> queryPopularMovies() {
        URL url = buildURLToQueryMovies(POPULAR);
        if (url == null) {
            return null;
        }
        return extractMoviesFromJsonResponse(handleApiCall(url));


    }


    /**
     * Loads the posterPath image into the provided imageView (with image size "w185")
     * with the help of {@link Picasso#load(File)}
     *
     * @param context
     * @param posterPath the posterPath from the movie
     * @param imageView the ImageView to load the image into
     */
    public void loadThumbnailImageToImageView(Context context, String posterPath, ImageView imageView){
        Picasso.with(context).load(BASE_URL_POSTER + POSTER_SIZE_THUMBNAIL + posterPath).into(imageView);
    }


    /**
     * Gets additional details e.g. list of videos and reviews for the movie
     *
     * @param movie
     * @return movie with updated infos
     */
    public Movie queryDetailsForMovie(Movie movie){
        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(MOVIE).appendPath(movie.getId()+"")
                .appendQueryParameter(QUERY_API_KEY, MOVIE_DB_API_KEY)
                .appendQueryParameter(QUERY_APPEND_TO_RESPONSE, PARAM_APPEND)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(url != null){
            String result = handleApiCall(url);
            try {
                JSONObject json = new JSONObject(result);

                JSONObject reviews = json.optJSONObject("reviews");
                movie.setReviews(reviews);

                JSONObject videos = json.optJSONObject("videos");
                movie.setVideos(videos);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movie;
    }


}
