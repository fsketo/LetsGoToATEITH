package com.example.user.letsgotoateith;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class OwnCarActivity extends ActionBarActivity{


    private BroadcastReceiver brRe=new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive", "Logout in progress");
            //At this point you should start the login activity and finish this one
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_car);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new OwnACarFragment())
                    .commit();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(brRe, intentFilter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.v("Own a car menu created", "Own a car menu created");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_logout) {
//            SnackBar snackBar = new SnackBar(get, "Are you sure you want to logout?", "Yes", new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE);
//                    prefs.edit().putInt(context.getString(R.string.pref_id_key), -1);
//                    prefs.edit().putString(context.getString(R.string.pref_username_key), "-1");
//                    prefs.edit().commit();
//                    Log.v("User ID", "#####********User ID:" + prefs.getInt(context.getString(R.string.pref_id_key), -5555));
//                    context.finish();
//                }
//            });
//            snackBar.show();
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(brRe);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_logout) {
////            SharedPreferences prefs=getApplication().getSharedPreferences("session", Context.MODE_PRIVATE);
////            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
////            prefs.edit().putInt(getString(R.string.pref_id_key),-1);
////            prefs.edit().putString(getString(R.string.pref_username_key),"-1");
////            prefs.edit().commit();
////            finish();
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
