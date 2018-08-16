package com.udacity.ry.popularmovies.remote.model;

/**
 * Created by netserve on 03/08/2018.
 */

public class Language {
    private String iso_639_1;
    private String name;

    public Language() {
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
