package com.udacity.ry.popularmovies.asyncTask;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.interfaces.OnDiscoverMoviesTaskCompleted;
import com.udacity.ry.popularmovies.remote.ApiUtils;
import com.udacity.ry.popularmovies.remote.model.Discover;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by netserve on 21/08/2018.
 */


//    Discover Movies async task
public class DiscoverMoviesTask extends AsyncTask<Void, Void, Discover> {
    private static final String TAG = DiscoverMoviesTask.class.getSimpleName();
    private Discover discoveredMovies = null;
    private OnDiscoverMoviesTaskCompleted taskCompleted;

    private int pageSize;
    private String sort;
    private String language;

    public DiscoverMoviesTask(OnDiscoverMoviesTaskCompleted activityContext, int pageSize, String sort, String language) {
        this.taskCompleted = activityContext;

        this.pageSize = pageSize;
        this.sort = sort;
        this.language = language;
    }

    @Override
    protected Discover doInBackground(Void... voids) {
//        Requesting for movies to the movie db

        Call<Discover> call = ApiUtils.getMoviesService().discoverMovie(sort, pageSize, language);
        try {
            Response<Discover> response = call.execute();
            if (response.isSuccessful()) {
                Discover discover = response.body();
                Log.e(TAG, "DiscoverMoviesTask:doInBackground: discover.getResults().size="+discover.getResults().size());
                return discover;
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
    protected void onPostExecute(Discover discover) {
//        super.onPostExecute(discover);

        this.taskCompleted.onTaskCompleted(discover);
    }
}

