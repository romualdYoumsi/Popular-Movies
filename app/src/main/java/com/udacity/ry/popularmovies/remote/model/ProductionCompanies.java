package com.udacity.ry.popularmovies.remote.model;

/**
 * Created by netserve on 03/08/2018.
 */

public class ProductionCompanies {
    private Long id;
    private String logo_path;
    private String name;
    private String origin_country;

    public ProductionCompanies() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(String origin_country) {
        this.origin_country = origin_country;
    }
}
