package com.udacity.ry.popularmovies.remote.model;

import java.util.ArrayList;

/**
 * Created by netserve on 03/08/2018.
 */

public class Discover {
    private Integer page;
    private Integer total_results;
    private Integer total_pages;
    private ArrayList<Movie> results;

    public Discover() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
