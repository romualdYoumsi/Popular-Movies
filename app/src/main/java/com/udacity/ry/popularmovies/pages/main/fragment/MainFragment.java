package com.udacity.ry.popularmovies.pages.main.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.udacity.ry.popularmovies.database.AppDataBase;
import com.udacity.ry.popularmovies.database.MovieEntry;
import com.udacity.ry.popularmovies.interfaces.OnDiscoverMoviesTaskCompleted;
import com.udacity.ry.popularmovies.model.RYMovie;
import com.udacity.ry.popularmovies.pages.main.viewmodel.MainViewModel;
import com.udacity.ry.popularmovies.remote.model.Discover;
import com.udacity.ry.popularmovies.remote.model.Movie;
import com.udacity.ry.popularmovies.task.DiscoverMoviesTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, OnDiscoverMoviesTaskCompleted {


    public static final String TAG = MainFragment.class.getSimpleName();

    private RYMovieAdapter mRYMovieAdapter;
    ArrayList<RYMovie> mRYMovies = new ArrayList<>();

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mErrorTV;

    private int firstVisiblePositionGV;
    private int movieListPage = 1;

    private DiscoverMoviesTask mDiscoverMoviesTask = null;

    // Member variable for the Database
    private AppDataBase mDb;

    public MainFragment() {
        // Required empty public constructor
//        Log.e(TAG, "MainFragment:");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            firstVisiblePositionGV = savedInstanceState.getInt("firstVisiblePositionGV");
//            Log.e(TAG, "onCreate: savedInstanceState not null firstVisiblePositionGV="+firstVisiblePositionGV);
        }

        mDb = AppDataBase.getsInstance(getContext().getApplicationContext());
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

//                Log.e(TAG, "onScroll: lastPosition="+lastPosition+" itemsCount="+itemsCount+" isConnected="+isConnected );
                if (lastPosition > 0 && (lastPosition + 1) == itemsCount) {
                    executeDiscovering();
                }
            }
        });
        /*mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RYMovie ryMovie = (RYMovie) parent.getItemAtPosition(position);
//                Log.e(TAG, "onItemClick: position="+position+" title="+ryMovie.getTitle()+" Vote_average="+ryMovie.getVote_average()+" Release_date="+ryMovie.getRelease_date());

                if (view.getId() == R.id.iv_favourite) {
                    Log.e(TAG, "onItemClick: favourite click");
                }
                Intent i = new Intent(getContext(), DetailsActivity.class);

                //Sending movie item data to DetailsActivity
                i.putExtra("movie_item", ryMovie);

                getContext().startActivity(i);
            }
        });*/

        setUpSharedPreference();

        executeDiscovering();

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
            Log.e(TAG, "onActivityCreated: savedInstanceState not null firstVisiblePositionGV=" + firstVisiblePositionGV);

        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            firstVisiblePositionGV = savedInstanceState.getInt("firstVisiblePositionGV");
            Log.e(TAG, "onViewStateRestored: savedInstanceState not null firstVisiblePositionGV=" + firstVisiblePositionGV);

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
            executeDiscovering();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    private boolean isPhoneConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null) &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void executeDiscovering() {

//        Getting the sharedPreference value
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort = sharedPreferences.getString(getContext().getString(R.string.pref_sort_order_key), getContext().getString(R.string.pref_sort_order_most_popular));
        String language = sharedPreferences.getString(getContext().getString(R.string.pref_language_key), getContext().getString(R.string.pref_language_fr));

//        Log.e(TAG, "executeDiscovering: sort=" + sort+" count="+mRYMovieAdapter.getCount());

        if (sort.equals(getString(R.string.pref_sort_order_favorites)) && mRYMovieAdapter.getCount() == 0) {
            loadLocalMovies();
        } else if (!sort.equals(getString(R.string.pref_sort_order_favorites))){
//            show progressBar
//            hide error message TextView
            mErrorTV.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            if (!isPhoneConnected()) {
                Toast.makeText(getContext(), getContext().getString(R.string.connection_error_discover_movies), Toast.LENGTH_LONG).show();

//            show error message TextView
                mErrorTV.setVisibility(View.VISIBLE);
                return;
            }
            if (mDiscoverMoviesTask == null) {
                mDiscoverMoviesTask = new DiscoverMoviesTask(MainFragment.this, movieListPage, sort, language);
                mDiscoverMoviesTask.execute();

//            increment page count
                movieListPage++;
                mDiscoverMoviesTask = null;
            }
        }

    }

    private void loadLocalMovies() {
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovieEntries().observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");

//                Getting the sharedPreference value
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String sort = sharedPreferences.getString(getContext().getString(R.string.pref_sort_order_key), getContext().getString(R.string.pref_sort_order_most_popular));
//                load local data movie if sort order equal to 'favorites'
                if (sort.equals(getString(R.string.pref_sort_order_favorites))) {
                    applyLocalMovies(movieEntries);
                }
            }
        });
    }

    private void applyLocalMovies(List<MovieEntry> movieEntries) {
        ArrayList<RYMovie> movieArrayList = new ArrayList<>();
        for (int i = 0; i < movieEntries.size(); i++) {
            MovieEntry movieEntry = movieEntries.get(i);

            RYMovie ryMovie = new RYMovie(movieEntry.getId(), movieEntry.getPoster_path(), movieEntry.getTitle(), movieEntry.getRelease_date(), movieEntry.getVote_average(), 1);
            movieArrayList.add(ryMovie);
        }
        mRYMovieAdapter.clear();
        mRYMovieAdapter.addAll(movieArrayList);

        mDiscoverMoviesTask = null;
    }

    @Override
    public void onTaskCompleted(Discover discoveredMovies) {

//        if there is discover movies
        if (discoveredMovies != null) {
            Log.e(TAG, "DiscoverMoviesTask:onPostExecute: discover.getResults().size=" + discoveredMovies.getResults().size());
            ArrayList<RYMovie> movieArrayList = new ArrayList<>();

            for (int i = 0; i < discoveredMovies.getResults().size(); i++) {
                Movie movieItem = discoveredMovies.getResults().get(i);
                RYMovie ryMovie = new RYMovie(movieItem.getId(), movieItem.getPoster_path(), movieItem.getTitle(), movieItem.getRelease_date(), movieItem.getVote_average(), 0);
                movieArrayList.add(ryMovie);
            }

//            increment page count
            movieListPage++;

            mRYMovieAdapter.addAll(movieArrayList);

//            scroll GridView to last position after phone rotation
            if (firstVisiblePositionGV > 0) {
                Log.e(TAG, "DiscoverMoviesTask:onPostExecute firstVisiblePositionGV=" + firstVisiblePositionGV);
//                mGridView.smoothScrollToPosition(firstVisiblePositionGV);
                mGridView.setSelection(firstVisiblePositionGV);
            } else {

//            hide error message TextView
                mErrorTV.setVisibility(View.VISIBLE);
            }

        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.connection_error_discover_movies), Toast.LENGTH_LONG).show();

//            show error message TextView
            mErrorTV.setVisibility(View.VISIBLE);
        }

        mDiscoverMoviesTask = null;

//            hide progressBar
        mProgressBar.setVisibility(View.GONE);
    }
}
