package com.udacity.ry.popularmovies.remote.model;

/**
 * Created by netserve on 03/08/2018.
 */

public class Genres {
    private Long id;
    private String name;

    public Genres() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
