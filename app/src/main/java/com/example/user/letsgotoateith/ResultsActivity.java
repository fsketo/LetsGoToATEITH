package com.example.user.letsgotoateith;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class ResultsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ResultsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.v("User ID", "User ID: " + getString(R.string.pref_id_key));

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences prefs=getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
            prefs.edit().putInt(getString(R.string.pref_id_key),-1);
            prefs.edit().putString(getString(R.string.pref_username_key),"-1");
            prefs.edit().commit();
            finish();
        }
        Log.v("User ID", "User ID: "+getString(R.string.pref_id_key));
        return super.onOptionsItemSelected(item);
    }

}
