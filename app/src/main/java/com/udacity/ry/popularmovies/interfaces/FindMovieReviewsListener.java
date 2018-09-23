package com.udacity.ry.popularmovies.interfaces;

import com.udacity.ry.popularmovies.remote.model.Reviews;

/**
 * Created by netserve on 22/09/2018.
 */

public interface FindMovieReviewsListener {
    void onFindMovieReviewsTaskCompleted(Reviews reviews);
}
