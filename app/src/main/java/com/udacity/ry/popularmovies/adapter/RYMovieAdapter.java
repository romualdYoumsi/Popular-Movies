package com.udacity.ry.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.model.RYMovie;
import com.udacity.ry.popularmovies.pages.details.DetailsActivity;
import com.udacity.ry.popularmovies.remote.ApiUtils;

import java.util.List;

/**
 * Created by netserve on 08/08/2018.
 */

public class RYMovieAdapter extends ArrayAdapter<RYMovie> implements AdapterView.OnItemClickListener {
    private static final String TAG = RYMovieAdapter.class.getSimpleName();

    public RYMovieAdapter(Context context, List<RYMovie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Gets the RYMovie object from the ArrayAdapter at the appropriate position
        final RYMovie ryMovie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        RelativeLayout cardMovie = (RelativeLayout) convertView.findViewById(R.id.card_view_content);
        cardMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e(TAG, "onItemClick: position="+position+" title="+ryMovie.getTitle()+" Vote_average="+ryMovie.getVote_average()+" Release_date="+ryMovie.getRelease_date());

                Intent i = new Intent(getContext(), DetailsActivity.class);
                //If you wanna send any data to nextActicity.class you can use
                i.putExtra("movie_id", ryMovie.getId());

                getContext().startActivity(i);
            }
        });

//        set the value of views
        ImageView posterView = (ImageView) convertView.findViewById(R.id.poster_iv);
        Picasso.with(getContext()).load(ApiUtils.MOVIES_POSTER_BASE_URL+ryMovie.getPoster_path()).into(posterView);

        TextView titleView = (TextView) convertView.findViewById(R.id.title_tv);
        titleView.setText(ryMovie != null ? ryMovie.getTitle() : "");

        TextView releaseDateView = (TextView) convertView.findViewById(R.id.release_date_tv);
        releaseDateView.setText(ryMovie.getRelease_date());
//        releaseDateView.setText(getContext().getString(R.string.release_date)+" "+ryMovie.getRelease_date());

        RatingBar vote = (RatingBar) convertView.findViewById(R.id.rating_rb);
        vote.setRating(Float.parseFloat(""+(ryMovie.getVote_average()/2)));

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        RYMovie item = getItem(position);
//        Log.e(TAG, "onItemClick: position="+position+" title="+item.getTitle()+" Vote_average="+item.getVote_average()+" Release_date="+item.getRelease_date());

        Intent i = new Intent(getContext(), DetailsActivity.class);
        //If you wanna send any data to nextActicity.class you can use
        i.putExtra("movie_id", item.getId());

        getContext().startActivity(i);
    }
}
