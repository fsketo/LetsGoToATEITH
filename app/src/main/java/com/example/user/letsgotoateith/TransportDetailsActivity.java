package com.example.user.letsgotoateith;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;


public class TransportDetailsActivity extends ActionBarActivity {

    private BroadcastReceiver brRe=new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive", "Logout in progress");
            //At this point you should start the login activity and finish this one
            finish();
        }
    };

    private BroadcastReceiver brRe1=new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive", "Refresh in progress");
            //At this point you should start the login activity and finish this one
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_details);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(brRe, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("com.package.ACTION_REFRESH");
        registerReceiver(brRe1, intentFilter1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(brRe);
        unregisterReceiver(brRe1);
    }
}
