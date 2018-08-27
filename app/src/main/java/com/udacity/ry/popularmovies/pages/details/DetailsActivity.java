package com.udacity.ry.popularmovies.pages.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.model.RYMovie;
import com.udacity.ry.popularmovies.pages.settings.SettingsActivity;
import com.udacity.ry.popularmovies.remote.ApiUtils;
import com.udacity.ry.popularmovies.remote.model.Movie;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageView backdropPathIV, posterPathIV, homePageIV;
    private TextView originalTitleTV, releaseDateTV, voteAverageTV, overviewTV, genresTV, homePageTV, originalLanguageTV, popularityTV
            , productionCompagniesTV, productionCountriesTV, releaseDate2TV, spokenLanguagesTV, voteCountTV, voteAverage2TV;
    private RatingBar voteAverageRB;

    private RYMovie movieItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Reference view
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        backdropPathIV = (ImageView) findViewById(R.id.backdrop_path_iv);
        posterPathIV = (ImageView) findViewById(R.id.cardview_movie_thumbnail_iv);
        overviewTV = (TextView) findViewById(R.id.overview_tv);
        originalTitleTV = (TextView) findViewById(R.id.original_title_tv);
        releaseDateTV = (TextView) findViewById(R.id.cardview_movie_release_date);
        voteAverageTV = (TextView) findViewById(R.id.cardview_movie_vote_average_text);
        genresTV = (TextView) findViewById(R.id.genres_tv);
        homePageIV = (ImageView) findViewById(R.id.homepage_iv);
        homePageTV = (TextView) findViewById(R.id.homepage_tv);
        originalLanguageTV = (TextView) findViewById(R.id.original_language_tv);
        popularityTV = (TextView) findViewById(R.id.popularity_tv);
        productionCompagniesTV = (TextView) findViewById(R.id.production_companies_tv);
        productionCountriesTV = (TextView) findViewById(R.id.production_countries_tv);
        releaseDate2TV = (TextView) findViewById(R.id.release_date_tv);
        spokenLanguagesTV = (TextView) findViewById(R.id.spoken_language_tv);
        voteAverage2TV = (TextView) findViewById(R.id.vote_average_tv);
        voteCountTV = (TextView) findViewById(R.id.vote_count_tv);
        voteAverageRB = (RatingBar) findViewById(R.id.cardview_movie_vote_average_rating);


        if (savedInstanceState == null) {
                movieItem = (RYMovie) getIntent().getParcelableExtra("movie_item");

                if (movieItem == null) {
                    finish();
                }
                fetchMovieDetailsData(movieItem.getId());

        } else {
//            Log.e(TAG, "onCreate: savedInstanceState not null");
            movieItem = (RYMovie) savedInstanceState.getParcelable("movie_item");

            fetchMovieDetailsData(movieItem.getId());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the id of menu item clicked
        int id = item.getItemId();

//        if is toolbar back button then call device back button method
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "onSaveInstanceState: ");

        outState.putParcelable("movie_item", movieItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Log.e(TAG, "onRestoreInstanceState: ");
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void fetchMovieDetailsData(long movieId) {

//        Getting the sharedPreference value
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        String language = sharedPreferences.getString(getString(R.string.pref_language_key), getString(R.string.pref_language_fr));

//        Requesting for movies details in the movie db

        Call<Movie> call = ApiUtils.getMoviesService().movieDetails(movieId, language);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movieResponse = response.body();
                    Log.e(TAG, "onResponse: getOriginal_title" + movieResponse.getOriginal_title());

                    initContentView(movieResponse);
                } else {
                    finish();
                    try {
                        String erroString = response.errorBody().string().toString();
                        Log.e(TAG, "onResponse: erroString="+erroString );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, getString(R.string.connection_error_movie_details), Toast.LENGTH_LONG).show();

            }
        });
    }

    //    Set the value of all views
    private void initContentView(final Movie movie) {

        collapsingToolbarLayout.setTitle(movie.getTitle());
        Picasso.with(DetailsActivity.this).load(ApiUtils.MOVIES_BACKDROP_PATH_BASE_URL + movie.getBackdrop_path()).into(backdropPathIV);

//        Set views value of cardview
        Picasso.with(DetailsActivity.this).load(ApiUtils.MOVIES_POSTER_BASE_URL + movie.getPoster_path()).into(posterPathIV);
        overviewTV.setText(movie.getOverview());
        originalTitleTV.setText(movie.getOriginal_title());

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt2 = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat fmt3 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        try {
            Date date = fmt.parse(movie.getRelease_date());
            releaseDateTV.setText(fmt2.format(date).toUpperCase());

            releaseDate2TV.setText(fmt3.format(date).toUpperCase());
        } catch (ParseException pe) {
        }

//        extracting genres name in movie object
        String genresString = "";
        for (int i = 0; i < movie.getGenres().size(); i++) {
            genresString += movie.getGenres().get(i).getName()+", ";
        }
        if(genresString.length() > 2) {
            genresTV.setText(genresString.substring(0, genresString.length() - 2));
        }

        homePageTV.setText(movie.getHomepage());
        homePageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.getHomepage() != null && !movie.getHomepage().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getHomepage()));
                    startActivity(intent);

                }
            }
        });


        originalLanguageTV.setText(movie.getOriginal_language());
        popularityTV.setText(String.valueOf(movie.getPopularity()));

//        extracting production companies name in movie object
        String companies = "";
        for (int i = 0; i < movie.getProduction_companies().size(); i++) {
            companies += movie.getProduction_companies().get(i).getName()+", ";
        }
        if(companies.length() > 2) {
            productionCompagniesTV.setText(companies.substring(0, companies.length() - 2));
        }

//        extracting production companies name in movie object
        String countries = "";
        for (int i = 0; i < movie.getProduction_countries().size(); i++) {
            countries += movie.getProduction_countries().get(i).getName()+", ";
        }
        if(countries.length() > 2) {
            productionCountriesTV.setText(countries.substring(0, countries.length() - 2));
        }

//        extracting spoken languages name in movie object
        String languages = "";
        for (int i = 0; i < movie.getSpoken_languages().size(); i++) {
            languages += movie.getSpoken_languages().get(i).getName()+", ";
        }
        if(languages.length() > 2) {
            spokenLanguagesTV.setText(languages.substring(0, languages.length() - 2));
        }

        voteCountTV.setText(String.valueOf(movie.getVote_count()));
        voteAverage2TV.setText(String.valueOf(movie.getVote_average()));

        voteAverageTV.setText(String.valueOf(movie.getVote_average()));
        voteAverageRB.setRating(Float.parseFloat("" + (movie.getVote_average() / 2)));
    }

}
