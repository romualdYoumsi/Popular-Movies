package com.udacity.ry.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.interfaces.TrailerAdapterListener;
import com.udacity.ry.popularmovies.remote.model.ReviewsItem;
import com.udacity.ry.popularmovies.remote.model.VideosItem;

import java.util.ArrayList;

/**
 * Created by netserve on 22/09/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<ReviewsItem> reviewsItemList;

    //    ViewHolder de l'adapter
    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        public TextView author, content;

        public ReviewsViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.tv_review_author);
            content = view.findViewById(R.id.tv_review_content);

        }
    }


    public ReviewsAdapter(Context context, ArrayList<ReviewsItem> reviewsItems) {
        this.mContext = context;
        this.reviewsItemList = reviewsItems;
    }

    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);

        return new ReviewsAdapter.ReviewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsViewHolder holder, int position) {
        holder.author.setText(reviewsItemList.get(position).getAuthor());
        holder.content.setText(reviewsItemList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return reviewsItemList.size();
    }

}
