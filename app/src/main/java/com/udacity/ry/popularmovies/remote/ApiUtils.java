package com.udacity.ry.popularmovies.remote;

import com.udacity.ry.popularmovies.BuildConfig;

/**
 * Created by netserve on 03/08/2018.
 */

public final class ApiUtils {
    public static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String MOVIES_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185/";
    public static final String MOVIES_BACKDROP_PATH_BASE_URL = "https://image.tmdb.org/t/p/w780/";

//    Movies DB api key
    public static final String API_KEY = BuildConfig.API_KEY;

//    body query properties
    public static final String api_key = "api_key";
    public static final String language = "language";
    public static final String sort_by = "sort_by";
    public static final String page = "page";

    private ApiUtils(){}

//get an instance of Movies API services
    public static MoviesServicesRemote getMoviesService() {
        return RetrofitClient.getClient(MOVIES_BASE_URL).create(MoviesServicesRemote.class);
    }

}
