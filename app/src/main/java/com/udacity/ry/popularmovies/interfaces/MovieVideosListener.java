package com.udacity.ry.popularmovies.interfaces;

import com.udacity.ry.popularmovies.remote.model.Videos;

/**
 * Created by netserve on 08/09/2018.
 */

public interface MovieVideosListener {
    void onMovieVideosTaskCompleted(Videos videos);
}
