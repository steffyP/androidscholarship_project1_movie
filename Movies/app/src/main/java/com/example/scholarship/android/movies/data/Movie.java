package com.example.scholarship.android.movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstracts the content of a "movie"
 */

public class Movie implements Parcelable {

    private String mPosterPath;
    private String mOverview;

    private String mReleaseDateString;


    private int mDatabaseId = -1;
    private int mId;

    private String mTitle;

    private double mPopularity;

    private byte[] mBlob;
    private String mReviewsJsonString;
    private String mVideosJsonString;

    private List<Video> videos;
    private List<Review> reviews;

    public Movie(JSONObject json) {
        mPosterPath = json.optString("poster_path");
        mOverview = json.optString("overview");
        mReleaseDateString = json.optString("release_date");
        mId = json.optInt("id");
        mTitle = json.optString("title");
        mPopularity = json.optDouble("popularity");
    }

    public Movie(Cursor cursor) {
        if (cursor.getPosition() != -1) {
            mPosterPath = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POSTER_PATH));
            mOverview = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_OVERVIEW));
            mReleaseDateString = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_RELEASE_DATE));

            mId = cursor.getInt(cursor.getColumnIndex(MovieContract.Movie.COLUMN_MOVIE_ID));

            mTitle = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_TITLE));


            mPopularity = cursor.getDouble(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POPULARITY));

            mDatabaseId = cursor.getInt(cursor.getColumnIndex(MovieContract.Movie._ID));

            mBlob = cursor.getBlob(cursor.getColumnIndex(MovieContract.Movie.COLUMN_POSTER_BLOB));

            mVideosJsonString = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_VIDEOS_JSON_STRING));
            mReviewsJsonString = cursor.getString(cursor.getColumnIndex(MovieContract.Movie.COLUMN_REVIEWS_JSON_STRING));
        }
    }

    public int getDatabaseId() {
        return mDatabaseId;
    }

    public void setDatabaseId(int id) {
        mDatabaseId = id;
    }

    public String getPosterPath() {
        return mPosterPath;
    }


    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDateString() {
        return mReleaseDateString;
    }


    public int getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDateString);
        dest.writeInt(mId);

        dest.writeString(mTitle);
        dest.writeDouble(mPopularity);

        dest.writeInt(mDatabaseId);

        int blobLen = mBlob == null ? 0 : mBlob.length;
        dest.writeInt(blobLen);
        if (mBlob != null) {
            dest.writeByteArray(mBlob);
        }

        dest.writeString(mReviewsJsonString);
        dest.writeString(mVideosJsonString);
    }


    private Movie(Parcel in) {
        mPosterPath = in.readString();
        mOverview = in.readString();
        mReleaseDateString = in.readString();
        mId = in.readInt();
        mTitle = in.readString();
        mPopularity = in.readDouble();
        mDatabaseId = in.readInt();
        int blobLen = in.readInt();
        if (blobLen > 0) {
            mBlob = new byte[blobLen];
            in.readByteArray(mBlob);
        }
        mReviewsJsonString = in.readString();
        mVideosJsonString = in.readString();
    }


    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.Movie.COLUMN_MOVIE_ID, mId);
        contentValues.put(MovieContract.Movie.COLUMN_OVERVIEW, mOverview);
        contentValues.put(MovieContract.Movie.COLUMN_POPULARITY, mPopularity);
        contentValues.put(MovieContract.Movie.COLUMN_POSTER_PATH, mPosterPath);
        contentValues.put(MovieContract.Movie.COLUMN_RELEASE_DATE, mReleaseDateString);
        contentValues.put(MovieContract.Movie.COLUMN_TITLE, mTitle);
        contentValues.put(MovieContract.Movie.COLUMN_POSTER_BLOB, mBlob);
        contentValues.put(MovieContract.Movie.COLUMN_REVIEWS_JSON_STRING, mReviewsJsonString);
        contentValues.put(MovieContract.Movie.COLUMN_VIDEOS_JSON_STRING, mVideosJsonString);
        return contentValues;
    }

    public void setBlobImage(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        mBlob = bitmapdata;
    }

    public Drawable getBlobImage(Context context) {
        if (mBlob == null) return null;
        return new BitmapDrawable(context.getResources(),
                BitmapFactory.decodeByteArray(mBlob, 0, mBlob.length));
    }

    public List<Video> getVideos() {
        if(videos == null && mVideosJsonString != null){
            try {
                JSONObject jsonObject = new JSONObject(mVideosJsonString);
                JSONArray array = jsonObject.optJSONArray("results");
                if(array != null){
                    videos = new ArrayList<>();
                    for(int i = 0; i < array.length(); i++){
                        videos.add(new Video(array.optJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return videos;
    }

    public List<Review> getReviews() {
        if(reviews == null && mReviewsJsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(mReviewsJsonString);
                JSONArray array = jsonObject.optJSONArray("results");
                if (array != null) {
                    reviews = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        reviews.add(new Review(array.optJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reviews;
    }

    public void setReviews(JSONObject reviews) {
        mReviewsJsonString = reviews.toString();
    }


    public void setVideos(JSONObject videos) {
        mVideosJsonString = videos.toString();
    }
}
