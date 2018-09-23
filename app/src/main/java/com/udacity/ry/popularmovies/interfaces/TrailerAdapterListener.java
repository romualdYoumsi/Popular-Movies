package com.udacity.ry.popularmovies.interfaces;

import com.udacity.ry.popularmovies.remote.model.VideosItem;

/**
 * Created by netserve on 22/09/2018.
 */

public interface TrailerAdapterListener {
    void onTrailerClickListener(VideosItem trailer);
}
