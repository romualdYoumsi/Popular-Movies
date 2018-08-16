package com.udacity.ry.popularmovies.remote.model;

/**
 * Created by netserve on 03/08/2018.
 */

public class Country {
    private String iso_3166_1;
    private String name;
    private String english_name;

    public Country() {
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }
}
