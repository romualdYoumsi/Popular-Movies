package com.udacity.ry.popularmovies.remote;

import com.udacity.ry.popularmovies.remote.model.Discover;
import com.udacity.ry.popularmovies.remote.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by netserve on 03/08/2018.
 */

public interface MoviesServicesRemote {

    //  Getting the discover movies from the movie database
    @GET("discover/movie")
    Call<Discover> discoverMovie(@Query(ApiUtils.page) int page,
                                 @Query(ApiUtils.language) String language,
                                 @Query(ApiUtils.sort_by) String sort_by);

    //  Getting the details of a movie from the movie database
    @GET("movie/{movieId}")
    Call<Movie> movieDetails(@Path("movieId") long movieId,
                             @Query(ApiUtils.language) String language);
}
