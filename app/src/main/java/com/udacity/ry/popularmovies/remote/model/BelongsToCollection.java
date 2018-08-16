package com.udacity.ry.popularmovies.remote.model;

/**
 * Created by netserve on 03/08/2018.
 */

public class BelongsToCollection {
    private long id;
    private String name;
    private String poster_path;
    private String backdrop_path;

    public BelongsToCollection() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
