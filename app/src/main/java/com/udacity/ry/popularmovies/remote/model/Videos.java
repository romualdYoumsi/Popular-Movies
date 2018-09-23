package com.udacity.ry.popularmovies.remote.model;

import java.util.ArrayList;

/**
 * Created by netserve on 08/09/2018.
 */

public class Videos {
    private Long id;
    private ArrayList<VideosItem> results;

    public Videos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<VideosItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<VideosItem> results) {
        this.results = results;
    }
}
