package com.example.user.letsgotoateith;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;


public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SearchFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            Utilities.logout(getApplication().getActivity());
////            SharedPreferences prefs=getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
////            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
////            prefs.edit().putInt(getString(R.string.pref_id_key),-1);
////            prefs.edit().putString(getString(R.string.pref_username_key),"-1");
////            prefs.edit().apply();
//              //finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
