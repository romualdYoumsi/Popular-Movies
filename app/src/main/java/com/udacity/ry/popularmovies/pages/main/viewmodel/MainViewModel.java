package com.udacity.ry.popularmovies.pages.main.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.udacity.ry.popularmovies.database.AppDataBase;
import com.udacity.ry.popularmovies.database.MovieEntry;

import java.util.List;

/**
 * Created by netserve on 21/09/2018.
 */

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<MovieEntry>> movieEntries;

    public MainViewModel(Application application) {
        super(application);
        AppDataBase database = AppDataBase.getsInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        movieEntries = database.movieDAO().getAllMovies();
    }

    public LiveData<List<MovieEntry>> getMovieEntries() {
        return movieEntries;
    }
}
