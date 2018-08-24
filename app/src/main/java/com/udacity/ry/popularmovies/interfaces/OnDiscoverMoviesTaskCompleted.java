package com.udacity.ry.popularmovies.interfaces;

import com.udacity.ry.popularmovies.remote.model.Discover;

/**
 * Created by netserve on 21/08/2018.
 */

public interface OnDiscoverMoviesTaskCompleted {
    void onTaskCompleted(Discover discoveredMovies);
}
