package com.udacity.ry.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Created by netserve on 20/09/2018.
 */

@Database(entities = {MovieEntry.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public static final String TAG = AppDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "ry_popularmovie";

    private static AppDataBase sInstance;

    public static AppDataBase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.e(TAG, "getsInstance: creating new database");

                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, AppDataBase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.e(TAG, "getsInstance: Getting the databse instance");
        return sInstance;
    }

    public abstract MovieDAO movieDAO();
}
