package com.udacity.ry.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.database.AppDataBase;
import com.udacity.ry.popularmovies.database.AppExecutors;
import com.udacity.ry.popularmovies.database.MovieEntry;
import com.udacity.ry.popularmovies.model.RYMovie;
import com.udacity.ry.popularmovies.pages.details.DetailsActivity;
import com.udacity.ry.popularmovies.remote.ApiUtils;

import java.util.List;

/**
 * Created by netserve on 08/08/2018.
 */

public class RYMovieAdapter extends ArrayAdapter<RYMovie> {
    private static final String TAG = RYMovieAdapter.class.getSimpleName();

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    private int mTaskId = DEFAULT_TASK_ID;

    // Member variable for the Database
    private AppDataBase mDb;

    public RYMovieAdapter(Context context, List<RYMovie> movies) {

        super(context, 0, movies);

        mDb = AppDataBase.getsInstance(context.getApplicationContext());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Gets the RYMovie object from the ArrayAdapter at the appropriate position
        final RYMovie ryMovie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        RelativeLayout cardMovie = (RelativeLayout) convertView.findViewById(R.id.card_view_content);

//        set the value of views
        ImageView posterView = (ImageView) convertView.findViewById(R.id.poster_iv);
        ImageView favouriteIV = (ImageView) convertView.findViewById(R.id.iv_favourite);
        Picasso.with(getContext()).load(ApiUtils.MOVIES_POSTER_BASE_URL + ryMovie.getPoster_path()).into(posterView);

        TextView titleView = (TextView) convertView.findViewById(R.id.title_tv);
        titleView.setText(ryMovie != null ? ryMovie.getTitle() : "");

        TextView releaseDateView = (TextView) convertView.findViewById(R.id.release_date_tv);
        releaseDateView.setText(ryMovie.getRelease_date());
//        releaseDateView.setText(getContext().getString(R.string.release_date)+" "+ryMovie.getRelease_date());

        /*
        RatingBar vote = (RatingBar) convertView.findViewById(R.id.rating_rb);
        vote.setRating(Float.parseFloat(""+(ryMovie.getVote_average()/2))); */

        TextView voteTV = (TextView) convertView.findViewById(R.id.rating_tv);
        voteTV.setText(String.valueOf(ryMovie.getVote_average()));

        cardMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.e(TAG, "onItemClick: position="+position+" title="+ryMovie.getTitle()+" Vote_average="+ryMovie.getVote_average()+" Release_date="+ryMovie.getRelease_date());

                Intent i = new Intent(getContext(), DetailsActivity.class);

                //Sending movie item data to DetailsActivity
                i.putExtra("movie_item", ryMovie);

                getContext().startActivity(i);
            }
        });


        // get movie in db
        MovieEntry movieEntry = mDb.movieDAO().getMovieById(ryMovie.getId());
//        set favorite icon
        if (movieEntry != null) {
            favouriteIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        } else {
            favouriteIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
        }

        favouriteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onItemClick: favourite click");
                if (ryMovie.getFavorite() == 1) {
                    getItem(position).setFavorite(0);
                    deleteRYMovie(ryMovie);
                } else {
                    getItem(position).setFavorite(1);
                    insertRYMovie(ryMovie);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

//    insert a movie in database
    public void insertRYMovie(RYMovie ryMovie) {

        final MovieEntry movieEntry = new MovieEntry(ryMovie.getId(), ryMovie.getPoster_path(), ryMovie.getTitle(), ryMovie.getRelease_date(), ryMovie.getVote_average(), ryMovie.getOverview());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // insert new task
                mDb.movieDAO().insertMovie(movieEntry);
                Log.e(TAG, "run: insertRYMovie");
            }
        });
    }

//    delete a movie in database
    public void deleteRYMovie(RYMovie ryMovie) {

        final MovieEntry movieEntry = new MovieEntry(ryMovie.getId(), ryMovie.getPoster_path(), ryMovie.getTitle(), ryMovie.getRelease_date(), ryMovie.getVote_average(), ryMovie.getOverview());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // insert new task
                mDb.movieDAO().deleteMovie(movieEntry);
                Log.e(TAG, "run: deleteRYMovie");
            }
        });
    }
}
