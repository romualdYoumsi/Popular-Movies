package com.udacity.ry.popularmovies.task;

import android.os.AsyncTask;
import android.util.Log;

import com.udacity.ry.popularmovies.interfaces.FindMovieReviewsListener;
import com.udacity.ry.popularmovies.remote.ApiUtils;
import com.udacity.ry.popularmovies.remote.model.Reviews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by netserve on 22/09/2018.
 */

public class MovieReviewsTask extends AsyncTask<Void, Void, Reviews> {
    private static final String TAG = MovieReviewsTask.class.getSimpleName();

    private FindMovieReviewsListener taskCompleted;

    private long movieId;
    private String language;
    private int page;

    public MovieReviewsTask(FindMovieReviewsListener activityContext, long movieId, String language, int page) {
        this.taskCompleted = activityContext;

        this.movieId = movieId;
        this.language = language;
        this.page = page;
    }

    @Override
    protected Reviews doInBackground(Void... voids) {
//        Requesting for movies to the movie db

        Call<Reviews> call = ApiUtils.getMoviesService().movieReviews(movieId, language, page);
        try {
            Response<Reviews> response = call.execute();
            if (response.isSuccessful()) {
                Reviews reviews = response.body();
                Log.e(TAG, "doInBackground: reviews.getResults().size=" + reviews.getResults().size() +
                        " page" + reviews.getPage() +
                        " id=" + reviews.getId() +
                        " Total_pages=" + reviews.getTotal_pages());
                return reviews;
            } else {
                String error = null;
                try {
                    error = response.errorBody().string();
                    JSONObject jsonObjectError = new JSONObject(error);
//                        String errorCode = jsonObjectError.getString("errorCode");
//                        String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "doInBackground: onResponse err: " + error);

                } catch (IOException e) {

                    e.printStackTrace();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Reviews reviews) {
//        super.onPostExecute(discover);

        this.taskCompleted.onFindMovieReviewsTaskCompleted(reviews);
    }
}
