package com.udacity.ry.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by netserve on 04/08/2018.
 */

public class RYMovie implements Parcelable {
    private long id;
    private String poster_path;
    private String title;
    private String release_date;
    private double vote_average;
    private String overview;

    public RYMovie() {
    }

    public RYMovie(long id, String poster_path, String title, String release_date, double vote_average) {
        this.id = id;
        this.poster_path = poster_path;
        this.title = title;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(poster_path);
        parcel.writeString(title);
        parcel.writeString(release_date);
        parcel.writeDouble(vote_average);
        parcel.writeString(overview);
    }
    private RYMovie(Parcel in) {
        id = in.readLong();
        poster_path = in.readString();
        title = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        overview = in.readString();
    }
    public static final Parcelable.Creator<RYMovie> CREATOR
            = new Parcelable.Creator<RYMovie>() {

        @Override
        public RYMovie createFromParcel(Parcel in) {
            return new RYMovie(in);
        }

        @Override
        public RYMovie[] newArray(int size) {
            return new RYMovie[size];
        }
    };

    /*
    // somewhere inside an Activity
    MyParcelable dataToSend = new MyParcelable();
    Intent i = new Intent(this, NewActivity.class);
    i.putExtra("myDataKey", dataToSend); // using the (String name, Parcelable value) overload!
    startActivity(i); // dataToSend is now passed to the new Activity*/

    /*
    public class NewActivity extends Activity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            MyParcelable object = (MyParcelable) getIntent().getParcelableExtra("myDataKey");
        }
    }*/
}
