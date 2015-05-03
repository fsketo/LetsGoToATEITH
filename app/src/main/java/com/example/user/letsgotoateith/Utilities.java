package com.example.user.letsgotoateith;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.gc.materialdesign.widgets.SnackBar;

/**
 * Created by user on 1/5/2015.
 */
public class Utilities {

    public static void logout(final Activity context){
        new SnackBar(context, "Are you sure you want to logout?", "Yes", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences prefs=context.getSharedPreferences(context.getString(R.string.pref_key), Context.MODE_PRIVATE);
                prefs.edit().putInt(context.getString(R.string.pref_id_key),-1);
                prefs.edit().putString(context.getString(R.string.pref_username_key), "-1");
                prefs.edit().commit();
                Log.v("User ID","#####********User ID:"+prefs.getInt(context.getString(R.string.pref_id_key),-5555));
                context.finish();
            }
        }).show();
    }
}
