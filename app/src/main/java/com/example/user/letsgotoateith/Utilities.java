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
                prefs.edit().putInt(Constants.EXTRA_USERID,-1);
                prefs.edit().putString(context.getString(R.string.pref_username_key), "-1");
                prefs.edit().commit();
                Log.v("User ID","#####********User ID:"+prefs.getInt(Constants.EXTRA_USERID,-5555));
                context.finish();
            }
        }).show();
    }

    public static String printArray(String[] temp){
        String tmp="";
        for(int i=0;i<temp.length;i++){
            tmp=tmp+temp[i];
        }
        return tmp;
    }

    public static String printArray(String[][] temp, int i, int max){
        String tmp="";
        Log.v("****I","****I"+i+"   *****J"+max);
        for(int j=0;j<max;j++){
            tmp=tmp+temp[i][j];
        }
        return tmp;
    }
}
