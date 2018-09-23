package com.udacity.ry.popularmovies.task;

import android.os.AsyncTask;
import android.util.Log;

import com.udacity.ry.popularmovies.interfaces.MovieVideosListener;
import com.udacity.ry.popularmovies.interfaces.OnDiscoverMoviesTaskCompleted;
import com.udacity.ry.popularmovies.remote.ApiUtils;
import com.udacity.ry.popularmovies.remote.model.Discover;
import com.udacity.ry.popularmovies.remote.model.Videos;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by netserve on 08/09/2018.
 */

public class MovieVideosTask extends AsyncTask<Void, Void, Videos> {
    private static final String TAG = DiscoverMoviesTask.class.getSimpleName();

    private MovieVideosListener taskCompleted;

    private long movieId;
    private String language;

    public MovieVideosTask(MovieVideosListener activityContext, long movieId, String language) {
        this.taskCompleted = activityContext;

        this.movieId = movieId;
        this.language = language;
    }

    @Override
    protected Videos doInBackground(Void... voids) {
//        Requesting for movies to the movie db

        Call<Videos> call = ApiUtils.getMoviesService().movieVideos(movieId, language);
        try {
            Response<Videos> response = call.execute();
            if (response.isSuccessful()) {
                Videos videos = response.body();
                Log.e(TAG, "DiscoverMoviesTask:doInBackground: discover.getResults().size="+videos.getResults().size());
                return videos;
            } else {
                String error = null;
                try {
                    error = response.errorBody().string();
                    JSONObject jsonObjectError = new JSONObject(error);
//                        String errorCode = jsonObjectError.getString("errorCode");
//                        String errorDetails = jsonObjectError.getString("errorDetails");
                    Log.e(TAG, "DiscoverMoviesTask:doLogin onResponse err: " + error);

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
    protected void onPostExecute(Videos videos) {
//        super.onPostExecute(discover);

        this.taskCompleted.onMovieVideosTaskCompleted(videos);
    }
}
