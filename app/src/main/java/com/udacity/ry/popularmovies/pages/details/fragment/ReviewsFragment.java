package com.udacity.ry.popularmovies.pages.details.fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.adapter.ReviewsAdapter;
import com.udacity.ry.popularmovies.interfaces.FindMovieReviewsListener;
import com.udacity.ry.popularmovies.remote.model.Reviews;
import com.udacity.ry.popularmovies.remote.model.ReviewsItem;
import com.udacity.ry.popularmovies.task.MovieReviewsTask;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment implements FindMovieReviewsListener {
    private static final String TAG = ReviewsFragment.class.getSimpleName();

    private RecyclerView mRecyclerview;
    private TextView mErrorTV;
    private ProgressBar mProgressBar;

    //    adapter reviews
    private ReviewsAdapter mAdapter;

    private static long movieId = 0;
    private static String language;
    //    list of reviews
    private ArrayList<ReviewsItem> reviewsItemsList;

    private MovieReviewsTask movieReviewsTask;
    private int reviewsListPage = 1;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(long idMovie, String lang) {
//        passage des parametres de la requete au fragment
        movieId = idMovie;
        language = lang;
        Log.e(TAG, "newInstance: movieId="+movieId+" language="+language);

        Bundle args = new Bundle();

        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        mRecyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview_reviews);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_reviews);
        mErrorTV = (TextView) rootView.findViewById(R.id.tv_reviews_error);
        reviewsItemsList = new ArrayList<>();

        mAdapter = new ReviewsAdapter(getActivity(), reviewsItemsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                int lastPosition = recyclerView.getLastVisiblePosition();
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                Log.e(TAG, "onScroll: lastVisible="+lastVisible+" totalItemCount="+totalItemCount);
                if (lastVisible > 0 && (lastVisible + 1) == totalItemCount) {
                    fetchReviews();
                }
            }
        });

        fetchReviews();

        return rootView;
    }

//    check is there is internet connection on device
    private boolean isPhoneConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null) &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void fetchReviews() {
        if (!isPhoneConnected()) {
            Toast.makeText(getContext(), getContext().getString(R.string.connection_error_discover_movies), Toast.LENGTH_LONG).show();

//            show error message TextView
            displayErroTV(true);
            return;
        }
        displayLoading(true);
        if (movieReviewsTask == null) {
            movieReviewsTask = new MovieReviewsTask(ReviewsFragment.this, movieId, language, reviewsListPage);
            movieReviewsTask.execute();
        }
    }
    private void displayLoading(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    private void displayErroTV(boolean show) {
        mErrorTV.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFindMovieReviewsTaskCompleted(Reviews reviews) {

//        if there is discover movies
        if (reviews != null) {
            Log.e(TAG, "DiscoverMoviesTask:onPostExecute: discover.getResults().size=" + reviews.getResults().size());


//            increment page count
            reviewsListPage++;
            reviewsItemsList.addAll(reviews.getResults());

            mAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.connection_error_discover_movies), Toast.LENGTH_LONG).show();

//            show error message TextView
            displayErroTV(true);
        }

        movieReviewsTask = null;

//            hide progressBar
        displayLoading(false);
    }
}
