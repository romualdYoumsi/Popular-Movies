package com.udacity.ry.popularmovies.pages.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.ry.popularmovies.R;
import com.udacity.ry.popularmovies.pages.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the id of menu item clicked
        int id = item.getItemId();

        //if the menu id correspond to settings, go to settings activity
        if (id == R.id.action_settings) {
            Log.e(TAG, "onOptionsItemSelected: action_settings");

            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
