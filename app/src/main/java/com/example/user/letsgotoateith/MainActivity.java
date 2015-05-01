package com.example.user.letsgotoateith;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback{

    static boolean mTwoPane;
    private String OWNACARFRAGMENT_TAG="OAC";
    private String SEARCHACARFRAGMENT_TAG="SAC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragmentContainer) != null) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new OwnACarFragment(), OWNACARFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
            }
            getSupportActionBar().setElevation(0f);
        }

//        MainFragment ownACarFragment =  ((MainFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.fragmentContainer));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.v("User ID", "User ID: "+getString(R.string.pref_id_key));

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences prefs=getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
            prefs.edit().remove(getString(R.string.pref_id_key));
            prefs.edit().remove(getString(R.string.pref_username_key));
            prefs.edit().commit();
            finish();
        }
        Log.v("User ID", "User ID: "+getString(R.string.pref_id_key));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int button) {
        if(button==R.id.ownAcar) {
           OwnACarFragment fragment = new OwnACarFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, OWNACARFRAGMENT_TAG)
                    .commit();
        }
        else if(button==R.id.lookingForCar){
            SearchFragment fragment = new SearchFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, SEARCHACARFRAGMENT_TAG)
                    .commit();
        }

        else if(button==R.id.manageTrans) {
            Toast.makeText(getApplication(),"Function not supported yet!",Toast.LENGTH_LONG).show();
        }

    }
}
