package com.example.scholarship.android.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.scholarship.android.movies.data.model.Review;

import java.util.List;


/**
 * Created by stefanie on 05.03.17.
 */

class ReviewArrayAdapter extends ArrayAdapter<Review> {

    private final List<Review> mReviews;

    public ReviewArrayAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
        mReviews = reviews;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = mReviews.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_review, parent, false);
        }
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);

        tvAuthor.setText(review.getAuthor());
        tvContent.setText(review.getContent());

        return convertView;
    }
}
