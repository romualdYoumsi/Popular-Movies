package com.udacity.ry.popularmovies.pages.main.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.util.Locale;

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
    private ProgressBar mProgressBar;
    private TextView mErrorTV;

    private int firstVisiblePositionGV;
    private int movieListPage = 1;

    private DiscoverMoviesTask mDiscoverMoviesTask = null;

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

        if (savedInstanceState != null) {

            firstVisiblePositionGV = savedInstanceState.getInt("firstVisiblePositionGV");
//            Log.e(TAG, "onCreate: savedInstanceState not null firstVisiblePositionGV="+firstVisiblePositionGV);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        mErrorTV = (TextView) rootView.findViewById(R.id.error_tv);

        mRYMovieAdapter = new RYMovieAdapter(getActivity(), mRYMovies);

        mGridView.setAdapter(mRYMovieAdapter);
        mGridView.setOnScrollListener(new GridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                int lastPosition = absListView.getLastVisiblePosition();
                int itemsCount = absListView.getAdapter().getCount();

//                getting internet connection status
                ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null
                        && activeNetwork.isConnectedOrConnecting();

//                Log.e(TAG, "onScroll: lastPosition="+lastPosition+" itemsCount="+itemsCount+" isConnected="+isConnected );
                if (lastPosition > 0 && (lastPosition+1) == itemsCount && isConnected) {
                    if (mDiscoverMoviesTask == null) {
                        mDiscoverMoviesTask = new DiscoverMoviesTask(getContext());
                        mDiscoverMoviesTask.execute(movieListPage);
                    }
                }
            }
        });

        setUpSharedPreference();

        if (mDiscoverMoviesTask == null) {
            mDiscoverMoviesTask = new DiscoverMoviesTask(getContext());
            mDiscoverMoviesTask.execute(movieListPage);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        firstVisiblePositionGV = mGridView.getFirstVisiblePosition();
//        Log.e(TAG, "onSaveInstanceState: firstVisiblePositionGV="+firstVisiblePositionGV);
        outState.putInt("firstVisiblePositionGV", firstVisiblePositionGV);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
//        Log.e(TAG, "onActivityCreated:");

        if (savedInstanceState != null) {
            firstVisiblePositionGV = savedInstanceState.getInt("firstVisiblePositionGV");
            Log.e(TAG, "onActivityCreated: savedInstanceState not null firstVisiblePositionGV="+firstVisiblePositionGV);

        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            firstVisiblePositionGV = savedInstanceState.getInt("firstVisiblePositionGV");
            Log.e(TAG, "onViewStateRestored: savedInstanceState not null firstVisiblePositionGV="+firstVisiblePositionGV);

        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void setUpSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort = sharedPreferences.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_most_popular));

//        Log.e(TAG, "setUpSharedPreference: sort="+sort);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Log.e(TAG, "onSharedPreferenceChanged: key="+key+" value="+sharedPreferences.getString(key, ""));


//        remove all movies
        mRYMovieAdapter.clear();

//        fetch movies on server
        if (mDiscoverMoviesTask == null) {
//            reinitialisation of discover page
            movieListPage = 1;

            mDiscoverMoviesTask = new DiscoverMoviesTask(getContext());
            mDiscoverMoviesTask.execute(movieListPage);
        }
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

//            show progressBar
            mProgressBar.setVisibility(View.VISIBLE);
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

//            increment page count
            movieListPage++;

            mRYMovieAdapter.addAll(movieArrayList);

//            scroll GridView to last position after phone rotation
            if (firstVisiblePositionGV > 0) {
                Log.e(TAG, "DiscoverMoviesTask:onPostExecute firstVisiblePositionGV="+firstVisiblePositionGV);
//                mGridView.smoothScrollToPosition(firstVisiblePositionGV);
                mGridView.setSelection(firstVisiblePositionGV);
            }

//            hide error message TextView
            mErrorTV.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this.context, this.context.getString(R.string.connection_error), Toast.LENGTH_LONG).show();

//            show error message TextView
            mErrorTV.setVisibility(View.VISIBLE);
        }

        mDiscoverMoviesTask = null;

//            hide progressBar
        mProgressBar.setVisibility(View.GONE);
    }
}

}
