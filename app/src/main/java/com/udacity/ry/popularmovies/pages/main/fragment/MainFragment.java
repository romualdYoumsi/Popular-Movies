package com.udacity.ry.popularmovies.pages.main.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.adapter.RYMovieAdapter;
import com.udacity.ry.popularmovies.model.RYMovie;
import com.udacity.ry.popularmovies.pages.details.DetailsActivity;
import com.udacity.ry.popularmovies.remote.ApiUtils;
import com.udacity.ry.popularmovies.remote.model.Discover;
import com.udacity.ry.popularmovies.remote.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    public static final String TAG = MainFragment.class.getSimpleName();

    private RYMovieAdapter mRYMovieAdapter;
    ArrayList<RYMovie> mRYMovies = new ArrayList<>();

    private GridView mGridView;

    public MainFragment() {
        // Required empty public constructor
//        Log.e(TAG, "MainFragment:");

//        mRYMovies.add(new RYMovie(0, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 0 Movie 0 Movie 0", "2020-05-24", 5.6));
//        mRYMovies.add(new RYMovie(1, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 1", "2019-12-29", 5.6));
//        mRYMovies.add(new RYMovie(2, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 2", "2019-10-09", 5.6));
//        mRYMovies.add(new RYMovie(3, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 3", "2019-09-15", 5.6));
//        mRYMovies.add(new RYMovie(4, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 4", "2019-04-30", 5.6));
//        mRYMovies.add(new RYMovie(5, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 5 Movie 5 Movie 5 Movie 5", "2019-02-17", 5.6));
//        mRYMovies.add(new RYMovie(6, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 6", "2018-11-21", 5.6));
//        mRYMovies.add(new RYMovie(7, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 7", "2018-11-08", 5.6));
//        mRYMovies.add(new RYMovie(8, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 8", "2018-05-31", 5.6));
//        mRYMovies.add(new RYMovie(9, "kmcqlZGaSh20zpTbuoF0Cdn07dT.jpg", "Movie 9", "2018-04-28", 5.6));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new DiscoverMoviesTask(getContext()).execute(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRYMovieAdapter = new RYMovieAdapter(getActivity(), mRYMovies);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mGridView.setAdapter(mRYMovieAdapter);

        setUpSharedPreference();
        return rootView;
    }

    private void setUpSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort = sharedPreferences.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_most_popular));

        Log.e(TAG, "setUpSharedPreference: sort="+sort);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e(TAG, "onSharedPreferenceChanged: key="+key);

//        remove all movies
        mRYMovieAdapter.clear();

//        fetch movies on server
        new DiscoverMoviesTask(getContext()).execute(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    //    Discover Movies async task
    private class DiscoverMoviesTask extends AsyncTask<Integer, Void, Discover> {
        private Context context;

        public DiscoverMoviesTask(Context context) {
            this.context = context;
        }

        @Override
    protected Discover doInBackground(Integer... integers) {
        int page = integers[0];

//        Getting the sharedPreference value
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        String sort = sharedPreferences.getString(this.context.getString(R.string.pref_sort_order_key), this.context.getString(R.string.pref_sort_order_most_popular));
        String language = sharedPreferences.getString(this.context.getString(R.string.pref_language_key), this.context.getString(R.string.pref_language_fr));

//        Requesting for movies to the movie db

        Call<Discover> call = ApiUtils.getMoviesService().discoverMovie(page, language, sort);
        try {
            Response<Discover> response = call.execute();
            if (response.isSuccessful()) {
                Discover discover = response.body();
//                Log.e(TAG, "DiscoverMoviesTask:doInBackground: discover.getResults().size="+discover.getResults().size());
                return discover;
            } else {
                String error = null;
                try {
                    error = response.errorBody().string();
                    JSONObject jsonObjectError = new JSONObject(error);
//                        String errorCode = jsonObjectError.getString("errorCode");
//                        String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "DiscoverMoviesTask:doLogin onResponse err: " + error);

                } catch (IOException e) {

                    e.printStackTrace();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Discover discover) {
//        super.onPostExecute(discover);

//        if there is discover movies
        if (discover != null) {
            Log.e(TAG, "DiscoverMoviesTask:onPostExecute: discover.getResults().size="+discover.getResults().size());
            ArrayList<RYMovie> movieArrayList = new ArrayList<>();

            for (int i = 0; i < discover.getResults().size(); i++) {
                Movie movieItem = discover.getResults().get(i);
                RYMovie ryMovie = new RYMovie(movieItem.getId(), movieItem.getPoster_path(), movieItem.getTitle(), movieItem.getRelease_date(), movieItem.getVote_average());
                movieArrayList.add(ryMovie);
            }

            mRYMovieAdapter.addAll(movieArrayList);

        }
        else {

            Toast.makeText(this.context, "Failed to get movies on server. Please check your internet connection.", Toast.LENGTH_LONG).show();
        }
    }
}

}
