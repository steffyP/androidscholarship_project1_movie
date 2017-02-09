package com.example.scholarship.android.movies.api;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Abstracts the content of a "movie"
 */

public class Movie implements Parcelable {

    private final String mPosterPath;
    private final boolean mIsAdult;
    private final String mOverview;

    private final String mReleaseDateString;
    private int[] mGenreIds;

    private final int mId;
    private final String mOriginalTitle;
    private final String mOriginalLanguage;

    private final String mTitle;
    private final String mBackdropPath;

    private final double mPopularity;

    private final int mVoteCount;
    private final boolean mHasVideo;

    private final double mVoteAverage;


    public Movie(JSONObject json) {
        mPosterPath = json.optString("poster_path");
        mIsAdult = json.optBoolean("adult");
        mOverview = json.optString("overview");
        mReleaseDateString = json.optString("release_date");

        JSONArray array = json.optJSONArray("genre_ids");
        mGenreIds = new int[array.length()];

        // Extract numbers from JSON array.
        for (int i = 0; i < array.length(); ++i) {
            mGenreIds[i] = array.optInt(i);
        }

        mId = json.optInt("id");
        mOriginalTitle = json.optString("original_title");
        mOriginalLanguage = json.optString("original_language");

        mTitle = json.optString("title");

        mBackdropPath = json.optString("backdrop_path");

        mPopularity = json.optDouble("popularity");
        mVoteCount = json.optInt("vote_count");

        mHasVideo = json.optBoolean("video");

        mVoteAverage = json.optDouble("vote_average");

    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public boolean isAdult() {
        return mIsAdult;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDateString() {
        return mReleaseDateString;
    }

    public int[] getGenreIds() {
        return mGenreIds;
    }

    public int getId() {
        return mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public boolean hasVideo() {
        return mHasVideo;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeInt(mIsAdult ? 1 : 0);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDateString);

        // only wrap the int array if it is not null
        dest.writeInt(mGenreIds != null ? mGenreIds.length : 0);
        if(mGenreIds != null) {
            dest.writeIntArray(mGenreIds);
        }

        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mTitle);
        dest.writeString(mBackdropPath);
        dest.writeDouble(mPopularity);
        dest.writeInt(mVoteCount);
        dest.writeInt(mHasVideo ? 1 : 0);
        dest.writeDouble(mVoteAverage);
    }


    private Movie(Parcel in) {
        mPosterPath = in.readString();
        mIsAdult = in.readInt() == 1;
        mOverview = in.readString();
        mReleaseDateString = in.readString();

        // check the length of the array first, only with size > 0 the array has actually been wrapped
        int len = in.readInt();
        if(len > 0) {
            mGenreIds = new int[len];
            in.readIntArray(mGenreIds);
        }
        mId = in.readInt();
        mOriginalTitle = in.readString();
        mOriginalLanguage = in.readString();
        mTitle = in.readString();
        mBackdropPath = in.readString();
        mPopularity = in.readDouble();
        mVoteCount = in.readInt();
        mHasVideo = in.readInt() == 1;
        mVoteAverage = in.readDouble();

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

}
