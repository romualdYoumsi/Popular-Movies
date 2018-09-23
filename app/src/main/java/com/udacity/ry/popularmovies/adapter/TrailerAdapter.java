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
import com.udacity.ry.popularmovies.remote.model.VideosItem;

import java.util.ArrayList;

/**
 * Created by netserve on 22/09/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<VideosItem> trailerList;
    private TrailerAdapterListener mListener;

    //    ViewHolder de l'adapter
    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;

        public TrailerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_trailer_name);
            description = view.findViewById(R.id.tv_trailer_description);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    mListener.onTrailerClickListener(trailerList.get(getAdapterPosition()));
                }
            });
        }
    }


    public TrailerAdapter(Context context, ArrayList<VideosItem> trailerList, TrailerAdapterListener listener) {
        this.mContext = context;
        this.trailerList = trailerList;
        this.mListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer, parent, false);

        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.name.setText(trailerList.get(position).getName());
        holder.description.setText(String.format("%s, %s, %s",
                trailerList.get(position).getSite(),
                trailerList.get(position).getType(),
                trailerList.get(position).getSize()));

    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

}
