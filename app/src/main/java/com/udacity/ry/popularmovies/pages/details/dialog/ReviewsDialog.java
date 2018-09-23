package com.udacity.ry.popularmovies.pages.details.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.pages.details.fragment.ReviewsFragment;

/**
 * Created by netserve on 23/09/2018.
 */

public class ReviewsDialog extends DialogFragment {
    public static String TAG = ReviewsDialog.class.getSimpleName();

    //    views
    private ImageButton ibClose;

    //    Parametre de recuperation de la liste des reviews
    private static long movieId;
    private static String language;

    public ReviewsDialog() {
    }

    public static ReviewsDialog newInstance(long idMovie, String lang) {
//        passage des parametres de la requete au fragment
        movieId = idMovie;
        language = lang;
        Bundle args = new Bundle();

        ReviewsDialog fragment = new ReviewsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ClientProfileDialogStyle);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_reviews, container, false);


        ibClose = (ImageButton) view.findViewById(R.id.ib_dialog_reviews_close);

//        inflate fragment profile client on view
        Fragment fragment = ReviewsFragment.newInstance(movieId, language);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content_dialog_reviews, fragment).commit();

//        Close the modal
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialogCategorieListener.onCategorieSelected(null);
                dismiss();
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
