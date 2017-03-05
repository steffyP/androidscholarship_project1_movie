package com.example.scholarship.android.movies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.scholarship.android.movies.data.model.Movie;
import com.example.scholarship.android.movies.api.MovieDbApiUtils;


import java.util.List;

/**
 * Adapter for movies, responsible for inflating and showing content in recyclerview
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {


    interface MovieOnClickListener {
        void onClick(Movie movie);
    }

    private List<Movie> mMovies;
    private final Context mContext;
    private final MovieOnClickListener mOnClickListener;


    public MovieAdapter(Context context, MovieOnClickListener onClickListener){
        mContext = context;
        mOnClickListener = onClickListener;

    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_movie, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        // no need to load the image again if it is already stored locally
        Drawable drawable = movie.getBlobImage(mContext);
        if(drawable != null){
            holder.mImageView.setImageDrawable(drawable);
        }else {
            MovieDbApiUtils.getInstance().loadThumbnailImageToImageView(mContext, movie.getPosterPath(), holder.mImageView);
        }
    }


    @Override
    public int getItemCount() {
        if(mMovies == null) {
            return 0;
        }
        return mMovies.size();
    }


    public void setMovies(List<Movie> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_movie_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           if(mOnClickListener != null){
               mOnClickListener.onClick(mMovies.get(getAdapterPosition()));
           }
        }
    }
}
