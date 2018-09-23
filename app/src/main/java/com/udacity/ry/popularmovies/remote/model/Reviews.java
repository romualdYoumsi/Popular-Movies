package com.udacity.ry.popularmovies.remote.model;

import java.util.ArrayList;

/**
 * Created by netserve on 22/09/2018.
 */

public class Reviews {
    private long id;
    private int page;
    private ArrayList<ReviewsItem> results;
    private int total_pages;
    private int total_results;

    public Reviews() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<ReviewsItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewsItem> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
